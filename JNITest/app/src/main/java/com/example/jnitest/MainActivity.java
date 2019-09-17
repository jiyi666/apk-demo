package com.example.jnitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "jnitest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button jniTest = (Button) findViewById(R.id.JNITest);

        jniTest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.JNITest:
                new JNITest().callStrFromJni();
                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
