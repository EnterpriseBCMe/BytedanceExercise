package com.example.exercise2.widget;

public interface MyVideoController {
    void onPlay();
    void onPause();
    void onStop();
    void onFullScreen();
    void onExitFullScreen();
    void onSeek(float i);
    void onStopUpdateProgress();
    void onResumeUpdateProgress();
}
