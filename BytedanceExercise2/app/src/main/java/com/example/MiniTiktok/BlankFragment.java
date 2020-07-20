package com.example.MiniTiktok;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;


public class BlankFragment extends Fragment {

    private static final String TAG = "BlankFragment";
    Button button,button1;
    SimpleDraweeView draweeView;
    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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
        View view=inflater.inflate(R.layout.fragment_blank, container, false);
        button=view.findViewById(R.id.bf_bt);
        button1=view.findViewById(R.id.bf_er);
        final Uri uri = Uri.parse("http://39.96.72.171/picture/1594727944288.jpg");
        final Uri erruri = Uri.parse("http://39.96.72.171/picture/111.jpg");
        draweeView = view.findViewById(R.id.bf_fresco);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draweeView.setImageURI(uri);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draweeView.setImageURI(erruri);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}