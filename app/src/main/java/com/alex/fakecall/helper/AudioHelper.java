package com.alex.fakecall.helper;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;

import com.alex.fakecall.App;

import java.io.File;
import java.io.IOException;

public class AudioHelper {
    private static AudioHelper mInstance;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;

    public static AudioHelper getInstance() {
        if (mInstance == null) {
            mInstance = new AudioHelper();
        }
        return mInstance;
    }

    private AudioHelper() {
    }

    public synchronized void startPlaying(Uri audioUri, boolean loop) {
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

    public synchronized void stopPlaying() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }

    public synchronized void startRecording(File output) {
        if (mRecorder != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(output.getAbsolutePath());
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
        }
        mRecorder = null;
    }
}
