package com.alex.fakecall.models;


import android.media.RingtoneManager;
import android.net.Uri;

import java.io.Serializable;

public class Call implements Serializable {
    public static final String KEY = Call.class.getSimpleName();

    private Long id;
    private String name;
    private String number;
    private Long time;
    private Theme theme;
    private boolean isAlarmed;
    private String ringtoneStr;
    private boolean vibrate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isAlarmed() {
        return isAlarmed;
    }

    public void setAlarmed(boolean alarmed) {
        isAlarmed = alarmed;
    }

    public String getDisplayName() {
        if (name == null || name.isEmpty()) {
            return number;
        }
        return name;
    }

    public String getRingtoneStr() {
        if (ringtoneStr == null) {
            Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            setRingtoneStr(defaultUri.toString());
        }
        return ringtoneStr;
    }

    public void setRingtoneStr(String ringtone) {
        this.ringtoneStr = ringtone;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }


    @Override
    public String toString() {
        return "Call{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", time=" + time +
                ", theme=" + theme +
                ", isAlarmed=" + isAlarmed +
                ", ringtoneStr='" + ringtoneStr + '\'' +
                '}';
    }
}
