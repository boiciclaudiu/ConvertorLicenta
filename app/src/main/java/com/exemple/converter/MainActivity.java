package com.exemple.converter;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

    static {
        if (!OpenCVLoader.initDebug()) {
            System.out.println("OPENCV not ready...");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button toPhotoConveter = (Button) findViewById(R.id.toPhotoConverter);
        Button toLocationBasedConveter = (Button) findViewById(R.id.toLocationConverter);
        Button toClassicConveter = (Button) findViewById(R.id.toClassicConverter);

        toPhotoConveter.setOnClickListener(btn);
        toLocationBasedConveter.setOnClickListener(btn);
        toClassicConveter.setOnClickListener(btn);
    }

    public View.OnClickListener btn = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toPhotoConverter:
                    Intent intent = new Intent(MainActivity.this, OcrCaptureActivity.class);
                    startActivityForResult(intent, 9003);
                    break;
                case R.id.toLocationConverter:
                    Intent j = new Intent(MainActivity.this, LocationBasedConverter.class);
                    startActivity(j);
                    break;
                case R.id.toClassicConverter:
                    Intent k = new Intent(MainActivity.this, ClassicConverter.class);
                    startActivity(k);
                    break;
            }
        }
    };

}