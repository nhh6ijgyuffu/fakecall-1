package com.alex.fakecall.models;


import java.io.Serializable;

public class Call implements Serializable {
    public static final String KEY = "CallInstance";

    public String name;
    public String number;

    public PhoneUI phone_ui;

    public Integer call_type;

    public Long alarm_time;

    public String image;

    public Integer repeat;
    public Long repeat_intervals;

    public String voice_file;

    @Override
    public String toString() {
        return "Call{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", phone_ui=" + phone_ui +
                ", call_type=" + call_type +
                ", alarm_time=" + alarm_time +
                ", image='" + image + '\'' +
                ", repeat=" + repeat +
                ", repeat_intervals=" + repeat_intervals +
                ", voice_file='" + voice_file + '\'' +
                '}';
    }
}
