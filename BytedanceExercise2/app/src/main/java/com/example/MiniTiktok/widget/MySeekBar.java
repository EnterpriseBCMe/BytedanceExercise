package com.example.MiniTiktok.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.MiniTiktok.R;

public class MySeekBar extends FrameLayout {
    SeekBar mSeekBar;
    ImageView mPlay,mFull;
    boolean playing=true,fullScreen=false;
    View view;
    MyVideoController mController;
    TextView mTime;
    public MySeekBar(@NonNull Context context)
    {
        this(context,null);
    }
    public MySeekBar(@NonNull Context context, AttributeSet attrs)
    {
        this(context,attrs,0);
    }
    public MySeekBar(@NonNull Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
        view = LayoutInflater.from(context).inflate(R.layout.my_seekbar,this);
        mSeekBar = view.findViewById(R.id.sb_seekbar);
        mPlay = view.findViewById(R.id.sb_play);
        mFull = view.findViewById(R.id.sb_full);
        mTime = view.findViewById(R.id.sb_time);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mController.onStopUpdateProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mController.onSeek(mSeekBar.getProgress());
                mController.onResumeUpdateProgress();
            }
        });
        mPlay.setOnClickListener(view -> {
            if(!playing){
                mController.onPlay();
                mPlay.setImageResource(R.drawable.pause);
                playing=true;
            }
            else if(playing){
                mController.onPause();
                mPlay.setImageResource(R.drawable.play);
                playing=false;
            }
        });
        mFull.setOnClickListener(view -> {
            if(!fullScreen){
                mController.onFullScreen();
                fullScreen=true;
            }
            else if(fullScreen){
                mController.onExitFullScreen();
                fullScreen=false;
            }
        });
    }

    public void setVideoController(MyVideoController controller){
        mController=controller;
    }

    public void show(){
        setVisibility(VISIBLE);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setVisibility(INVISIBLE);
        }).start();
    }
    @SuppressLint("SetTextI18n")
    public void setTime(long pos, long dur){
        if(dur==0){
            mTime.setText("00:00");
            mSeekBar.setProgress(0);
            return;
        }
        mSeekBar.setMax((int)dur);
        mTime.setText(praseTime(pos)+'/'+praseTime(dur));
        mSeekBar.setProgress((int)pos);
    }
    private static String praseTime(long seconds) {
        seconds=seconds/1000;
        int temp = 0;
        StringBuffer sb = new StringBuffer();
        if (seconds > 3600) {
            temp = (int) (seconds / 3600);
            sb.append((seconds / 3600) < 10 ? "0" + temp + ":" : temp + ":");
            temp = (int) (seconds % 3600 / 60);
            changeSeconds(seconds, temp, sb);
        } else {
            temp = (int) (seconds % 3600 / 60);
            changeSeconds(seconds, temp, sb);
        }
        return sb.toString();
    }

    private static void changeSeconds(long seconds, int temp, StringBuffer sb) {
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        temp = (int) (seconds % 3600 % 60);
        sb.append((temp < 10) ? "0" + temp : "" + temp);
    }
}
