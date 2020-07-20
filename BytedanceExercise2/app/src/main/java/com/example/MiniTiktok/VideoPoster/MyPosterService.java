package com.example.MiniTiktok.VideoPoster;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MyPosterService {
    String BASE_URL = "http://39.96.72.171/";

    @Multipart
    @POST("upload")
    Call<PostVideoResponse> postVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @GET("upload")
    Call<GetVideoResponse> getVideos(
            @Query("event") String event
    );
}
/*
public interface MyPosterService {
    String BASE_URL = "https://api.daliapp.net/android-bootcamp/invoke/";

    @Multipart
    @POST("video")
    Call<PostVideoResponse> postVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @GET("video")
    Call<GetVideoResponse> getVideos();
}*/
