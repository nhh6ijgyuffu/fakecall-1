package com.alex.fakecall.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Theme implements Parcelable {
    public static final String KEY = Theme.class.getSimpleName();

    private String name;
    private Class<?> incoming_class;
    private int incomingRes;
    private int inCallRes;

    public Theme(String name, int incomingRes, int inCallRes, Class<?> incoming_class) {
        this.name = name;
        this.incoming_class = incoming_class;
        this.incomingRes = incomingRes;
        this.inCallRes = inCallRes;
    }

    //Parcelable
    private Theme(Parcel in) {
        name = in.readString();
        incoming_class = (Class<?>) in.readSerializable();
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
        dest.writeInt(incomingRes);
        dest.writeInt(inCallRes);
        dest.writeSerializable(incoming_class);
    }

    //Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getIncomingClass() {
        return incoming_class;
    }

    public void setIncomingClass(Class<?> incoming_class) {
        this.incoming_class = incoming_class;
    }

    public int getIncomingRes() {
        return incomingRes;
    }

    public void setIncomingRes(int incomingRes) {
        this.incomingRes = incomingRes;
    }

    public int getInCallRes() {
        return inCallRes;
    }

    public void setInCallRes(int inCallRes) {
        this.inCallRes = inCallRes;
    }


}