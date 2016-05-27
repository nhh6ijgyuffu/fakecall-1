package com.alex.fakecall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.alex.fakecall.R;

public class AnimImageView extends ImageView {
    private boolean autoRun = false;

    public AnimImageView(Context context) {
        super(context);
    }

    public AnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AnimImageView,
                0, 0);

        try {
            autoRun = a.getBoolean(R.styleable.AnimImageView_autoRun, false);
        } finally {
            a.recycle();
        }

        if(autoRun) startAnimation();
    }

    public void startAnimation() {
       final AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        if (animationDrawable != null) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    animationDrawable.start();
                }
            });
        }
    }
}
