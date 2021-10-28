package com.android.imageviewer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import androidx.annotation.Keep;

import com.github.chrisbanes.photoview.PhotoView;

import me.panpf.sketch.decode.ImageSizeCalculator;

public class DragView extends FrameLayout {
    private float mAlpha = 0;
    private float mDownX;
    private float mDownY;
    private float mYDistanceTraveled;
    private float mXDistanceTraveled;
    private float mTranslateY;
    private float mTranslateX;

    private final float DEFAULT_MIN_SCALE = 0.3f;
    private int MAX_TRANSLATE_Y = 0;
    private int MAX_Y = 0;

    FrameLayout contentLayout;
    View backgroundView;

    private final long DEFAULT_DURATION = 300;
    long animationDuration = DEFAULT_DURATION;

    private int mOriginLeft;
    private int mOriginTop;
    private int mOriginHeight;
    private int mOriginWidth;

    private final int screenWidth;
    private final int screenHeight;

    private int targetImageTop;
    private int targetImageWidth;
    private int targetImageHeight;

    private int mCurrentLeft;
    private int mCurrentTop;
    private int mCurrentHeight;
    private int mCurrentWidth;

    private float mLastY;
    private float mLastX;

    int minWidth = 0;
    int minHeight = 0;

    int releaseLeft = 0;
    float releaseY = 0;
    int releaseWidth = 0;
    int releaseHeight = 0;
    int realWidth;
    int realHeight;
    int touchSlop = ViewConfiguration.getTouchSlop();

    int imageLeftOfAnimatorEnd = 0;
    int imageTopOfAnimatorEnd = 0;
    int imageWidthOfAnimatorEnd = 0;
    int imageHeightOfAnimatorEnd = 0;

    boolean isMulitFinger = false;
    boolean isDrag = false;
    boolean isLongHeightImage = false;//是否是高度长图
    boolean isLongWidthImage = false;//是否是宽度长图
    boolean isAnimating = false;//是否在动画中
    boolean isPhoto = false;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        MAX_TRANSLATE_Y = screenHeight / 6;
        MAX_Y = screenHeight - screenHeight / 8;

        addView(LayoutInflater.from(getContext()).inflate(R.layout.content_item, null), 0);
        contentLayout = findViewById(R.id.contentLayout);
        backgroundView = findViewById(R.id.backgroundView);
        setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        View view = getContentView();
        if (view instanceof ImageView) {
            ImageView sketchImageView = (ImageView) view;
            //如果是长图  没有缩放到最小,则不给事件
//            if (sketchImageView.getZoomer() != null) {
//                if (isLongHeightImage || isLongWidthImage) {
//                    if (sketchImageView.getZoomer().getZoomScale() > sketchImageView.getZoomer().getMinZoomScale()) {
//                        return super.dispatchTouchEvent(event);
//                    }
//                } else if ((Math.round(sketchImageView.getZoomer().getSupportZoomScale() * 1000) / 1000f) > 1) {
//                    //如果对图片进行缩放或者缩小操作 则不给事件
//                    return super.dispatchTouchEvent(event);
//                }
//            }
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                isMulitFinger = true;
                break;
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mLastX = mDownX;
                mLastY = mDownY;
                mTranslateX = 0;
                mTranslateY = 0;
                //触摸背景需要捕捉事件
                if (!isTouchPointInContentLayout(contentLayout, event)) {
                    mLastX = x;
                    mLastY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                float dx = moveX - mLastX;
                float dy = moveY - mLastY;

                mTranslateX = moveX - mDownX;
                mTranslateY = moveY - mDownY;
                // 禁止向上滑动
                if (mTranslateY <= 0) {
                    break;
                }

                mYDistanceTraveled += Math.abs(mTranslateY);
                mXDistanceTraveled += Math.abs(mTranslateX);


                if (isAnimating) {
                    break;
                }

                if (view instanceof ImageView && (isLongHeightImage || isLongWidthImage)) {
                    //长图缩放到最小比例  拖动时显示方式需要更新  并且不能启用阅读模式
                    ImageView sketchImageView = (ImageView) view;
//                    if (sketchImageView.getZoomer() != null) {
//                        sketchImageView.getZoomer().setReadMode(false);
//                    }
                    changeImageViewToFitCenter();
                }
                if (event.getPointerCount() != 1 || isMulitFinger) {
                    isMulitFinger = true;
                    break;
                }

                //如果滑动距离不足,则不需要事件
                if (Math.abs(mYDistanceTraveled) < touchSlop || (Math.abs(mTranslateX) > Math.abs(mYDistanceTraveled) && !isDrag)) {
                    mYDistanceTraveled = 0;
                    if (isTouchPointInContentLayout(contentLayout, event)) {
                        break;
                    }
                    break;
                }
                if (mDragListener != null) {
                    mDragListener.onDrag(this, mTranslateX, mTranslateY);
                }
                isDrag = true;

                //根据触摸点的Y坐标和屏幕的比例来更改透明度
                float alphaChangePercent = mTranslateY / screenHeight;
                mAlpha = 1f - alphaChangePercent;
                mCurrentTop += 1.1 * dy;
                mCurrentLeft += dx;

                int newWidth;
                int newHeight;
                ImageSizeCalculator sizeCalculator = new ImageSizeCalculator();
                if (sizeCalculator.canUseReadModeByHeight(realWidth, realHeight) ||
                        sizeCalculator.canUseReadModeByWidth(realWidth, realHeight) ||
                        screenWidth / (float) screenHeight < realWidth / (float) realHeight) {
                    isLongHeightImage = sizeCalculator.canUseReadModeByHeight(realWidth, realHeight) && getContentView() instanceof ImageView;
                    isLongWidthImage = sizeCalculator.canUseReadModeByWidth(realWidth, realHeight) && getContentView() instanceof ImageView;
                    newWidth = screenWidth;
                    newHeight = (int) (newWidth * (realHeight / (float) realWidth));
                    if (newHeight >= screenHeight || sizeCalculator.canUseReadModeByWidth(realWidth, realHeight)) {
                        newHeight = screenHeight;
                    }
                } else {
                    newHeight = screenHeight;
                    newWidth = (int) (newHeight * (realWidth / (float) realHeight));
                }

                final int endLeft = (screenWidth - newWidth) / 2;
                final int endHeight = newHeight;
                final int endWidth = newWidth;


                FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                layoutParams.width = (int) (endWidth * (1 - alphaChangePercent));
                layoutParams.height = (int) (endHeight * (1 - alphaChangePercent));
                layoutParams.leftMargin = mCurrentLeft;
                layoutParams.topMargin = mCurrentTop;
                contentLayout.setLayoutParams(layoutParams);

                mCurrentWidth = layoutParams.width;
                mCurrentHeight = layoutParams.height;

                backgroundView.setAlpha(mAlpha);
                mLastX = moveX;
                mLastY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                if (isAnimating) {
                    break;
                }
                //如果滑动距离不足,则不需要事件
                if (Math.abs(mYDistanceTraveled) < touchSlop || (Math.abs(mYDistanceTraveled) > Math.abs(mYDistanceTraveled) && !isDrag)) {
                    if (!isMulitFinger && onClickListener != null) {
                        onClickListener.onClick(DragView.this);
                    }
                    isMulitFinger = false;
                    if (isTouchPointInContentLayout(contentLayout, event)) {
                        break;
                    }
                    break;
                }
                //防止滑动时出现多点触控
                if (isMulitFinger && !isDrag) {
                    isMulitFinger = false;
                    break;
                }
                isMulitFinger = false;
                if (mTranslateY > MAX_TRANSLATE_Y) {
                    onBackPress();
                } else {
                    backToNormal();
                }
                isDrag = false;
                mYDistanceTraveled = 0;
                break;
        }

        mLastY = y;
        return super.dispatchTouchEvent(event);
    }

    private boolean isTouchPointInContentLayout(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return y >= top && y <= bottom && x >= left
                && x <= right;
    }


    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
    }

    public void addContentChildView(View view) {
        ViewGroup parentViewGroup = (ViewGroup) view.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeView(view);
        }
        if (view instanceof ImageView) {
            ImageView sketchImageView = (ImageView) view;
//            if (sketchImageView.getZoomer() != null) {
//                sketchImageView.getZoomer().setReadMode(true);
//                sketchImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        backToMin();
//                    }
//                });
//            }
            sketchImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContentView() instanceof PhotoView) {
                        PhotoView photoView = (PhotoView) getContentView();
                        float scale = photoView.getScale();
                        photoView.setScale(1, true);
                        photoView.setVisibility(View.VISIBLE);
                    }
                    onBackPress();
                }
            });
            sketchImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        contentLayout.addView(view);
    }

    public void show(boolean showAnimation) {
        setVisibility(View.VISIBLE);
        mAlpha = showAnimation ? 0 : 1;

//        mOriginTop = mOriginTop + Utils.calcStatusBarHeight(getContext());
        FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
        layoutParams.width = mOriginWidth;
        layoutParams.height = mOriginHeight;
        layoutParams.leftMargin = mOriginLeft;
        layoutParams.topMargin = mOriginTop;
        contentLayout.setLayoutParams(layoutParams);

        if (showAnimation)
            changeBackgroundViewAlpha(0, 1, false);


        int newWidth;
        int newHeight;
        ImageSizeCalculator sizeCalculator = new ImageSizeCalculator();
        if (sizeCalculator.canUseReadModeByHeight(realWidth, realHeight) ||
                sizeCalculator.canUseReadModeByWidth(realWidth, realHeight) ||
                screenWidth / (float) screenHeight < realWidth / (float) realHeight) {
            isLongHeightImage = sizeCalculator.canUseReadModeByHeight(realWidth, realHeight) && getContentView() instanceof ImageView;
            isLongWidthImage = sizeCalculator.canUseReadModeByWidth(realWidth, realHeight) && getContentView() instanceof ImageView;
            newWidth = screenWidth;
            newHeight = (int) (newWidth * (realHeight / (float) realWidth));
            if (newHeight >= screenHeight || sizeCalculator.canUseReadModeByWidth(realWidth, realHeight)) {
                newHeight = screenHeight;
                newWidth = (int) (newHeight * (realWidth / (float) realHeight));
            }
        } else {
            newHeight = screenHeight;
            newWidth = (int) (newHeight * (realWidth / (float) realHeight));
        }

        final int endLeft = (screenWidth - newWidth) / 2;
        final int endHeight = newHeight;
        final int endWidth = newWidth;
        if (!showAnimation) {
            changeImageViewToFitCenter();
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = 0;

            mCurrentLeft = endLeft;
            mCurrentTop = (screenHeight - endHeight) / 2;
            mCurrentWidth = endWidth;
            mCurrentHeight = endHeight;
            contentLayout.setLayoutParams(layoutParams);
            return;
        }
        if (mOriginLeft != endLeft) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mOriginLeft, endLeft);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    float percent = (mOriginLeft - value) / (mOriginLeft - endLeft);

                    float leftMargin = value;
                    float topMargin = ((screenHeight - endHeight) / 2f - mOriginTop) * percent + mOriginTop;
                    float width = percent * (endWidth - mOriginWidth) + mOriginWidth;
                    float height = percent * (endHeight - mOriginHeight) + mOriginHeight;

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) height;
                    layoutParams.leftMargin = (int) leftMargin;
                    layoutParams.topMargin = (int) topMargin;
                    contentLayout.setLayoutParams(layoutParams);

                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) screenWidth;
                    layoutParams.height = (int) screenHeight;
                    layoutParams.leftMargin = 0;
                    layoutParams.topMargin = 0;

                    mCurrentLeft = endLeft;
                    mCurrentTop = (screenHeight - endHeight) / 2;
                    mCurrentWidth = endWidth;
                    mCurrentHeight = endHeight;
                    contentLayout.setLayoutParams(layoutParams);
                    changeImageViewToFitCenter();
                }
            });
            valueAnimator.setDuration(animationDuration).start();
        } else {
            int endTop = (screenHeight - endHeight) / 2;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mOriginTop, endTop);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    float topPercent = (mOriginTop - value) / (mOriginTop - endTop);

                    float leftMargin = mOriginLeft;
                    float topMargin = value;
                    float width = topPercent * (endWidth - mOriginWidth) + mOriginWidth;
                    float height = topPercent * (endHeight - mOriginHeight) + mOriginHeight;

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) height;
                    layoutParams.leftMargin = (int) leftMargin;
                    layoutParams.topMargin = (int) topMargin;
                    contentLayout.setLayoutParams(layoutParams);

                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    changeImageViewToFitCenter();
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) screenWidth;
                    layoutParams.height = (int) screenHeight;
                    layoutParams.leftMargin = 0;
                    layoutParams.topMargin = 0;

                    mCurrentLeft = endLeft;
                    mCurrentTop = (screenHeight - endHeight) / 2;
                    mCurrentWidth = endWidth;
                    mCurrentHeight = endHeight;
                    contentLayout.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.setDuration(animationDuration).start();
        }

        getLocation(mOriginWidth, mOriginHeight, showAnimation);
    }

    private void getLocation(float minViewWidth, float minViewHeight, final boolean showImmediately) {
        int[] locationImage = new int[2];
        contentLayout.getLocationOnScreen(locationImage);
        float targetSize;
        targetImageWidth = screenWidth;
    }

    public void putData(int left, int top, int originWidth, int originHeight, int realWidth, int realHeight) {
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        mOriginLeft = left;
        mOriginTop = top;
        mOriginWidth = originWidth;
        mOriginHeight = originHeight;
    }


    private void changeImageViewToFitCenter() {
        if (getContentView() instanceof ImageView) {
            ((ImageView) getContentView()).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    private void changeImageViewToCenterCrop() {
        if (getContentView() instanceof ImageView) {
            ((ImageView) getContentView()).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public void backToNormal() {
        int newWidth;
        int newHeight;
        ImageSizeCalculator sizeCalculator = new ImageSizeCalculator();
        if (sizeCalculator.canUseReadModeByHeight(realWidth, realHeight) ||
                sizeCalculator.canUseReadModeByWidth(realWidth, realHeight) ||
                screenWidth / (float) screenHeight < realWidth / (float) realHeight) {
            isLongHeightImage = sizeCalculator.canUseReadModeByHeight(realWidth, realHeight) && getContentView() instanceof ImageView;
            isLongWidthImage = sizeCalculator.canUseReadModeByWidth(realWidth, realHeight) && getContentView() instanceof ImageView;
            newWidth = screenWidth;
            newHeight = (int) (newWidth * (realHeight / (float) realWidth));
            if (newHeight >= screenHeight || sizeCalculator.canUseReadModeByWidth(realWidth, realHeight)) {
                newHeight = screenHeight;
            }
        } else {
            newHeight = screenHeight;
            newWidth = (int) (newHeight * (realWidth / (float) realHeight));
        }

        final int endLeft = (screenWidth - newWidth) / 2;
        final int endHeight = newHeight;
        final int endWidth = newWidth;

        if (endLeft != mCurrentLeft) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurrentLeft, endLeft);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    float percent = (mCurrentLeft - value) / (mCurrentLeft - endLeft);
                    float leftMargin = value;
//                float topMargin = (screenHeight - mCurrentHeight) / 2f - ((screenHeight - mCurrentHeight) / 2f - mOriginTop) * percent;
                    float topMargin = mCurrentTop - percent * (mCurrentTop - (screenHeight - endHeight) / 2f);
                    float width = mCurrentWidth + percent * (endWidth - mCurrentWidth);
                    float height = mCurrentHeight + percent * (endHeight - mCurrentHeight);

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) height;
                    layoutParams.leftMargin = (int) leftMargin;
                    layoutParams.topMargin = (int) topMargin;
                    contentLayout.setLayoutParams(layoutParams);

                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) screenWidth;
                    layoutParams.height = (int) screenHeight;
                    layoutParams.leftMargin = 0;
                    layoutParams.topMargin = 0;

                    mCurrentLeft = endLeft;
                    mCurrentTop = (screenHeight - endHeight) / 2;
                    mCurrentWidth = endWidth;
                    mCurrentHeight = endHeight;
                    contentLayout.setLayoutParams(layoutParams);


                }
            });
            valueAnimator.setDuration(animationDuration).start();
        } else {
            int endTop = (screenHeight - endHeight) / 2;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurrentTop, endTop);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    float percent = (mCurrentLeft - value) / (mCurrentTop - endTop);
                    float leftMargin = mCurrentLeft;
//                float topMargin = (screenHeight - mCurrentHeight) / 2f - ((screenHeight - mCurrentHeight) / 2f - mOriginTop) * percent;
                    float topMargin = value;
                    float width = mCurrentWidth + percent * (endWidth - mCurrentWidth);
                    float height = mCurrentHeight + percent * (endHeight - mCurrentHeight);

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) height;
                    layoutParams.leftMargin = (int) leftMargin;
                    layoutParams.topMargin = (int) topMargin;
                    contentLayout.setLayoutParams(layoutParams);

                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) screenWidth;
                    layoutParams.height = (int) screenHeight;
                    layoutParams.leftMargin = 0;
                    layoutParams.topMargin = 0;

                    mCurrentLeft = endLeft;
                    mCurrentTop = (screenHeight - endHeight) / 2;
                    mCurrentWidth = endWidth;
                    mCurrentHeight = endHeight;
                    contentLayout.setLayoutParams(layoutParams);


                }
            });
            valueAnimator.setDuration(animationDuration).start();
        }
        changeBackgroundViewAlpha(mAlpha, 1, false);
    }

    public void onBackPress() {
        if (mCurrentLeft != mOriginLeft) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurrentLeft, mOriginLeft);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    float percent = (mCurrentLeft - value) / (mCurrentLeft - mOriginLeft);
                    float leftMargin = value;
//                float topMargin = (screenHeight - mCurrentHeight) / 2f - ((screenHeight - mCurrentHeight) / 2f - mOriginTop) * percent;
                    float topMargin = mCurrentTop - percent * (mCurrentTop - mOriginTop);
                    float width = mCurrentWidth - percent * (mCurrentWidth - mOriginWidth);
                    float height = mCurrentHeight - percent * (mCurrentHeight - mOriginHeight);

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) height;
                    layoutParams.leftMargin = (int) leftMargin;
                    layoutParams.topMargin = (int) topMargin;
                    contentLayout.setLayoutParams(layoutParams);

                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    changeImageViewToCenterCrop();
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = mOriginWidth;
                    layoutParams.height = mOriginHeight;
                    layoutParams.leftMargin = mOriginLeft;
                    layoutParams.topMargin = mOriginTop;
                    contentLayout.setLayoutParams(layoutParams);


                }
            });
            valueAnimator.setDuration(animationDuration).start();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurrentTop, mOriginTop);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    float percent = (mCurrentTop - value) / (mCurrentTop - mOriginTop);
                    float leftMargin = mOriginLeft;
//                float topMargin = (screenHeight - mCurrentHeight) / 2f - ((screenHeight - mCurrentHeight) / 2f - mOriginTop) * percent;
                    float topMargin = value;
                    float width = mCurrentWidth - percent * (mCurrentWidth - mOriginWidth);
                    float height = mCurrentHeight - percent * (mCurrentHeight - mOriginHeight);

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) height;
                    layoutParams.leftMargin = (int) leftMargin;
                    layoutParams.topMargin = (int) topMargin;
                    contentLayout.setLayoutParams(layoutParams);

                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    changeImageViewToCenterCrop();
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) contentLayout.getLayoutParams();
                    layoutParams.width = mOriginWidth;
                    layoutParams.height = mOriginHeight;
                    layoutParams.leftMargin = mOriginLeft;
                    layoutParams.topMargin = mOriginTop;
                    contentLayout.setLayoutParams(layoutParams);


                }
            });
            valueAnimator.setDuration(animationDuration).start();
        }
        changeBackgroundViewAlpha(mAlpha, 0, true);
    }

    /**
     * @param startAlpha
     * @param endAlpha
     * @param isFinish   动画结束后是否关闭
     */
    private void changeBackgroundViewAlpha(float startAlpha, float endAlpha, boolean isFinish) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                isAnimating = true;
                mAlpha = (Float) valueAnimator.getAnimatedValue();
                backgroundView.setAlpha(mAlpha);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                if (isFinish) {
                    setVisibility(View.GONE);
                    if (onFinishListener != null) {
                        onFinishListener.callFinish();
                    }
                }
            }
        });
        valueAnimator.setDuration(animationDuration).start();
    }

    private OnFinishListener onFinishListener;
    private OnDragListener mDragListener;
    private OnShowFinishListener onShowFinishListener;
    private OnClickListener onClickListener;
    private OnReleaseListener onReleaseListener;

    public void setOnReleaseListener(OnReleaseListener onReleaseListener) {
        this.onReleaseListener = onReleaseListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnShowFinishListener(OnShowFinishListener onShowFinishListener) {
        this.onShowFinishListener = onShowFinishListener;
    }

    public void setOnDragListener(OnDragListener listener) {
        mDragListener = listener;
    }


    public interface OnDragListener {
        void onDrag(DragView view, float moveX, float moveY);
    }

    public interface OnShowFinishListener {
        void showFinish(DragView dragDiootoView, boolean showImmediately);
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
        void callFinish();
    }

    public interface OnReleaseListener {
        void onRelease(boolean isToMax, boolean isToMin);
    }

    public interface OnClickListener {
        void onClick(DragView dragDiootoView);
    }


    //获得可滑动view的布局中添加的子view
    public View getContentView() {
        return contentLayout.getChildAt(0);
    }

    public ViewGroup getContentParentView() {
        return contentLayout;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }
}
