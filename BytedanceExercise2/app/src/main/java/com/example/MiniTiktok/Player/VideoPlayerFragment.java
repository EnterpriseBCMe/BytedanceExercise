package com.example.MiniTiktok.Player;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MiniTiktok.R;
import com.example.MiniTiktok.widget.AndroidMediaController;
import com.example.MiniTiktok.widget.FlowLikeView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Objects;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerFragment extends Fragment {
    private String TAG = "MyPlayer";
    AVLoadingIndicatorView avi;
    MyPlayer player;
    AndroidMediaController mController;
    ImageView like, comment, forward;
    private FlowLikeView likeViewLayout;
    boolean liked = false, commented = false, forwarded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        super.onCreate(savedInstanceState);
        likeViewLayout = view.findViewById(R.id.flowlikeview);
        likeViewLayout.setOnClickListener(view1 -> addLikeView(view1));
        like = view.findViewById(R.id.vp_like);
        comment = view.findViewById(R.id.vp_comment);
        forward = view.findViewById(R.id.vp_forward);
        TextView song = view.findViewById(R.id.vp_song);
        song.setSelected(true);
        like.setOnClickListener((View) -> {
            if (!liked) {
                like.setImageResource(R.drawable.liked);
                TextView likeCount = view.findViewById(R.id.vp_likecount);
                int count = Integer.parseInt(likeCount.getText().toString());
                likeCount.setText(String.valueOf(count + 1));
                liked = true;
            } else {
                like.setImageResource(R.drawable.like);
                TextView likeCount = view.findViewById(R.id.vp_likecount);
                int count = Integer.parseInt(likeCount.getText().toString());
                likeCount.setText(String.valueOf(count - 1));
                liked = false;
            }
        });
        player = view.findViewById(R.id.video);
        avi = view.findViewById(R.id.avi);
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
                Log.d(TAG, "prepared");
                avi.hide();
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

            }
        });
        mController = new AndroidMediaController(getActivity(), false);
        ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        if (actionBar != null)
            mController.setSupportActionBar(actionBar);
        else
            Log.d(TAG, "no actionbar");
        mController.setMediaPlayer(player);
        player.setMediaController(mController);
        player.setPath("http://39.96.72.171/video/1.mp4");
        try {
            player.load();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "播放失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        player.start();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            player.load();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "播放失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        player.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        player.stop();
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
    public void addLikeView(View view) {
        likeViewLayout.addLikeView();
    }
}