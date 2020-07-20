package com.example.MiniTiktok.widget;

public interface MyVideoController {
    void onPlay();
    void onPause();
    void onStop();
    void onFullScreen();
    void onExitFullScreen();
    void onSeek(int i);
    void onStopUpdateProgress();
    void onResumeUpdateProgress();
}
