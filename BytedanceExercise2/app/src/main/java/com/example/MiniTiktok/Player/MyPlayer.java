package com.example.MiniTiktok.Player;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.MiniTiktok.widget.MySeekBar;
import com.example.MiniTiktok.widget.MyVideoController;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyPlayer extends FrameLayout implements MediaController.MediaPlayerControl{
    private String TAG="MyPlayer";
    /**
     * 由ijkplayer提供，用于播放视频，需要给他传入一个surfaceView
     */
    private IMediaPlayer mMediaPlayer = null;
    /**
     * 视频文件地址
     */
    private String mPath ;
    /**
     * 视频请求header
     */
    private Map<String,String> mHeader;

    private SurfaceView mSurfaceView;

    private Context mContext;
    private boolean mEnableMediaCodec=true;
    private MySeekBar mSeekBar;
    private MyVideoListener mListener;
    private AudioManager mAudioManager;
    private AudioFocusHelper mAudioFocusHelper;
    private IMediaController mMediaController;
    private int MediaHeight,MediaWidth;
    private int LayoutHeight,LayoutWidth;

    public MyPlayer(@NonNull Context context) {
        this(context, null);
    }

    public MyPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //初始化
    private void init(Context context) {
        mContext = context;
        setBackgroundColor(Color.BLACK);
        createSurfaceView();
        setVideoController();
        mAudioManager = (AudioManager)mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusHelper = new AudioFocusHelper();
    }

    //创建surfaceView
    private void createSurfaceView() {
        mSeekBar = new MySeekBar(mContext);
        mSurfaceView = new SurfaceView(mContext);
        mSeekBar.setVisibility(INVISIBLE);
        mSurfaceView.setOnTouchListener((view, motionEvent) -> {
            mSeekBar.show();
            return false;
        });
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(surfaceHolder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        LayoutParams surfaceViewParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        LayoutParams seekBarViewParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(mSurfaceView,0,surfaceViewParams);
        addView(mSeekBar,1,seekBarViewParams);
        mSeekBar.bringToFront();
    }


    //创建一个新的player
    private IMediaPlayer createPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        ijkMediaPlayer.setVolume(1.0f, 1.0f);
        setEnableMediaCodec(ijkMediaPlayer,mEnableMediaCodec);
        return ijkMediaPlayer;
    }

    //设置是否开启硬解码
    private void setEnableMediaCodec(IjkMediaPlayer ijkMediaPlayer, boolean isEnable) {
        int value = isEnable ? 1 : 0;
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);//开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value);
    }

    public void setEnableMediaCodec(boolean isEnable){
        mEnableMediaCodec = isEnable;
    }

    //设置ijkplayer的监听
    private void setListener(IMediaPlayer player){
        player.setOnPreparedListener(mPreparedListener);
    }

    public void setVideoController(){
        mSeekBar.setVideoController(new MyVideoController() {
            @Override
            public void onPlay() {
                start();
            }

            @Override
            public void onPause() {
                pause();
            }

            @Override
            public void onStop() {
                stop();
            }

            @Override
            public void onFullScreen() {

            }

            @Override
            public void onExitFullScreen() {

            }

            @Override
            public void onSeek(int i) {
                seekTo(i);
            }
            @Override
            public void onStopUpdateProgress() {

            }

            @Override
            public void onResumeUpdateProgress() {

            }
        });
    }

    /**
     * 设置自己的player回调
     */
    public void setVideoListener(MyVideoListener listener){
        mListener = listener;
    }

    //设置播放地址
    public void setPath(String path) {
        setPath(path,null);
    }

    public void setPath(String path,Map<String,String> header){
        mPath = path;
        mHeader = header;
    }

    //开始加载视频
    public void load() throws IOException {
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = createPlayer();
        setListener(mMediaPlayer);
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.setDataSource(mContext, Uri.parse(mPath),mHeader);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnVideoSizeChangedListener((iMediaPlayer, i, i1, i2, i3) -> {
            int w,h;
            MediaWidth=i;
            MediaHeight=i1;
            float LayoutRatio=(float) LayoutWidth /(float) LayoutHeight;
            float MediaRatio=(float) MediaWidth /(float) MediaHeight;
            if(LayoutRatio>MediaRatio){
                h=LayoutHeight;
                w=(int)(LayoutHeight*((float)MediaWidth /(float)MediaHeight));
            }
            else{
                w=LayoutWidth;
                h=(int)(LayoutWidth*((float)MediaHeight/(float)MediaWidth));
            }
            LayoutParams lp = new LayoutParams(w,h);
            lp.gravity=Gravity.CENTER;
            mSeekBar.setLayoutParams(lp);
            mSurfaceView.setLayoutParams(lp);
        });
        new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mSeekBar.post(() -> mSeekBar.setTime(mMediaPlayer.getCurrentPosition(),mMediaPlayer.getDuration()));
            }
        }).start();
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mAudioFocusHelper.requestFocus();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioFocusHelper.abandonFocus();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mAudioFocusHelper.abandonFocus();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mAudioFocusHelper.abandonFocus();
        }
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mAudioFocusHelper.abandonFocus();
        }
    }


    public int getDuration() {
        if (mMediaPlayer != null) {
            return (int)mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return (int)mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public void seekTo(int i) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(i);
        }
    }

    public boolean isPlaying(){
        if(mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newWidth=3*newConfig.screenWidthDp;
        int newHeight=3*newConfig.screenHeightDp;
        int w,h;
        float LayoutRatio=(float) newWidth /(float) newHeight;
        float MediaRatio=(float) MediaWidth /(float) MediaHeight;
        if(LayoutRatio>MediaRatio){
            h=newHeight;
            w=(int)(newHeight*((float)MediaWidth /(float)MediaHeight));
        }
        else{
            w=newWidth;
            h=(int)(newWidth*((float)MediaHeight/(float)MediaWidth));
        }
        LayoutParams svlp = new LayoutParams(w,h);
        LayoutParams sblp = new LayoutParams(w,h);
        //横屏
        if((newConfig.orientation > 225 && newConfig.orientation < 315)||(newConfig.orientation > 45 && newConfig.orientation < 135))
            sblp.width=newWidth;
        svlp.gravity=Gravity.CENTER;
        sblp.gravity=Gravity.CENTER;
        mSeekBar.setLayoutParams(sblp);
        mSurfaceView.setLayoutParams(svlp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutHeight=MeasureSpec.getSize(heightMeasureSpec);
        LayoutWidth=MeasureSpec.getSize(widthMeasureSpec);
    }
    public void setMediaController(IMediaController controller) {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ?
                    (View) this.getParent() : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(isPlaying());
        }
    }

    public void resize(){

    }

    //------------------  各种listener 赋值 ---------------------//

    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            if(mListener != null){
                mListener.onPrepared(iMediaPlayer);
            }
        }
    };
    private class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
        boolean startRequested = false;
        boolean pausedForLoss = false;
        int currentFocus = 0;

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (currentFocus == focusChange) {
                return;
            }

            currentFocus = focusChange;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN://获得焦点
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT://暂时获得焦点
                    if (startRequested || pausedForLoss) {
                        start();
                        startRequested = false;
                        pausedForLoss = false;
                    }
                    if (mMediaPlayer != null)//恢复音量
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS://焦点丢失
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://焦点暂时丢失
                    if (isPlaying()) {
                        pausedForLoss = true;
                        pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://此时需降低音量
                    if (mMediaPlayer != null && isPlaying()) {
                        mMediaPlayer.setVolume(0.1f, 0.1f);
                    }
                    break;
            }
        }

        boolean requestFocus() {
            if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
                return true;
            }

            if (mAudioManager == null) {
                return false;
            }

            int status = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
                currentFocus = AudioManager.AUDIOFOCUS_GAIN;
                return true;
            }

            startRequested = true;
            return false;
        }

        boolean abandonFocus() {

            if (mAudioManager == null) {
                return false;
            }

            startRequested = false;
            int status = mAudioManager.abandonAudioFocus(this);
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status;
        }
    }

}
