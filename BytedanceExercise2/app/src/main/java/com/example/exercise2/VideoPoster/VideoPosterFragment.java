package com.example.exercise2.VideoPoster;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.VideoCapture;
import androidx.camera.view.CameraView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.example.exercise2.R;

import java.io.File;
import java.lang.reflect.Parameter;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VideoPosterFragment extends Fragment {

    private static final int REQUEST_VIDEOPOSTER = 10086;
    private static String[] permissionArray = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private boolean checkPermissionAllGranted() {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissionArray) {
            if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    public void verifyPermissions(Activity activity) {
        if (!checkPermissionAllGranted()) {
            ActivityCompat.requestPermissions(activity, permissionArray, REQUEST_VIDEOPOSTER);
        }
    }

    private static final String TAG = "VideoPoster";
    private static final int PICK_IMAGE = 1001;
    private static final int PICK_VIDEO = 1002;
    private View mView;
    private CameraView mCamera;
    private ImageView takePhoto;
    private ImageView changeMod;
    private ImageView toggleLens;
    private ImageView flashMod;
    private ImageView uploadCloud;
    private ImageView timerSet;
    private TextView timerText;
    private Uri mSelectedImage;
    private Uri mSelectedVideo;
    private String outputFilePath;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MyPosterService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private MyPosterService mService = retrofit.create(MyPosterService.class);
    private static boolean CameraBinded = false;
    private int delay=0;

    public VideoPosterFragment() {
        // Required empty public constructor
    }

    public static VideoPosterFragment newInstance() {
        VideoPosterFragment fragment = new VideoPosterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "poster frag created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video_poster, container, false);
        /*
        mCamera=new MyCamera(getContext());
        container.addView(mCamera);


        ConstraintSet set =new ConstraintSet();
        set.connect(mCamera.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);
        set.connect(mCamera.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        set.connect(mCamera.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);
        set.connect(mCamera.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);
        set.applyTo(mView.findViewById(R.id.vf_cl));*/

        mCamera = mView.findViewById(R.id.vf_camera);
        mCamera.setPinchToZoomEnabled(true);
        mCamera.setFlash(ImageCapture.FLASH_MODE_AUTO);

        verifyPermissions(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            verifyPermissions(getActivity());
        }
        try {
            mCamera.bindToLifecycle((LifecycleOwner) getContext());
        }catch (IllegalArgumentException|IllegalStateException e){
            e.printStackTrace();
        }
        Log.d(TAG,String.valueOf(mCamera.getCameraLensFacing()));
        initBtns();
        return mView;
    }

    private void initBtns() {
        flashMod = mView.findViewById(R.id.vf_flash);
        flashMod.setOnClickListener(view -> {
            if(mCamera.getFlash()==ImageCapture.FLASH_MODE_AUTO){
                mCamera.setFlash(ImageCapture.FLASH_MODE_ON);
                flashMod.setImageResource(R.drawable.flash_on);
            }
            else if(mCamera.getFlash()==ImageCapture.FLASH_MODE_ON){
                mCamera.setFlash(ImageCapture.FLASH_MODE_OFF);
                flashMod.setImageResource(R.drawable.flash_off);
            }
            else if(mCamera.getFlash()==ImageCapture.FLASH_MODE_OFF){
                mCamera.setFlash(ImageCapture.FLASH_MODE_AUTO);
                flashMod.setImageResource(R.drawable.flash_auto);
            }
        });
        uploadCloud = mView.findViewById(R.id.vf_upload);
        timerSet = mView.findViewById(R.id.vf_timerset);
        timerText = mView.findViewById(R.id.vf_timertext);
        timerSet.setOnClickListener(view -> {
            if(delay==0){
                delay=3;
                timerSet.setImageResource(R.drawable.timer_3);
            }
            else if(delay==3){
                delay=5;
                timerSet.setImageResource(R.drawable.timer_5);
            }
            else if(delay==5){
                delay=10;
                timerSet.setImageResource(R.drawable.timer_10);
            }
            else if(delay==10){
                delay=0;
                timerSet.setImageResource(R.drawable.timer_0);
            }
        });
        //TODO 完成图片上传
        /*
        uploadCloud.setOnClickListener(v -> {
            String s = postIt.getText().toString();
            if (getString(R.string.selectImage).equals(s)) {
                chooseImage();
            } else if (getString(R.string.selectVideo).equals(s)) {
                chooseVideo();
            } else if (getString(R.string.postIt).equals(s)) {
                if (mSelectedVideo != null && mSelectedImage != null) {
                    postVideo();
                } else {
                    throw new IllegalArgumentException("error data uri, mSelectedVideo = "
                            + mSelectedVideo
                            + ", mSelectedImage = "
                            + mSelectedImage);
                }
            } else if ((getString(R.string.postSuccess).equals(s))) {
                postIt.setText(R.string.selectImage);
            }
        });

         */
        takePhoto = mView.findViewById(R.id.vf_takephoto);
        takePhoto.setOnClickListener(takePhotoListener);
        changeMod = mView.findViewById(R.id.vf_changemod);
        changeMod.setOnClickListener(view -> {
            if(mCamera.getCaptureMode()==CameraView.CaptureMode.IMAGE){
                mCamera.setCaptureMode(CameraView.CaptureMode.VIDEO);
                takePhoto.setOnClickListener(takeVideoListener);
                takePhoto.setImageResource(R.drawable.takevideo);
                changeMod.setImageResource(R.drawable.takephoto);
            }
            else if(mCamera.getCaptureMode()==CameraView.CaptureMode.VIDEO){
                mCamera.setCaptureMode(CameraView.CaptureMode.IMAGE);
                takePhoto.setOnClickListener(takePhotoListener);
                takePhoto.setImageResource(R.drawable.takephotoselector);
                changeMod.setImageResource(R.drawable.takevideo);
            }
        });
        toggleLens = mView.findViewById(R.id.vf_togglelens);
        toggleLens.setOnClickListener(view -> {
            mCamera.toggleCamera();
        });
        timerSet.bringToFront();
        timerText.bringToFront();
        flashMod.bringToFront();
        uploadCloud.bringToFront();
        takePhoto.bringToFront();
        changeMod.bringToFront();
        toggleLens.bringToFront();
    }
    private View.OnClickListener takePhotoListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(delay>0){
                //TODO RXJAVA
                new Thread(() -> {
                    for(int i=delay;i>0;i--){
                        Log.d(TAG,String.valueOf(i));
                        int finalI = i;
                        getActivity().runOnUiThread(() -> timerText.setText(String.valueOf(finalI)));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    getActivity().runOnUiThread(() -> timerText.setText(""));
                    getActivity().runOnUiThread(() -> takePhoto());
                }).start();
            }
            else
                takePhoto();
        }
    };
    private void takePhoto(){
        File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                System.currentTimeMillis() + ".jpeg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        mCamera.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = outputFileResults.getSavedUri();
                if(savedUri == null){
                    savedUri = Uri.fromFile(file);
                }
                outputFilePath = file.getAbsolutePath();
                onFileSaved(savedUri);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Photo capture failed: "+exception.getMessage(), exception);
            }
        });
    }
    private View.OnClickListener takeVideoListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(!mCamera.isRecording()){
                takePhoto.setImageResource(R.drawable.takevideo1);
                File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                        System.currentTimeMillis() + ".mp4");
                mCamera.startRecording(file, Executors.newSingleThreadExecutor(), new VideoCapture.OnVideoSavedCallback() {
                    @Override
                    public void onVideoSaved(@NonNull File file) {
                        outputFilePath = file.getAbsolutePath();
                        onFileSaved(Uri.fromFile(file));
                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                        Log.i(TAG,message);
                    }
                });
            }
            else {
                mCamera.stopRecording();
                takePhoto.setImageResource(R.drawable.takevideo);
            }
        }
    };
    private void onFileSaved(Uri savedUri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            getActivity().sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri));
        }
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(savedUri.getPath()));
        MediaScannerConnection.scanFile(getContext(),
                new String[]{new File(savedUri.getPath()).getAbsolutePath()},
                new String[]{mimeTypeFromExtension}, (MediaScannerConnection.OnScanCompletedListener) (path, uri) ->
                        Log.d(TAG, "Image capture scanned into media store: $uri"+uri));
    }
    //TODO 完成图片上传
/*
    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (resultCode == Activity.RESULT_OK && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                postIt.setText(R.string.selectVideo);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
                postIt.setText(R.string.postIt);
            }
        }
    }

    private void postVideo() {

        postIt.setText("POSTING...");
        postIt.setEnabled(false);
        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);
        mService.postVideo("Entp", "syh", coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                        postIt.setText(R.string.selectImage);
                        postIt.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        postIt.setText(R.string.selectImage);
                        postIt.setEnabled(true);
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        Log.d(TAG, "RealPath "+ResourceUtils.getRealPath(getActivity(), uri));
        verifyPermissions(getActivity());
        File f = new File(ResourceUtils.getRealPath(getActivity(), uri));
        Log.d(TAG,"f name "+f.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

 */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CameraBinded=false;
    }
}