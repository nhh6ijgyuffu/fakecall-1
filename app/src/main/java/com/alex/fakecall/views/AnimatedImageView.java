package com.alex.fakecall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.alex.fakecall.R;

public class AnimatedImageView extends ImageView {

    public AnimatedImageView(Context context) {
        super(context);
    }

    public AnimatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AnimatedImageView,
                0, 0);

        try {
            boolean autoRun = a.getBoolean(R.styleable.AnimatedImageView_autoRun, false);
            if(autoRun) startAnimation();
        } finally {
            a.recycle();
        }
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
