package com.example.exercise2.VideoPoster;

import com.google.gson.annotations.SerializedName;

public class VideoInfo {
    @SerializedName("student_id") public String studentId;
    @SerializedName("user_name") public String userName;
    @SerializedName("image_url") public String imageUrl;
    @SerializedName("video_url") public String videoUrl;
    @SerializedName("image_w") public int imageWidth;
    @SerializedName("image_h") public int imageHeight;
}