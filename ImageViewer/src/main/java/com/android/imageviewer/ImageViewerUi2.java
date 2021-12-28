package com.android.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.imageviewer.databinding.ActivityImageViewerBinding;
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
import java.util.List;


public class ImageViewerUi2 extends AppCompatActivity {

    public static final String TAG = "ImageViewerActivity";

    private static final String IMAGE_LIST = "IMAGE_LIST";

    private static final String IMAGE_POSITION = "IMAGE_POSITION";

    private static final String IMAGE_CONTENTVIEW_ORIGIN_MODEL = "IMAGE_CONTENTVIEW_ORIGIN_MODEL";

    //    private IIndicator iIndicator;

    private ViewPagerFixed mViewPager;

    private List<ImageWatcherFragment> fragmentList;

    public static void show(Context context, View sharedElement, ArrayList<String> imageList, int position, ArrayList<ContentViewOriginModel> originModels) {

        Intent intent = new Intent(context, ImageViewerUi2.class);
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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
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
//        fullScreen(this);
//        ImmersionBar.with(this).statusBarColor(R.color.black).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).fullScreen(true).transparentNavigationBar().init();
        setContentView(R.layout.activity_image_viewer);

        ArrayList<String> imageList = getIntent().getStringArrayListExtra(IMAGE_LIST);

        if (imageList == null || imageList.size() == 0) {
            return;
        }
        originModels = getIntent().getParcelableArrayListExtra(IMAGE_CONTENTVIEW_ORIGIN_MODEL);
        initPosition = getIntent().getIntExtra(IMAGE_POSITION, -1);
        if (initPosition < 0) {
            throw new IllegalArgumentException("initPosition must be > 0 !");
        }

        mViewPager = findViewById(R.id.viewPager);

        fragmentList = new ArrayList<>();
        for (int i = 0; i < originModels.size(); i++) {
            ImageWatcherFragment imageFragment = ImageWatcherFragment.newInstance(
                    imageList.get(i), i,
                    originModels.size() == 1 || initPosition == i, originModels.get(i)
            );
            fragmentList.add(imageFragment);
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
//        mViewPager.setOffscreenPageLimit(originModels.size());
        mViewPager.setCurrentItem(initPosition);

//        iIndicator = new CircleIndexIndicator();
//        iIndicator.attach(getViewBinding().indicatorLayout);
//        iIndicator.onShow(getViewBinding().viewPager);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            fragmentList.get(mViewPager.getCurrentItem()).onBackPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}