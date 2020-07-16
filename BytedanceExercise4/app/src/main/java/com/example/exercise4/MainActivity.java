package com.example.exercise4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView editTextTime;
    ClockView clockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTime=findViewById(R.id.editTextTime);
        clockView=findViewById(R.id.clockView);
        clockView.setOnTimeChangeListener(new MyInterface() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTimeChanged(Bundle timeBundle) {
                editTextTime.setText(String.format("%02d", timeBundle.getInt("hour"))+":"+
                        String.format("%02d", timeBundle.getInt("minute"))+":"+
                        String.format("%02d", timeBundle.getInt("second")));
            }
        });
    }
}