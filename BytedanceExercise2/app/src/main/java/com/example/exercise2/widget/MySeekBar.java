package com.example.exercise2.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.exercise2.R;

public class MySeekBar extends FrameLayout {
    SeekBar mSeekBar;
    Button mPlay,mFull;
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
        mFull = view.findViewById(R.id.sb_fs);
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
                float progress = (float)mSeekBar.getProgress()/(float)mSeekBar.getMax();
                mController.onSeek(progress);
                mController.onResumeUpdateProgress();
            }
        });
        mPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlay.getText().equals("play")){
                    mController.onPlay();
                    mPlay.setText("pause");
                }
                else if(mPlay.getText().equals("pause")){
                    mController.onPause();
                    mPlay.setText("play");
                }
            }
        });
        mFull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFull.getText().equals("full")){
                    mController.onFullScreen();
                    mFull.setText("exit");
                }
                else if(mFull.getText().equals("exit")){
                    mController.onExitFullScreen();
                    mFull.setText("full");
                }
            }
        });
    }

    public void setVideoController(MyVideoController controller){
        mController=controller;
    }

    public void show(){
        setVisibility(VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setVisibility(INVISIBLE);
            }
        }).start();
    }
    @SuppressLint("SetTextI18n")
    public void setTime(long pos, long dur){
        if(dur==0){
            mTime.setText("00:00");
            mSeekBar.setProgress(0);
            return;
        }
        mTime.setText(praseTime(pos)+'/'+praseTime(dur));
       // Log.d("my seekbar",String.valueOf((int)(((float)pos/(float)dur)*(float)mSeekBar.getMax())));
        mSeekBar.setProgress((int)(((float)pos/(float)dur)*(float)mSeekBar.getMax()));
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
