package com.alex.fakecall.models;


import android.net.Uri;

import com.alex.fakecall.utils.Utils;

import java.io.File;

public class Voice {
    public static final String TAG = "Voice";
    private String uriString;
    private String name;
    private long duration;
    private int sizeKB;

    public Voice(File file) {
        uriString = Uri.fromFile(file).toString();
        name = file.getName();
        duration = Utils.getDurationOfAudioFile(file);
        sizeKB = (int) (file.length() / 1024);
    }

    public String getUriString() {
        return uriString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSizeKB() {
        return sizeKB;
    }

    public String getFormattedDuration() {
        int seconds = (int) (duration / 1000) % 60;
        int minutes = (int) ((duration / (1000 * 60)) % 60);
        int hours = (int) ((duration / (1000 * 60 * 60)) % 24);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
