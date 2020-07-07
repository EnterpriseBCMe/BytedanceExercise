package com.example.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MessageDetail extends AppCompatActivity {
    TextView ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ID=findViewById(R.id.ID);
        Intent intent = getIntent();
        ID.setText(intent.getStringExtra("ID"));
    }
}