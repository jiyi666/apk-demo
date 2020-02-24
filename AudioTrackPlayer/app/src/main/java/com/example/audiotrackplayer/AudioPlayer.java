package com.example.audiotrackplayer;

public class AudioPlayer {

    private TrackAudio mTrack = null;
    private final boolean staticTrack = false;

    public final int prepared = 1;
    public final int playing = 2;
    public final int pause = 3;
    public final int stop = 4;

    /* 构造暂时不考虑其他操作 */
    AudioPlayer(){
    }

    public void play() throws Exception{
        /* 只支持一个track，暂不做多track混音 */
        if (staticTrack){       //static模式单独创建track
            mTrack = new TrackAudio(staticTrack);
        } else {
            //if (mTrack == null){
                mTrack = new TrackAudio();
            //}
            //Thread.currentThread().sleep(1000);//毫秒
            mTrack.trackPlay();
        }
    }
    public void pasue() throws Exception{
        mTrack.trackPause();
    }
    public void stop() throws Exception{
        mTrack.trackStop();
        mTrack = null;
    }
}
