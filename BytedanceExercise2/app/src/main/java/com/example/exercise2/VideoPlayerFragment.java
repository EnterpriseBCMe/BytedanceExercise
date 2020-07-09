package com.example.exercise2;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerFragment extends Fragment {

    Context mContext;
    VideoView player;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=getActivity();
        player=view.findViewById(R.id.mediaPlayer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class MyVideoView extends VideoView{
        private Context mContext;
        final int defaultHeight=200; //单位DP
        private int width,height;

        public MyVideoView(Context context) {
            super(context);
            mContext=context;
        }

        public MyVideoView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext=context;
        }

        public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mContext=context;
        }

        //widthMeasureSpec 和 heightMeasureSpec的值 由父容器决定
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super .onMeasure(widthMeasureSpec,heightMeasureSpec);
            // 默认高度，为了自动获取到focus
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width;
            // 这个之前是默认的拉伸图像
            if (this.width > 0 && this.height > 0) {
                width = this.width;
                height = this.height;
            }
            setMeasuredDimension(width, height);
        }
        
        public void setMeasure(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}