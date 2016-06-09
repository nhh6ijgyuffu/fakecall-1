package com.alex.fakecall.models;


import java.io.Serializable;

public class Call implements Serializable {
    public static final String KEY = Call.class.getSimpleName();

    private Long id;
    private String name;
    private String number;
    private Long time;

    private Theme phone_ui;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Theme getPhoneUI() {
        return phone_ui;
    }

    public void setPhoneUI(Theme phone_ui) {
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
