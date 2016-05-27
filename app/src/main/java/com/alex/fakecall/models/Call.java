package com.alex.fakecall.models;

import android.provider.CallLog;

import java.io.Serializable;

public class Call implements Serializable{
    private String callerName;
    private String callerNumber;
    private String callerAvatarPath;
    private String voiceFilePath;
    private int duration;
    private int type;

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getCallerAvatarPath() {
        return callerAvatarPath;
    }

    public void setCallerAvatarPath(String callerAvatarPath) {
        this.callerAvatarPath = callerAvatarPath;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public void setVoiceFilePath(String voiceFilePath) {
        this.voiceFilePath = voiceFilePath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
