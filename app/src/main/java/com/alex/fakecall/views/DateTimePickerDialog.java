package com.alex.fakecall.views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alex.fakecall.R;
import com.alex.fakecall.helper_utils.Converter;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateTimePickerDialog extends
        AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @BindView(R.id.tvDate)
    TextView tvDate;

    private OnDateTimeSetListener listener;

    private Calendar mCalendar;

    public DateTimePickerDialog(Context context, Calendar initCal, OnDateTimeSetListener listener) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_date_time_picker, null);
        setView(view);

        ButterKnife.bind(this, view);

        this.listener = listener;

        if (initCal == null)
            initCal = Calendar.getInstance();

        this.mCalendar = initCal;

        datePicker.init(initCal.get(Calendar.YEAR),
                initCal.get(Calendar.MONTH), initCal.get(Calendar.DAY_OF_MONTH), this);

        timePicker.setCurrentHour(initCal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(initCal.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(this);

        setButton(BUTTON_POSITIVE, "Set", this);
        setButton(BUTTON_NEGATIVE, "Cancel", this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE) {
            if (listener != null) {
                mCalendar.set(Calendar.SECOND, 0);
                listener.OnDateTimeSet(mCalendar);
            }
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String value = Converter.calendar2String(mCalendar, "dd/MM/yyyy");
        tvDate.setText(value);
    }

    @OnClick(R.id.tvDate)
    void onToggleDatePicker() {
        boolean visible = datePicker.getVisibility() == View.VISIBLE;
        datePicker.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
    }

    public interface OnDateTimeSetListener {
        void OnDateTimeSet(Calendar calendar);
    }
}
