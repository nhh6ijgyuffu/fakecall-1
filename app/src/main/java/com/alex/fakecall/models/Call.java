package com.alex.fakecall.models;


import java.io.Serializable;

public class Call implements Serializable {
    public static final String KEY = Call.class.getSimpleName();
    private int id = 0;
    private String name;
    private String number;
    private Long time;
    private PhoneUI phone_ui;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneUI getPhoneUI() {
        return phone_ui;
    }

    public void setPhoneUI(PhoneUI phone_ui) {
        this.phone_ui = phone_ui;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getDisplayName() {
        if (name == null || name.isEmpty()) {
            return number;
        }
        return name;
    }
}
