package com.example.jnitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
                int[] arrays = {1, 2, 3};
                JNITest obj = new JNITest();
                int[] arraysNew = obj.intMathod(arrays);
                for (int i = 0; i < arraysNew.length; i++)
                {
                    Log.e(TAG, "onClick: arrys:" + arraysNew[i]);
                }

                Toast.makeText(MainActivity.this, obj.str, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
