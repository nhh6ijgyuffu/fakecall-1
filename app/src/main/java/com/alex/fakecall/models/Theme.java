package com.alex.fakecall.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Theme implements Parcelable {
    public static final String TAG = Theme.class.getSimpleName();

    private int id;
    private String name;
    private int incomingRes;
    private int inCallRes;

    public Theme(int id) {
        this.id = id;
    }

    public Theme(int id, String name, int incomingRes, int inCallRes) {
        this.id = id;
        this.name = name;
        this.incomingRes = incomingRes;
        this.inCallRes = inCallRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return ((Theme) o).id == this.id;
    }

    private Theme(Parcel in) {
        id = in.readInt();
        name = in.readString();
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(incomingRes);
        dest.writeInt(inCallRes);
    }
}