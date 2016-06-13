package com.alex.fakecall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TwoLineTextOption extends FrameLayout {
    @BindView(R.id.tvPrimary)
    TextView tvOption;

    @BindView(R.id.tvSecondary)
    TextView tvOptValue;

    @BindView(R.id.ivLeftIcon)
    ImageView ivIconLeft;

    @BindView(R.id.ivRightIcon)
    ImageView ivIconRight;

    public TwoLineTextOption(Context context) {
        this(context, null);
    }

    public TwoLineTextOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.two_line_text_opt, this, true);

        ButterKnife.bind(this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomOptView,
                0, 0);

        try {
            String optTitle = a.getString(R.styleable.MyCustomOptView_optText);
            tvOption.setText(optTitle);

            String optValue = a.getString(R.styleable.MyCustomOptView_valueText);
            tvOptValue.setText(optValue);

            int ivLeftRes = a.getResourceId(R.styleable.MyCustomOptView_leftIcon, 0);
            ivIconLeft.setImageResource(ivLeftRes);

            int ivRightRes = a.getResourceId(R.styleable.MyCustomOptView_rightIcon, 0);
            ivIconRight.setImageResource(ivRightRes);

            boolean showRightIcon = a.getBoolean(R.styleable.MyCustomOptView_showRightIcon, false);
            ivIconRight.setVisibility(showRightIcon ? VISIBLE : GONE);
        } finally {
            a.recycle();
        }
    }

    public void setOnRightIconClickListener(View.OnClickListener l){
        ivIconRight.setOnClickListener(l);
    }

    public void setRightIconVisibility(int v){
        ivIconRight.setVisibility(v);
    }

    public void setValue(@StringRes int value) {
        tvOptValue.setText(value);
    }

    public void setValue(String value) {
        tvOptValue.setText(value);
    }
}
