package com.alex.fakecall.controllers;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;

import com.alex.fakecall.FakeCallApp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AudioController {
    private static AudioController mInstance;
    private HashMap<PlayerTag, MediaPlayer> mediaPlayers;
    private MediaRecorder mRecorder;

    public enum PlayerTag{
        RINGTONE,
        VOICE
    }

    public static synchronized AudioController getInstance() {
        if (mInstance == null) {
            mInstance = new AudioController();
        }
        return mInstance;
    }

    private AudioController() {
        mediaPlayers = new HashMap<>();
    }

    public synchronized void startPlaying(PlayerTag tag, Uri audioUri, boolean loop) {
        MediaPlayer mp = mediaPlayers.get(tag);
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
        try {
            MediaPlayer mPlayer = new MediaPlayer();
            mediaPlayers.put(tag, mPlayer);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(FakeCallApp.getInstance(), audioUri);
            mPlayer.prepare();
            mPlayer.setLooping(loop);
            mPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void stopPlaying(PlayerTag tag) {
        MediaPlayer mp = mediaPlayers.get(tag);
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
        mediaPlayers.remove(tag);
    }

    public synchronized void stopAllPlaying() {
        for (PlayerTag tag : mediaPlayers.keySet()) {
            stopPlaying(tag);
        }
    }

    public synchronized void startRecording(File output) {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
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
