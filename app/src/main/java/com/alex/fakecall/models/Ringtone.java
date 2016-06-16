package com.alex.fakecall.models;

public class Ringtone {
    private String name;
    private String uriStr;

    public Ringtone(String name, String uriStr) {
        this.name = name;
        this.uriStr = uriStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUriString() {
        return uriStr;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ringtone)) return false;
        Ringtone other = (Ringtone) o;
        return other.uriStr.equals(this.uriStr);
    }
}
