package com.example.audiotrackplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AudioPlayer";

    /* 实例化player对象 */
    AudioPlayer pcmPlayer = new AudioPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        /* 运行时权限申请 */
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            Log.d(TAG, "we have got WRITE_EXTERNAL_STORAGE permisson！");
        }
    }

    /* 权限申请回调函数 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    Toast.makeText(this, "成功获取读取存储权限",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                try {
                    pcmPlayer.play();
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.pause:
                try {
                    pcmPlayer.pasue();
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case  R.id.stop:
                try {
                    pcmPlayer.stop();
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
