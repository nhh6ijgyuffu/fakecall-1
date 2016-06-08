package com.alex.fakecall.helper;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.alex.fakecall.App;

import java.io.IOException;

public class RingtoneHelper {
    private static RingtoneHelper mInstance;
    private MediaPlayer mPlayer;

    public static RingtoneHelper getInstance() {
        if (mInstance == null) {
            mInstance = new RingtoneHelper();
        }
        return mInstance;
    }

    private RingtoneHelper() {
    }

    public void playRingtone(Uri audioUri, boolean loop) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(App.getInstance(), audioUri);
            mPlayer.prepare();
            mPlayer.setLooping(loop);
            mPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopRingtone() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }
}
