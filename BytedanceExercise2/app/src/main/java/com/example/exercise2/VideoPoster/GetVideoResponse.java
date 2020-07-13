package com.example.exercise2.VideoPoster;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVideoResponse {

    @SerializedName("success") public boolean success;
    @SerializedName("feeds") public List<VideoInfo> videos;
    @SerializedName("error") public String error;

    @Override
    public String toString() {
        return "success=" + success +
                ", error=" + error +
                '}';
    }
}
