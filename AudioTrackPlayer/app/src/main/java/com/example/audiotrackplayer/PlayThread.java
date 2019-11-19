package com.example.audiotrackplayer;

import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PlayThread implements Runnable{

    private final String TAG = "PlaybackThread";

    private AudioTrack mTrack;
    public FileInputStream mFileInputStream;
    public int mTempBufferSize;
    public byte[] mTempBuffer = null;

    /* 线程结束标志位 */
    private boolean mFlag = true;

    /* 构造 */
    PlayThread(AudioTrack pTrack, int pBufferSize){
        this.mTrack = pTrack;
        this.mTempBufferSize = pBufferSize;
        this.mTempBuffer = new byte[mTempBufferSize];
    }

    @Override
    public void run(){
        if (mTrack != null){
            /* 获取输入流 */
            if (!getFileStream()){
                mFlag = false;
                mTrack.release();
            }

            /* 线程不停止则一直写数据 */
            while (mFlag){
                writeData();
            }
        } else {
            Log.e(TAG, "run: thread is null pointer!");
        }
    }

    public void stop(){
        this.mFlag = false;
    }

    public boolean getFileStream(){
        /* 1.获取文件 */
        File file = new File(Environment.getExternalStorageDirectory(),
                "test1.wav");
        if (!file.exists())  {
            Log.e(TAG, "file don't exist!");
            return false;
        }
        /* 2.获取输入文件流 */
        try {
            mFileInputStream = new FileInputStream(file);
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public void writeData(){
        try {
            while (mFileInputStream.available() > 0) {
                int readCount = mFileInputStream.read(mTempBuffer);
                if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                        readCount == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }

                if (readCount != 0 && readCount != -1) {
                    if (mTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING){
                        mTrack.write(mTempBuffer, 0, readCount);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
