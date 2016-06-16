package com.alex.fakecall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;


public class MyChronometer extends Chronometer {
    private long duration;

    private OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            duration++;
        }
    };

    public MyChronometer(Context context) {
        super(context);
    }

    public MyChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnChronometerTickListener(listener);
    }

    @Override
    public void stop() {
        super.stop();
        duration = 0;
    }

    public long getDuration() {
        return duration;
    }
}
