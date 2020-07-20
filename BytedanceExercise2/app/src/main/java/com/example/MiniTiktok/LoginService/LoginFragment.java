package com.example.MiniTiktok.LoginService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.MiniTiktok.R;

public class LoginFragment extends Fragment {
    private View mInputLayout;
    private Button mButton2Login;
    private TextView textView;
    private boolean logged=false;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        textView = root.findViewById(R.id.login_username);
        mButton2Login=root.findViewById(R.id.login_login);
        mButton2Login.setOnClickListener(view -> {
            if(!logged){
                Intent intent = new Intent(getContext(), LoginActivity2.class);
                startActivityForResult(intent,9999);
            }
            else if(logged){
                textView.setText("您好,游客,请登录!");
                mButton2Login.setText("LOGIN");
                Toast.makeText(getContext(),"登出成功",Toast.LENGTH_SHORT);
                logged=false;
            }

        });
        return root;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9999&&resultCode== Activity.RESULT_OK){
            String name = data.getStringExtra("data_return");
            textView.setText("欢迎!"+name);
            mButton2Login.setText("LOGOUT");
            logged=true;
        }
    }
}