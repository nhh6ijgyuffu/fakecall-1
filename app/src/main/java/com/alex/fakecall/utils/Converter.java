package com.alex.fakecall.utils;


public class Converter {

    public static String secondToMMSS(int totalSeconds){
        String theFormat = "%02d:%02d";
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = (totalSeconds % 3600) % 60;
        return String.format( theFormat, minutes, seconds);
    }
}
