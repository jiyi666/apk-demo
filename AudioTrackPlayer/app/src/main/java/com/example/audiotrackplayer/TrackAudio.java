package com.example.audiotrackplayer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class TrackAudio {

    private static final int SAMPLERATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioTrack mTrack;

    public int mMinBufferSize;
    public int mSampleRate;
    public int mChannelCount;
    public int mBitwidth;
    public PlayThread mThread;

    TrackAudio(){
        trackConfig();
        trackCreate();
    }

    public void trackConfig(){
        mSampleRate = SAMPLERATE;
        mChannelCount = CHANNEL_CONFIG;
        mBitwidth = AUDIO_FORMAT;
    }

    public void trackCreate(){
        /* 1.获取最小buffersize */
        mMinBufferSize = AudioTrack.getMinBufferSize(mSampleRate,
                mChannelCount, mBitwidth);
        /* 2.创建audiotrack */
        mTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                mSampleRate, mChannelCount, mBitwidth,
                mMinBufferSize, AudioTrack.MODE_STREAM);
        /* 3.创建数据读取线程 */
        mThread = new PlayThread(mTrack, mMinBufferSize);
    }

    public void trackPlay(){
        /* stream模式要先playtrack然后再去write数据 */
        mTrack.play();
        Thread t = new Thread(mThread, "pcm playback thread!");
        /* track.write在线程run()函数中 */
        t.start();
    }

    public void trackStop(){
        if (mTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING){
            mTrack.stop();
            mTrack.release();
            mThread.stop();
        }

    }
    public void trackPause(){
        mTrack.pause();
    }
}
