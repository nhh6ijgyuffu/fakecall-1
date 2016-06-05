package com.alex.fakecall.helper;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class AudioHelper {
    private static AudioHelper mInstance;
    private MediaPlayer mPlayer;
    private Context mContext;

    public static AudioHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new AudioHelper(ctx);
        }
        return mInstance;
    }

    public AudioHelper(Context ctx) {
        mContext = ctx;
    }

    public void playAudio(Uri audioUri, boolean loop) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mContext, audioUri);
            mPlayer.prepare();
            mPlayer.setLooping(loop);
            mPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopAudio() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }
}
