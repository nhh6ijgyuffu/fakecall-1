package com.alex.fakecall.models;

import java.io.Serializable;

public class PhoneUI implements Serializable {
    public static final String KEY = PhoneUI.class.getSimpleName();

    private String name;
    private Class<?> incoming_class;
    private int incomingRes;
    private int inCallRes;

    public PhoneUI(String name, int incomingRes, int inCallRes, Class<?> incoming_class) {
        this.incomingRes = incomingRes;
        this.inCallRes = inCallRes;
        this.name = name;
        this.incoming_class = incoming_class;
    }

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