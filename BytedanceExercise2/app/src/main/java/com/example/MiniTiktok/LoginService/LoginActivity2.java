package com.example.MiniTiktok.LoginService;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MiniTiktok.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginActivity2 extends AppCompatActivity {
    private View mInputLayout;
    public boolean mLogin=true;
    private View progress;
    private Button mLoginButton;
    private LinearLayout mName,mPsw;
    private EditText nameEditText;
    private TextView mRegisterBu;
    private EditText pwdEditText;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mInputLayout=findViewById(R.id.input_layout);
        progress = findViewById(R.id.layout_progress);
        mLoginButton=findViewById(R.id.main_btn_login);
        mName=(LinearLayout) findViewById(R.id.input_layout_name);
        mPsw=(LinearLayout) findViewById(R.id.input_layout_psw);
        nameEditText=findViewById(R.id.username);
        pwdEditText=findViewById(R.id.password);
        mRegisterBu=findViewById(R.id.signupBU);

        mRegisterBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLogin==true)
                {
                    mLoginButton.setText("Sign up");
                    mRegisterBu.setText("Login");
                }
                else
                {
                    mLoginButton.setText("Login");
                    mRegisterBu.setText("Signup");
                }
                mLogin=!mLogin;
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mWidth = mLoginButton.getMeasuredWidth();
                int mHeight = mLoginButton.getMeasuredHeight();
                // 隐藏输入框
                final String name = nameEditText.getText().toString();
                final String pwd = pwdEditText.getText().toString();
                if (TextUtils.isEmpty(nameEditText.getText()) || TextUtils.isEmpty(pwdEditText.getText())) {
                    Toast.makeText(LoginActivity2.this,"请输入用户名或密码",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mName.setVisibility(View.INVISIBLE);
                    mPsw.setVisibility(View.INVISIBLE);
                    inputAnimator(mInputLayout, mWidth, mHeight);

                    nameEditText.postDelayed(() -> new AsyncTask<Void,Void,String>(){


                        @Override
                        protected String doInBackground(Void... voids) {
                            String resultJson = login(name , pwd, mLogin);
                            return resultJson;
                        }
                        @Override
                        protected void onPostExecute(String resultJson) {
                            try {
                                JSONObject jsonObject = new JSONObject(resultJson);
                                String status = jsonObject.getString("success");
                                if(mLogin==true)
                                {
                                    if( "true".equals(status) ){
                                        Toast.makeText(LoginActivity2.this,"登陆成功",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent();
                                        intent.putExtra("data_return",name);
                                        intent.putExtra("sid_return",name);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity2.this,"错误的用户名或者密码",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity2.this, LoginActivity2.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else
                                {
                                    if( "true".equals(status) ){
                                        String sid = jsonObject.getString("sid");
                                        Toast.makeText(LoginActivity2.this,"登陆成功,您的sid为"+sid,Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent();
                                        intent.putExtra("data_return",name);
                                        intent.putExtra("sid_return",name);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity2.this,"错误的用户名或者密码",Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(),1000);
                }
            }
        });
    }
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
    }
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(500);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }
    public class JellyInterpolator extends LinearInterpolator {
        private float factor;

        public JellyInterpolator() {
            this.factor = 0.15f;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input)
                    * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }
    public static String login(String username,String password,boolean isLogin) {
        String msg = "";
        try {
            username = URLEncoder.encode(username, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String urlStr;
        if (isLogin==true)
        {
            urlStr="http://39.96.72.171/upload?event=login&name=" + username + "&passwd=" + SHA256.getSHA256(password);
        }
        else
        {
            urlStr="http://39.96.72.171/upload?event=register&name=" + username + "&passwd=" + SHA256.getSHA256(password);
        }
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (msg == null) {
                    msg = line;
                } else {
                    msg += line;
                }
            }
            reader.close();
            in.close();//关闭数据流
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally { //做清理操作 }
            Log.d("LoginActivity2",msg);
            return msg;
        }
}}