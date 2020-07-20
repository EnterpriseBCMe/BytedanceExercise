package com.example.MiniTiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;


public class TesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Uri uri = Uri.parse(bundle.getString("uri"));
        Log.d("111",uri.toString());
        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setImageURI(uri);
    }
}