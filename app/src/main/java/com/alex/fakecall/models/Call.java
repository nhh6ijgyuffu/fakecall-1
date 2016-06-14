package com.alex.fakecall.models;


import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Call implements Parcelable {
    public static final String TAG = Call.class.getSimpleName();
    private long id;
    private String name;
    private String number;
    private long time;
    private Theme theme;
    private boolean isAlarmed;
    private String ringtoneUri;
    private boolean vibrate;
    private String photoUri;
    private long callDuration;
    private String voiceUri;

    public Call(){}

    //Parcelable
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
        theme = in.readParcelable(Theme.class.getClassLoader());
        isAlarmed = in.readInt() == 1;
        ringtoneUri = in.readString();
        vibrate = in.readInt() == 1;
        photoUri = in.readString();
        callDuration = in.readLong();
        voiceUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeLong(time);
        dest.writeParcelable(theme, flags);
        dest.writeInt(isAlarmed ? 1 : 0);
        dest.writeString(ringtoneUri);
        dest.writeInt(vibrate ? 1 : 0);
        dest.writeString(photoUri);
        dest.writeLong(callDuration);
        dest.writeString(voiceUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //getter and setter
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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
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
            ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
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
}
