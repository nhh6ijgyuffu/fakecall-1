package com.alex.fakecall.models;


import java.io.Serializable;

public class PhoneUI implements Serializable{
    public static final String KEY = "PhoneUI";
    public String name;
    public String os;
    public Class<?> incoming_act;
    public int incomingRes;
    public int inCallRes;

    public PhoneUI(String name, String os, int incomingRes, int inCallRes, Class<?> incoming_act) {
        this.incomingRes = incomingRes;
        this.inCallRes = inCallRes;
        this.name = name;
        this.os = os;
        this.incoming_act = incoming_act;
    }

    @Override
    public String toString() {
        return "PhoneUI{" +
                "name='" + name + '\'' +
                ", os='" + os + '\'' +
                ", incoming_act=" + incoming_act +
                ", incomingRes=" + incomingRes +
                ", inCallRes=" + inCallRes +
                '}';
    }
}
