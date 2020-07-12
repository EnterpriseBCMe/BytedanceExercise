package com.example.exercise2;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exercise2.widget.AndroidMediaController;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Objects;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerFragment extends Fragment{
    private String TAG = "MyPlayer";
    AVLoadingIndicatorView avi;
    Context mContext;
    MyPlayer player;
    Uri uri=null;
    AndroidMediaController mController;
    @SuppressLint("SdCardPath")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_video_player, container, false);
        super.onCreate(savedInstanceState);
        player = view.findViewById(R.id.video);
        avi=view.findViewById(R.id.avi);
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
        mController = new AndroidMediaController(getActivity(),false);
        ActionBar actionBar=((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        if(actionBar!=null)
            mController.setSupportActionBar(actionBar);
        else
            Log.d(TAG,"no actionbar");
        mController.setMediaPlayer(player);
        player.setMediaController(mController);
        player.setPath("http://39.96.72.171/src/1.mp4");
        try {
            player.load();
        } catch (IOException e) {
            Toast.makeText(getActivity(),"播放失败",Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        player.start();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

}