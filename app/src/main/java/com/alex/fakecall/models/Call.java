package com.alex.fakecall.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;

public class Call implements Parcelable {
    public static final String TAG = Call.class.getSimpleName();
    private long id;
    private String name;
    private String number;
    private long time;
    private boolean isAlarmed;
    private int themeId;
    private String ringtoneUri;
    private boolean vibrate;
    private String photoUri;
    private long callDuration;
    private String voiceUri;
    private int selectedTimeOpt;
    private boolean isPrivate;

    public Call() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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

    public String getRingtoneUri() {
        if (ringtoneUri == null) {
            ringtoneUri = Settings.System.DEFAULT_RINGTONE_URI.toString();
        }
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtone) {
        this.ringtoneUri = ringtone;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public String getVoiceUri() {
        return voiceUri;
    }

    public void setVoiceUri(String voiceUri) {
        this.voiceUri = voiceUri;
    }

    public int getSelectedTimeOpt() {
        return selectedTimeOpt;
    }

    public void setSelectedTimeOpt(int time_opt) {
        this.selectedTimeOpt = time_opt;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public static final Creator<Call> CREATOR = new Creator<Call>() {
        @Override
        public Call createFromParcel(Parcel in) {
            return new Call(in);
        }

        @Override
        public Call[] newArray(int size) {
            return new Call[size];
        }
    };

    private Call(Parcel in) {
        id = in.readLong();
        name = in.readString();
        number = in.readString();
        time = in.readLong();
        themeId = in.readInt();
        isAlarmed = in.readInt() == 1;
        ringtoneUri = in.readString();
        vibrate = in.readInt() == 1;
        photoUri = in.readString();
        callDuration = in.readLong();
        voiceUri = in.readString();
        selectedTimeOpt = in.readInt();
        isPrivate = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeLong(time);
        dest.writeInt(themeId);
        dest.writeInt(isAlarmed ? 1 : 0);
        dest.writeString(ringtoneUri);
        dest.writeInt(vibrate ? 1 : 0);
        dest.writeString(photoUri);
        dest.writeLong(callDuration);
        dest.writeString(voiceUri);
        dest.writeInt(selectedTimeOpt);
        dest.writeInt(isPrivate ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
