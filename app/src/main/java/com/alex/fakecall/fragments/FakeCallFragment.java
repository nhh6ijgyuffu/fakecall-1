package com.alex.fakecall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.DateTimePickerDialog;
import com.alex.fakecall.activities.MoreCallSettingsActivity;
import com.alex.fakecall.activities.PhoneUISelectorActivity;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.Converter;
import com.alex.fakecall.utils.DialogUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class FakeCallFragment extends BaseFragment {
    @BindView(R.id.tvTimeValue)
    TextView tvTimeValue;

    private long selectedTimeInterval = 0;

    private static final int REQUEST_MORE_SETTING = 1;
    private static final int REQUEST_PHONE_UI = 2;

    private Call mCall;

    @Override
    public int getLayoutResource() {
        return R.layout.fake_call;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mCall = (Call) getArguments().getSerializable(Call.TAG);
        }
        if (mCall == null)
            mCall = new Call();
    }

    @OnClick(R.id.btnSelectTime)
    void onSelectTime(View v) {
        DialogUtils.showPopupMenu(getContext(),
                v.findViewById(R.id.anchorLeft), R.menu.select_time, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.mn_now) {
                            selectedTimeInterval = 0;
                            tvTimeValue.setText(R.string.default_time_value);
                        } else if (id == R.id.mn_15s) {
                            selectedTimeInterval = 15 * 1000;
                            tvTimeValue.setText(R.string.time_value_15s);
                        } else if (id == R.id.mn_30s) {
                            selectedTimeInterval = 30 * 1000;
                            tvTimeValue.setText(R.string.time_value_30s);
                        } else if (id == R.id.mn_1m) {
                            selectedTimeInterval = 60 * 1000;
                            tvTimeValue.setText(R.string.time_value_1m);
                        } else if (id == R.id.mn_5m) {
                            selectedTimeInterval = 5 * 60 * 1000;
                            tvTimeValue.setText(R.string.time_value_5m);
                        } else if (id == R.id.mn_custom) {
                            selectedTimeInterval = -1;

                            DateTimePickerDialog dtd = new DateTimePickerDialog(getContext(), null,
                                    new DateTimePickerDialog.OnDateTimeSetListener() {
                                @Override
                                public void OnDateTimeSet(Calendar calendar) {
                                    String value = Converter.calendar2String(calendar, "dd/MM/yyyy HH:mm");
                                    tvTimeValue.setText(value);

                                    //save directly
                                    mCall.alarm_time = calendar.getTimeInMillis();
                                }
                            });
                            dtd.show();

                        }
                        return false;
                    }
                });
    }

    @OnClick(R.id.btnMore)
    void onBtnMore() {
        Intent intent = new Intent(getContext(), MoreCallSettingsActivity.class);
        startActivityForResult(intent, REQUEST_MORE_SETTING);
    }

    @OnClick(R.id.btnPhoneUI)
    void onBtnPhoneUI() {
        Intent intent = new Intent(getContext(), PhoneUISelectorActivity.class);
        startActivityForResult(intent, REQUEST_PHONE_UI);
    }

    @OnClick(R.id.btnSave)
    void saveCall() {
        //Time of call
        if (selectedTimeInterval != -1) {
            mCall.alarm_time = (System.currentTimeMillis() + selectedTimeInterval);
        }
    }

    public static FakeCallFragment newInstance(Call mCall) {
        FakeCallFragment frag = new FakeCallFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Call.TAG, mCall);

        frag.setArguments(bundle);

        return frag;
    }
}
