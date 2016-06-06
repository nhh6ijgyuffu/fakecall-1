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

    public static class PhoneUI implements Serializable {
        public static final String KEY = PhoneUI.class.getSimpleName();

        private String name;
        private Class<?> incoming_act;
        private int incomingRes;
        private int inCallRes;

        public PhoneUI(String name, int incomingRes, int inCallRes, Class<?> incoming_act) {
            this.incomingRes = incomingRes;
            this.inCallRes = inCallRes;
            this.name = name;
            this.incoming_act = incoming_act;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

}
