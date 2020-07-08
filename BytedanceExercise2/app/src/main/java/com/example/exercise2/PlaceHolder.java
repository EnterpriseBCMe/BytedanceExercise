package com.example.exercise2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class PlaceHolder extends Fragment {

    LottieAnimationView lottieAnim;
    SeekBar lottieProgress;
    Animation alphaAnim,scaleAnim;
    AnimationSet animeSet;
    ImageView cat;
    Button replay;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_place_holder, container, false);
        cat=view.findViewById(R.id.catTemp);
        lottieAnim=view.findViewById(R.id.loadingTemp);
        lottieProgress=view.findViewById(R.id.animeSeekBar);
        lottieAnim.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int maxProgress=lottieProgress.getMax();
                float currentProgress=valueAnimator.getAnimatedFraction();
                lottieProgress.setProgress((int) (currentProgress*maxProgress));
            }
        });
        lottieProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean touched=false;
            void dragListener(final SeekBar seekBar) throws InterruptedException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(touched){
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            float progress=(float)seekBar.getProgress()/(float)seekBar.getMax();
                            Log.d("progress",String.valueOf(progress));
                            lottieAnim.setProgress(progress);
                        }
                    }
                }).start();
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lottieAnim.pauseAnimation();
                try {
                    touched=true;
                    dragListener(seekBar);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                touched=false;
                //lottieAnim.playAnimation();
            }
        });
        alphaAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
        animeSet = new AnimationSet(true);
        animeSet.addAnimation(alphaAnim);
        animeSet.addAnimation(scaleAnim);
        animeSet.setDuration(2000);
        animeSet.setStartOffset(500);
        cat.startAnimation(animeSet);
        replay=view.findViewById(R.id.replay);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottieAnim.playAnimation();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private int viewCount(View root){
        int viewCount = 0;
        if (null == root) {
            return 0;
        }
        if (root instanceof ViewGroup) {
            viewCount++;
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                View view = ((ViewGroup) root).getChildAt(i);
                if (view instanceof ViewGroup) {
                    viewCount += viewCount(view);
                } else {
                    viewCount++;
                }
            }
        }
        return viewCount;
    }
}