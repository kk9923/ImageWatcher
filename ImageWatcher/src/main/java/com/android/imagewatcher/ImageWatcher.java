package com.android.imagewatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.imagewatcher.utils.Fucking;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import static com.android.imagewatcher.Contents.IMAGE_CONTENTVIEW_ORIGIN_MODEL;
import static com.android.imagewatcher.Contents.IMAGE_LIST;
import static com.android.imagewatcher.Contents.IMAGE_POSITION;


public class ImageWatcher extends AppCompatActivity {

    public static final String TAG = "ImageWatcher";

    //    private IIndicator iIndicator;

    private ViewPagerFixed mViewPager;

    private List<ImageWatcherFragment> fragmentList;


    public static void show(Activity activity, List<ImageView> views, ArrayList<String> imageList) {
        show(activity, views, imageList, 0);
    }

    public static void show(Activity activity, List<ImageView> views, ArrayList<String> imageList, int initPosition) {
        if (views == null || imageList == null) {
            return;
        }
        if (views.size() != imageList.size()) {
            return;
        }
        if (initPosition < 0) {
            return;
        }
        int[] location = new int[2];
        ArrayList<ContentViewOriginModel> originModels = new ArrayList<ContentViewOriginModel>();
        for (int i = 0; i < views.size(); i++) {
            ContentViewOriginModel imageBean = new ContentViewOriginModel();
            ImageView imageView = views.get(i);
            if (imageView == null) {
                imageBean.left = 0;
                imageBean.top = 0;
                imageBean.width = 0;
                imageBean.height = 0;
            } else {
                imageView.getLocationOnScreen(location);
                imageBean.left = location[0];
                imageBean.top = location[1] - Fucking.getFuckHeight(imageView.getContext());
                imageBean.width = imageView.getWidth();
                imageBean.height = imageView.getHeight();
            }
            originModels.add(imageBean);
        }
        Intent intent = new Intent(activity, ImageWatcher.class);
        intent.putExtra(IMAGE_LIST, imageList);
        intent.putExtra(IMAGE_POSITION, initPosition);
        intent.putExtra(IMAGE_CONTENTVIEW_ORIGIN_MODEL, originModels);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ViewGroup content = findViewById(android.R.id.content);

        View decorView = getWindow().getDecorView();
        decorView.post(() -> {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        });

        ArrayList<String> imageList = getIntent().getStringArrayListExtra(IMAGE_LIST);

        ArrayList<ContentViewOriginModel> originModels = getIntent().getParcelableArrayListExtra(IMAGE_CONTENTVIEW_ORIGIN_MODEL);
        int initPosition = getIntent().getIntExtra(IMAGE_POSITION, 0);

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