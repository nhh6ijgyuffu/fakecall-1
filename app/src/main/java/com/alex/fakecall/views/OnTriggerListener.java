package com.alex.fakecall.views;


public interface OnTriggerListener {
    int TRIGGER_NONE = 0;
    int TRIGGER_ANSWER = 1;
    int TRIGGER_DECLINE = 2;
    int TRIGGER_TEXT = 3;

    void onTrigger(int whatToTrigger);
}
