package com.alex.fakecall.models;

import android.net.Uri;

public class AudioObj {
    private String name;
    private Uri uri;

    public AudioObj(String name, Uri uri) {
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

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof AudioObj)) return false;
        AudioObj other = (AudioObj) o;
        return other.uri.equals(this.uri);
    }
}
