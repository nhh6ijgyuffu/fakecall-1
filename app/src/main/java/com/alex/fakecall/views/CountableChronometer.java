package com.alex.fakecall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;


public class CountableChronometer extends Chronometer {
    private long duration;

    private OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            duration++;
        }
    };

    public CountableChronometer(Context context) {
        super(context);
    }

    public CountableChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        duration = 0;
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
