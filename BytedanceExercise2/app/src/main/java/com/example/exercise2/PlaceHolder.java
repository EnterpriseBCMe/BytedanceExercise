package com.example.exercise2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class PlaceHolder extends Fragment {

    TextView count;
    Button getCount;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_place_holder, container, false);
        count=view.findViewById(R.id.count);
        getCount=view.findViewById(R.id.getCount);
        getCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity mainAct=getActivity();
                View mainView=mainAct.getWindow().getDecorView();
                count.setText(String.valueOf(viewCount(mainView)));
            }
        });
        return view;
    }
    private int viewCount(View root){
        int viewCount = 0;
        if (null == root) {
            return 0;
        }
        if (root instanceof ViewGroup) {
            viewCount++;
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                View view = ((ViewGroup) root).getChildAt(i);
                if (view instanceof ViewGroup) {
                    viewCount += viewCount(view);
                } else {
                    viewCount++;
                }
            }
        }
        return viewCount;
    }
}