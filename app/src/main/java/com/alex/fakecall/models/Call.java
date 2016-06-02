package com.alex.fakecall.models;


import java.io.Serializable;

public class Call implements Serializable {
    public static final String TAG = "CallInstance";

    public String name;
    public String number;

    public PhoneUI phone_ui;

    public Integer call_type;

    public Long alarm_time;

    public String image;

    public Integer repeat;
    public Long repeat_intervals;

    public String voice_file;

}
