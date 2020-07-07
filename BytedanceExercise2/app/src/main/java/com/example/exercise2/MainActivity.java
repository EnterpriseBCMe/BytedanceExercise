package com.example.exercise2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPagerAdapter fAdapter;
    List<Fragment> list_fragment;
    List<String> list_title;
    OrientationEventListener myOrientoinListener;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mContext=this;
        initTabs();
        myOrientoinListener = new MyOrientoinListener(this);
        myOrientoinListener.enable();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时取消监听
        myOrientoinListener.disable();
    }
    private void initTabs(){
        fAdapter = new MyAdapter(getSupportFragmentManager());
        //页面，数据源
        list_fragment = new ArrayList<>();
        list_fragment.add(new PlaceHolder());
        list_fragment.add(new PlaceHolder());
        list_fragment.add(new PlaceHolder());
        list_fragment.add(new MessageFragment());
        list_fragment.add(new PlaceHolder());
        list_title = new ArrayList<>();
        list_title.add("首页");
        list_title.add("关注");
        list_title.add("+");
        list_title.add("消息");
        list_title.add("我");
        //ViewPager的适配器
        fAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fAdapter);
        //绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable;
            String title = " ";
            if(position!=2){
                title=list_title.get(position);
            }
            SpannableString spannableString = new SpannableString(title);
            if(position==2)
            {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.middle);
                drawable.setBounds(0, 0, 140, 90);
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableString;
        }
    }
    class MyOrientoinListener extends OrientationEventListener {
        String TAG="OrientoinListener";
        public MyOrientoinListener(Context context) {
            super(context);
        }

        public MyOrientoinListener(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            Log.d(TAG, "orention" + orientation);
            int screenOrientation = getResources().getConfiguration().orientation;
            if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    Log.d(TAG, "设置竖屏");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            } else if (orientation > 225 && orientation < 315) { //设置横屏
                Log.d(TAG, "设置横屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            } else if (orientation > 45 && orientation < 135) {// 设置反向横屏
                Log.d(TAG, "反向横屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
            } else if (orientation > 135 && orientation < 225) {
                Log.d(TAG, "反向竖屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                }
            }
        }
    }
}
