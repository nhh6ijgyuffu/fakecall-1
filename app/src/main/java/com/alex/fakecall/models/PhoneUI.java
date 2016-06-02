package com.alex.fakecall.models;


import java.io.Serializable;

public class PhoneUI implements Serializable{
    public String name;
    public String os;
    public Class<?> incoming_act;

    public PhoneUI(String name, String os, Class<?> incoming_act) {
        this.name = name;
        this.os = os;
        this.incoming_act = incoming_act;
    }
}
