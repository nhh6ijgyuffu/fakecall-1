package com.alex.fakecall.models;

import android.net.Uri;

public class Ringtone {
    private String name;
    private Uri uri;

    public Ringtone(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ringtone)) return false;
        Ringtone other = (Ringtone) o;
        return other.uri.equals(this.uri);
    }
}
