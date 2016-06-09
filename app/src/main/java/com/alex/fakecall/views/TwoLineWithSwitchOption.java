package com.alex.fakecall.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwoLineWithSwitchOption extends FrameLayout {
    @BindView(R.id.tvPrimary)
    TextView tvOption;

    @BindView(R.id.tvSecondary)
    TextView tvValue;

    @BindView(R.id.switchView)
    SwitchCompat switchView;

    @BindView(R.id.ivLeftIcon)
    ImageView ivIconLeft;

    private String valueOnChecked, valueOnNoChecked;

    private OnCheckedChangeListener checkedListener;

    public TwoLineWithSwitchOption(Context context) {
        this(context, null);
    }

    public TwoLineWithSwitchOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.two_line_with_switch_opt, this, true);
        ButterKnife.bind(this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomOptView,
                0, 0);

        try {
            String optTitle = a.getString(R.styleable.MyCustomOptView_optText);
            tvOption.setText(optTitle);

            int ivLeftRes = a.getResourceId(R.styleable.MyCustomOptView_leftIcon, 0);
            ivIconLeft.setImageResource(ivLeftRes);

            boolean isChecked = a.getBoolean(R.styleable.MyCustomOptView_checked, false);
            switchView.setChecked(isChecked);

            valueOnChecked = a.getString(R.styleable.MyCustomOptView_valueOnChecked);
            valueOnNoChecked = a.getString(R.styleable.MyCustomOptView_valueOnNoChecked);

            tvValue.setText(isChecked ? valueOnChecked : valueOnNoChecked);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkedListener != null)
                    checkedListener.onCheckedChange(buttonView, isChecked);
                tvValue.setText(isChecked ? valueOnChecked : valueOnNoChecked);
            }
        });
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(CompoundButton switchView, boolean checked);
    }

    public void toggle() {
        switchView.toggle();
    }

    public boolean isChecked() {
        return switchView.isChecked();
    }

    public void setOnSwitchCheckedChangeListener(OnCheckedChangeListener l) {
        checkedListener = l;
    }
}
