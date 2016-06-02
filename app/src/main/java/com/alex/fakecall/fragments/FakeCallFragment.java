package com.alex.fakecall.fragments;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.MainActivity;
import com.alex.fakecall.activities.MoreCallSettingsActivity;
import com.alex.fakecall.activities.PhoneUISelectorActivity;
import com.alex.fakecall.data.Database;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.models.PhoneUI;
import com.alex.fakecall.receivers.FakeCallReceiver;
import com.alex.fakecall.utils.Converter;
import com.alex.fakecall.utils.DialogUtils;
import com.alex.fakecall.utils.SimpleTextWatcher;
import com.alex.fakecall.views.DateTimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class FakeCallFragment extends BaseFragment {
    @BindView(R.id.tvTimeValue)
    TextView tvTimeValue;

    @BindView(R.id.tvPhoneUIValue)
    TextView tvPhoneUIValue;

    @BindView(R.id.edtCallerName)
    EditText edtCallerName;

    @BindView(R.id.edtCallerNumber)
    EditText edtCallerNumber;

    private long selectedTimeInterval = 0;

    private static final int REQUEST_MORE_SETTING = 1;
    private static final int REQUEST_PHONE_UI = 2;

    private Call mCall;

    private AlarmManager alarmManager;

    private Database db;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_fake_call;
    }

    @Override
    protected void setUp() {
        if (getArguments() != null) {
            mCall = (Call) getArguments().getSerializable(Call.KEY);
        }
        if (mCall == null)
            mCall = new Call();

        edtCallerName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mCall.name = s.toString().trim();
            }
        });

        edtCallerNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mCall.number = s.toString().trim();
            }
        });

        this.alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        this.db = new Database(getContext());
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
        if (selectedTimeInterval != -1) {
            mCall.alarm_time = (System.currentTimeMillis() + selectedTimeInterval);
        }

        Toast.makeText(getContext(), "New call planned!", Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(getContext(), FakeCallReceiver.class);
        myIntent.putExtra(Call.KEY, mCall);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, mCall.alarm_time, pendingIntent);

        db.addNewCall(mCall);
        ((MainActivity)getActivity()).selectPage(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PHONE_UI) {
                if (data != null) {
                    PhoneUI pui = (PhoneUI) data.getSerializableExtra(PhoneUI.KEY);

                    tvPhoneUIValue.setText(pui.name);
                    mCall.phone_ui = pui;
                }
            }
        }
    }

    public static FakeCallFragment newInstance(Call mCall) {
        FakeCallFragment frag = new FakeCallFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Call.KEY, mCall);
        frag.setArguments(bundle);
        return frag;
    }
}
