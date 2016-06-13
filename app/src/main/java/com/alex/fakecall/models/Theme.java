package com.alex.fakecall.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Theme implements Parcelable {
    public static final String TAG = Theme.class.getSimpleName();

    private String name;
    private Class<?> clazz;
    private int incomingRes;
    private int inCallRes;

    public Theme(String name, Class<?> clazz, int incomingRes, int inCallRes) {
        this.name = name;
        this.clazz = clazz;
        this.incomingRes = incomingRes;
        this.inCallRes = inCallRes;
    }

    //Parcelable
    private Theme(Parcel in) {
        name = in.readString();
        clazz = (Class<?>) in.readSerializable();
        incomingRes = in.readInt();
        inCallRes = in.readInt();
    }

    public static final Creator<Theme> CREATOR = new Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel in) {
            return new Theme(in);
        }


        @Override
        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeSerializable(clazz);
        dest.writeInt(incomingRes);
        dest.writeInt(inCallRes);
    }

    //Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public int getIncomingRes() {
        return incomingRes;
    }

    public int getInCallRes() {
        return inCallRes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Theme)) return false;
        Theme other = (Theme) o;
        return other.clazz.equals(this.clazz);
    }
}