package com.example.MiniTiktok.Player;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.Toast;


import com.example.MiniTiktok.R;
import com.example.MiniTiktok.widget.AndroidMediaController;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class PlayerActivity extends AppCompatActivity {
    private String TAG = "MyPlayer";
    AVLoadingIndicatorView avi;
    MyPlayer player;
    AndroidMediaController mController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        player = findViewById(R.id.video1);
        avi=findViewById(R.id.avi4);
        avi.bringToFront();
        player.setVideoListener(new MyVideoListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {

            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.d(TAG,"prepared");
                avi.hide();
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

            }
        });
        MyOrientoinListener myOrientoinListener = new MyOrientoinListener(this);
        myOrientoinListener.enable();
        mController = new AndroidMediaController(this,false);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            mController.setSupportActionBar(actionBar);
        else
            Log.d(TAG,"no actionbar");
        mController.setMediaPlayer(player);
        player.setMediaController(mController);
        Log.d(TAG,intent.getStringExtra("uri"));
        player.setPath(intent.getStringExtra("uri"));
        try {
            player.load();
        } catch (IOException e) {
            Toast.makeText(this,"播放失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        player.start();
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