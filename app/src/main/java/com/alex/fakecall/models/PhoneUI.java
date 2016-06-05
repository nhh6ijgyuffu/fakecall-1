package com.alex.fakecall.models;


import java.io.Serializable;

public class PhoneUI implements Serializable {
    public static final String KEY = PhoneUI.class.getSimpleName();

    private String name;
    private String os;
    private Class<?> incoming_act;
    private int incomingRes;
    private int inCallRes;

    public PhoneUI(String name, String os, int incomingRes, int inCallRes, Class<?> incoming_act) {
        this.incomingRes = incomingRes;
        this.inCallRes = inCallRes;
        this.name = name;
        this.os = os;
        this.incoming_act = incoming_act;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Class<?> getIncoming_act() {
        return incoming_act;
    }

    public void setIncoming_act(Class<?> incoming_act) {
        this.incoming_act = incoming_act;
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

