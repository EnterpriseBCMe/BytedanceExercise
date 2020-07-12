package com.example.exercise2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class TesterActivity extends AppCompatActivity {

    MyPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        player = findViewById(R.id.video1);
        player.setPath("http://39.96.72.171/src/1.mp4");
        try {
            player.load();
            Log.d("MyPlayer","loaded");
        } catch (IOException e) {
            Toast.makeText(this,"播放失败",Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        player.start();
        Log.d("MyPlayer","started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }
    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view=super.onCreateView(name, context, attrs);
        return view;
    }

 */


}