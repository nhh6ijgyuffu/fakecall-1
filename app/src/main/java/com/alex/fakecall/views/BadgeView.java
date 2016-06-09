package com.alex.fakecall.views;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;


public class BadgeView extends FrameLayout {
    private TextView tvCounter;
    private ImageView ivIcon;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.badge_view, this, true);
        tvCounter = (TextView) findViewById(R.id.badgeCounter);
        ivIcon = (ImageView) findViewById(R.id.badgeIcon);
    }

    public void setCounter(int count) {
        tvCounter.setText(String.valueOf(count));
    }

    public void setBadgeIcon(@DrawableRes int resId) {
        ivIcon.setImageResource(resId);
    }
}
