package com.android.imageviewer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.imageviewer.databinding.ActivityImageViewerBinding;
import com.android.imageviewer.interfaces.CircleIndexIndicator;
import com.android.imageviewer.interfaces.IIndicator;
import com.android.imageviewer.utils.Fucking;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;


public class ImageViewerUi extends AppCompatActivity {

    public static final String TAG = "ImageViewerActivity";

    private static final String IMAGE_LIST = "IMAGE_LIST";

    private static final String IMAGE_POSITION = "IMAGE_POSITION";

    private static final String IMAGE_CONTENTVIEW_ORIGIN_MODEL = "IMAGE_CONTENTVIEW_ORIGIN_MODEL";

    private ActivityImageViewerBinding activityImageViewerBinding;
    //    private IIndicator iIndicator;
    private ImagePagerAdapter imagePagerAdapter;


    private ActivityImageViewerBinding getViewBinding() {
        return activityImageViewerBinding;
    }

    public static void show(Context context, View sharedElement, ArrayList<String> imageList, int position, ArrayList<ContentViewOriginModel> originModels) {
        Window window = Fucking.getWindow(context);
        boolean isFullScreen = false;
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            isFullScreen = true;
        }
        if (!isFullScreen) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        }

        Intent intent = new Intent(context, ImageViewerUi.class);
        intent.putExtra(IMAGE_LIST, imageList);
        intent.putExtra(IMAGE_POSITION, position);
        intent.putExtra(IMAGE_CONTENTVIEW_ORIGIN_MODEL, originModels);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);

    }

    private ArrayList<ContentViewOriginModel> originModels;
    private int initPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
        if (ImmersionBar.hasNavigationBar(this)) {
            BarParams barParams = ImmersionBar.with(this).getBarParams();
            if (barParams.fullScreen) {
                ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).init();
            } else {
                ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().init();
            }
        } else {
            Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
        }
        activityImageViewerBinding = ActivityImageViewerBinding.inflate(getLayoutInflater());
        setContentView(activityImageViewerBinding.getRoot());

        ArrayList<String> imageList = getIntent().getStringArrayListExtra(IMAGE_LIST);

        if (imageList == null || imageList.size() == 0) {
            return;
        }
        originModels = getIntent().getParcelableArrayListExtra(IMAGE_CONTENTVIEW_ORIGIN_MODEL);
        initPosition = getIntent().getIntExtra(IMAGE_POSITION, -1);
        if (initPosition < 0) {
            throw new IllegalArgumentException("initPosition must be > 0 !");
        }

        imagePagerAdapter = new ImagePagerAdapter(imageList);

        getViewBinding().viewPager.setAdapter(imagePagerAdapter);
        getViewBinding().viewPager.setCurrentItem(initPosition);

//        iIndicator = new CircleIndexIndicator();
//        iIndicator.attach(getViewBinding().indicatorLayout);
//        iIndicator.onShow(getViewBinding().viewPager);
    }

    class ImagePagerAdapter extends PagerAdapter {

        private final ArrayList<String> imageList;

        private final FrameLayout.LayoutParams lpCenter = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        private final SparseArray<DragDiootoView> mImageSparseArray = new SparseArray<>();

        public void onBackPress() {
            mImageSparseArray.get(getViewBinding().viewPager.getCurrentItem()).backToMin();
        }

        private boolean hasPlayBeginAnimation;

        public ImagePagerAdapter(ArrayList<String> imageList) {
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            return imageList != null ? imageList.size() : 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mImageSparseArray.remove(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = View.inflate(container.getContext(), R.layout.fragment_image, null);
            DragDiootoView dragDiootoView = itemView.findViewById(R.id.dragDiootoView);
            ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dragDiootoView.setLayoutParams(layoutParams);
//            ImageView imageView = new ImageView(container.getContext());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                imageView.setTransitionName(ImageViewerUi.TAG + ":" + position);
//            }
            PhotoView photoView = new PhotoView(container.getContext());
            ViewGroup.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams2);

//            sketchImageView.setZoomEnabled(true);
            dragDiootoView.addContentChildView(photoView);
//            sketchImageView.getZoomer().getBlockDisplayer().setPause(!isVisibleToUser());

            mImageSparseArray.put(position, dragDiootoView);

            ContentViewOriginModel contentViewOriginModel = originModels.get(position);

            boolean hasCache;
//            DiskCache diskCache = Sketch.with(container.getContext()).getConfiguration().getDiskCache();
//            hasCache = diskCache.exist(imageList.get(position));
            hasCache = false;
            if (hasCache) {
                Glide.with(photoView).load(imageList.get(position)).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        photoView.setImageDrawable(drawable);
                        int w = drawable.getIntrinsicWidth();
                        int h = drawable.getIntrinsicHeight();
                        //如果有缓存  直接将大小变为最终大小而不是去调用notifySize来更新 并且是直接显示不进行动画
                        dragDiootoView.putData(
                                contentViewOriginModel.getLeft(), contentViewOriginModel.getTop(),
                                contentViewOriginModel.getWidth(), contentViewOriginModel.getHeight(),
                                w, h);
                        dragDiootoView.show(true);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            } else {
                dragDiootoView.putData(contentViewOriginModel.getLeft(), contentViewOriginModel.getTop(), contentViewOriginModel.getWidth(), contentViewOriginModel.getHeight());
                //如果显示的点击的position  则进行动画处理
                Glide.with(photoView).load(imageList.get(position)).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        photoView.setImageDrawable(drawable);
                        int w = drawable.getIntrinsicWidth();
                        int h = drawable.getIntrinsicHeight();
                        dragDiootoView.show(initPosition != position);
                        dragDiootoView.notifySize(w, h);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
//            dragDiootoView.setOnDragListener(new DragDiootoView.OnDragListener() {
//                @Override
//                public void onDrag(DragDiootoView view, float moveX, float moveY) {
//                    iIndicator.move(moveX, moveY);
//                }
//            });
//            dragDiootoView.setOnReleaseListener(new DragDiootoView.OnReleaseListener() {
//                @Override
//                public void onRelease(boolean isToMax, boolean isToMin) {
//                    iIndicator.fingerRelease(isToMax, isToMin);
//                }
//            });
            dragDiootoView.setOnFinishListener(new DragDiootoView.OnFinishListener() {
                @Override
                public void callFinish() {
                    finish();
                    overridePendingTransition(0, 0);
                }
            });

            container.addView(itemView);
            return itemView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            imagePagerAdapter.onBackPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}