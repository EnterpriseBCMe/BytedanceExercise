package com.example.exercise2.VideoPoster;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.exercise2.MainActivity;
import com.example.exercise2.R;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VideoPosterFragment extends Fragment {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private static final String TAG = "VideoPoster";
    private static final int PICK_IMAGE = 1001;
    private static final int PICK_VIDEO = 1002;
    Button postIt;
    View mView;
    private Uri mSelectedImage;
    private Uri mSelectedVideo;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MyPosterService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private MyPosterService mService = retrofit.create(MyPosterService.class);
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

    }
    private void initBtns() {
        postIt = mView.findViewById(R.id.postIt);
        postIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        //mBtnRefresh = findViewById(R.id.btn_refresh);
    }

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_video_poster, container, false);
        initBtns();
        return mView;
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
        verifyStoragePermissions(getActivity());
        File f = new File(ResourceUtils.getRealPath(getActivity(), uri));
        Log.d(TAG,"f name "+f.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }
}