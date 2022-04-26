package com.android.imagewatcher;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.imagewatcher.loading.LoadingView;
import com.android.imagewatcher.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.android.imagewatcher.Contents.IMAGE_MODEL;
import static com.android.imagewatcher.Contents.IMAGE_POSITION;
import static com.android.imagewatcher.Contents.IMAGE_URL;
import static com.android.imagewatcher.Contents.SHOW_ANIMATION;

public class ImageWatcherFragment extends Fragment {

    private ImageWatcherView dragView;
    private ContentViewOriginModel contentViewOriginModel;
    private String url;
    private int position;
    private boolean shouldShowAnimation = false;
    private PhotoView mPhotoView;
    private LoadingView loadingView;

    static ImageWatcherFragment newInstance(String url, int position, boolean showAnimation, ContentViewOriginModel contentViewOriginModel) {
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        args.putInt(IMAGE_POSITION, position);
        args.putBoolean(SHOW_ANIMATION, showAnimation);
        args.putParcelable(IMAGE_MODEL, contentViewOriginModel);
        ImageWatcherFragment fragment = new ImageWatcherFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dragView = new ImageWatcherView(container.getContext());
        mPhotoView = new PhotoView(container.getContext());
        dragView.addContentChildView(mPhotoView);
        loadingView = new LoadingView(container.getContext());
        int dp25 = Utils.dip2px(container.getContext(), 35f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dp25, dp25);
        layoutParams.gravity = Gravity.CENTER;
        dragView.addView(loadingView, layoutParams);
        return dragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(IMAGE_URL);
            position = getArguments().getInt(IMAGE_POSITION);
            shouldShowAnimation = getArguments().getBoolean(SHOW_ANIMATION);
            contentViewOriginModel = getArguments().getParcelable(IMAGE_MODEL);
        }

        dragView.setOnFinishListener(new ImageWatcherView.OnFinishListener() {
            @Override
            public void callFinish() {
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                }
            }
        });
        loadingView.setVisibility(View.VISIBLE);
        loadingView.start();
        Glide.with(mPhotoView).load(url).placeholder(R.drawable.default_circle_loading).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Drawable drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.default_circle_loading));
                show(drawable);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable drawable, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                show(drawable);
                return false;
            }

            private void show(Drawable drawable) {
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.stop();
                            loadingView.setVisibility(View.GONE);
                            int realWidth = drawable.getIntrinsicWidth();
                            int realHeight = drawable.getIntrinsicHeight();
                            int left = contentViewOriginModel.getLeft();
                            int top = contentViewOriginModel.getTop();
                            int width = contentViewOriginModel.getWidth();
                            int height = contentViewOriginModel.getHeight();
                            mPhotoView.setImageDrawable(drawable);
                            dragView.putData(left, top, width, height, realWidth, realHeight);
                            dragView.show(shouldShowAnimation);
                        }
                    });
                }
            }

        }).submit();

    }

    void onBackPress() {
        dragView.onBackPress();
    }
}
