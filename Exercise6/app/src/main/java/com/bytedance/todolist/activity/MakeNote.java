package com.bytedance.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bytedance.todolist.R;

public class MakeNote extends AppCompatActivity {

    private TextView mContent;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);
        mContent=findViewById(R.id.mn_content);
        confirm=findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("content", mContent.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}