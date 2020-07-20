package com.example.MiniTiktok;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ClockFragment extends Fragment {

    ClockView clockView;
    TextView editTextTime;
    public static ClockFragment newInstance(String param1, String param2) {
        ClockFragment fragment = new ClockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_clock, container, false);
        editTextTime=view.findViewById(R.id.editTextTime);
        clockView=view.findViewById(R.id.clockView);
        clockView.setOnTimeChangeListener(new MyInterface() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTimeChanged(Bundle timeBundle) {
                editTextTime.setText(String.format("%02d", timeBundle.getInt("hour"))+":"+
                        String.format("%02d", timeBundle.getInt("minute"))+":"+
                        String.format("%02d", timeBundle.getInt("second")));
            }
        });
        return view;
    }

}
