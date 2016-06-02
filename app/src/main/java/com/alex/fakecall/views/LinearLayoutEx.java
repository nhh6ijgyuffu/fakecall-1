package com.alex.fakecall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class LinearLayoutEx extends LinearLayout {
    private OnToggleListener listener;

    public LinearLayoutEx(Context context) {
        super(context);
    }

    public LinearLayoutEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void toggle() {
        int visibility = getVisibility();
        if (visibility == VISIBLE)
            setVisibility(GONE);
        else
            setVisibility(VISIBLE);
    }

    @Override
    public void setVisibility(int visibility) {
        if(listener != null)
            listener.onChange(visibility == VISIBLE);
        super.setVisibility(visibility);
    }

    public void setOnToggleListener(OnToggleListener l){
        listener = l;
    }

    public interface OnToggleListener{
        void onChange(boolean isShowing);
    }
}
