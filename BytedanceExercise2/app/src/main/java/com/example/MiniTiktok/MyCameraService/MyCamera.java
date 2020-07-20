package com.example.MiniTiktok.MyCameraService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.camera.core.Camera;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.CameraView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.MiniTiktok.MainActivity;
import com.example.MiniTiktok.R;
import com.google.common.util.concurrent.ListenableFuture;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class MyCamera extends ConstraintLayout {

    private static final String TAG = "MyCamera";
    private static final int REQUEST_PERMISSION = 10010;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraView mCameraView;
    private Camera camera;

    public MyCamera(Context context) {
        this(context, null);
    }

    public MyCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.my_camera, this);
        Log.d(TAG, "Camera created");
        initCamera();
    }
    private String[] mPermissionsArrays = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};
    void initCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((MainActivity)getContext(),mPermissionsArrays, REQUEST_PERMISSION);
            return;
        }
        mCameraView.bindToLifecycle((LifecycleOwner) getContext());
    }
    /*
    void initCamera(){
        cameraSurface = findViewById(R.id.preview_view);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        },ContextCompat.getMainExecutor(getContext()));
    }

     */
/*
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();
        int screenAspectRatio = AspectRatio.RATIO_16_9 ;
        int rotation = Surface.ROTATION_0;
        ImageCapture imageCapture = new ImageCapture.Builder()
                //优化捕获速度，可能降低图片质量
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                //设置宽高比
                .setTargetAspectRatio(screenAspectRatio)
                //设置初始的旋转角度
                .setTargetRotation(rotation)
                .build();

        @SuppressLint("RestrictedApi")
        VideoCapture videoCapture = new VideoCaptureConfig.Builder()
                //设置当前旋转
                .setTargetRotation(rotation)
                //设置宽高比
                .setTargetAspectRatio(screenAspectRatio)
                //分辨率
                //.setTargetResolution(resolution)
                //视频帧率  越高视频体积越大
                .setVideoFrameRate(30)
                //bit率  越大视频体积越大
                .setBitRate(3 * 1024 * 1024)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        cameraProvider.unbindAll();
        camera = cameraProvider.bindToLifecycle((LifecycleOwner)getContext(),cameraSelector, preview,imageCapture,videoCapture);
        preview.setSurfaceProvider(cameraSurface.createSurfaceProvider());//camera.getCameraInfo()
    }

 */



}
