package com.example.exercise1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.Exercise1.MESSAGE";

    private static final int REQUEST_CAMERA = 0x00000012;
    private static final String packageName="com.example.exercise1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch switch1= (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView textView1=(TextView) findViewById(R.id.textView1);
                if (isChecked) {
                    Log.d("Switch1","Checked");
                    textView1.setText("Switch1 Checked");
                }
                else{
                    Log.d("Switch1","Unchecked");
                    textView1.setText("Switch1 Unchecked");
                }
            }
        });
        CheckBox checkBox1=(CheckBox) findViewById(R.id.checkBox1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView textView2=(TextView) findViewById(R.id.textView2);
                if (isChecked) {
                    Log.d("CheckBox1","Checked");
                    textView2.setText("CheckBox1 Checked");
                }
                else{
                    Log.d("CheckBox1","Unchecked");
                    textView2.setText("CheckBox1 Unchecked");
                }
            }
        });
        RadioGroup rGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("radioButton"+checkedId,"Checked");
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                TextView radioResult=(TextView) findViewById(R.id.radioResult);
                String result="你选择了"+radbtn.getText()+",";
                int count = group.getChildCount();
                for(int i = 0 ;i < count;i++){
                    RadioButton rb = (RadioButton)group.getChildAt(i);
                    if(rb.isChecked()){
                        if(i==1)
                            result+="答案正确";
                        else
                            result+="答案错误";
                        break;
                    }
                }

                radioResult.setText(result);
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        String[] spinnerItems = {"0","1","2"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerItems);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.d("Spinner",String.valueOf(pos)+" Selected");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        SeekBar seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("SeekBar","onProgressChanged=" +progress);
                TextView seekBarProgress=(TextView) findViewById(R.id.seekBarProgress);
                seekBarProgress.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("SeekBar","StartTrackingTouch");
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("SeekBar","StopTrackingTouch");
            }
        });

    }
    public void onButton1Clicked(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        //startActivity(intent);
        Log.d("Button1","Pressed");
        Log.d("EditText1",message);
    }

    public void onResetProgressBarClicked(View view){
        Log.d("ResetProgressBar","Clicked");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar=findViewById(R.id.progressBar);
                int max=progressBar.getMax();
                progressBar.setProgress(0);
                for (int i=0;i <= max;i++) {
                    progressBar.setProgress(i);
                    try {
                        Thread.sleep(15);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void onTakePhotoClicked(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView=findViewById(R.id.imageView1);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}