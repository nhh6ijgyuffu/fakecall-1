package com.alex.fakecall.helper_utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

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

    public void playRingtone(Context ctx, Uri audioUri, boolean loop) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(ctx, audioUri);
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
