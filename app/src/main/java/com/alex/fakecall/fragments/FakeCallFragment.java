package com.alex.fakecall.fragments;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.PhoneUISelectorActivity;
import com.alex.fakecall.helper.AlarmHelper;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.models.PhoneUI;
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

    @BindView(R.id.lyMoreSettings)
    View lyMoreSettings;

    @BindView(R.id.ivExpandSetting)
    ImageView ivExpandSetting;

    @BindView(R.id.tvMore)
    TextView tvMore;

    private long selectedTimeInterval = 0;
    private long calendarTimePicked = 0;

    private static final int REQUEST_PHONE_UI = 1;
    private static final int REQUEST_PICK_CONTACT = 2;

    private Call mCall;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_fake_call;
    }

    @Override
    protected void setUp() {
        if (getArguments() != null)
            mCall = (Call) getArguments().getSerializable(Call.KEY);

        if (mCall == null)
            mCall = new Call();

        edtCallerName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mCall.setName(s.toString().trim());
            }
        });

        edtCallerNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mCall.setNumber(s.toString().trim());
            }
        });
    }

    @OnClick(R.id.btnChangeCaller)
    void onBtnChangeCallerClick(View v) {
        DialogUtils.showPopupMenu(getContext(), v, R.menu.change_caller, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int id = item.getItemId();
                switch (id) {
                    case R.id.mn_private:
                        edtCallerName.setText(R.string.lb_private_num);
                        edtCallerNumber.setText("");
                        break;
                    case R.id.mn_contact:
                        try {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                            startActivityForResult(intent, REQUEST_PICK_CONTACT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btnSelectTime)
    void onSelectTime(View v) {
        DialogUtils.showPopupMenu(getContext(),
                v.findViewById(R.id.anchorLeft), R.menu.select_time, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.mn_now:
                                selectedTimeInterval = 0;
                                tvTimeValue.setText(R.string.default_time_value);
                                break;
                            case R.id.mn_15s:
                                selectedTimeInterval = 15 * 1000;
                                tvTimeValue.setText(R.string.time_value_15s);
                                break;
                            case R.id.mn_30s:
                                selectedTimeInterval = 30 * 1000;
                                tvTimeValue.setText(R.string.time_value_30s);
                                break;
                            case R.id.mn_1m:
                                selectedTimeInterval = 60 * 1000;
                                tvTimeValue.setText(R.string.time_value_1m);
                                break;
                            case R.id.mn_5m:
                                selectedTimeInterval = 5 * 60 * 1000;
                                tvTimeValue.setText(R.string.time_value_5m);
                                break;
                            case R.id.mn_custom:

                                DateTimePickerDialog dtd = new DateTimePickerDialog(getContext(), null,
                                        new DateTimePickerDialog.OnDateTimeSetListener() {
                                            @Override
                                            public void OnDateTimeSet(Calendar calendar) {
                                                String value = Converter.calendar2String(calendar, "dd/MM/yyyy HH:mm");
                                                tvTimeValue.setText(value);
                                                selectedTimeInterval = -1;
                                                calendarTimePicked = calendar.getTimeInMillis();
                                            }
                                        });
                                dtd.show();

                                break;
                        }
                        return false;
                    }
                });
    }

    @OnClick(R.id.btnShowMore)
    void onBtnMore() {
        boolean visible = lyMoreSettings.getVisibility() == View.VISIBLE;
        lyMoreSettings.setVisibility(visible ? View.GONE : View.VISIBLE);
        ivExpandSetting.setImageResource(visible ? R.drawable.ic_action_expand : R.drawable.ic_action_collapse);
        tvMore.setText(visible ? R.string.lb_show_more : R.string.lb_hide_more);
    }

    @OnClick(R.id.btnPhoneUI)
    void onBtnPhoneUI() {
        Intent intent = new Intent(getContext(), PhoneUISelectorActivity.class);
        startActivityForResult(intent, REQUEST_PHONE_UI);
    }

    @OnClick(R.id.btnSave)
    void onSaveCall() {
        Toast.makeText(getContext(), "New call has been planned", Toast.LENGTH_SHORT).show();

        if (selectedTimeInterval != -1) {
            mCall.setTime(System.currentTimeMillis() + selectedTimeInterval);
        } else {
            mCall.setTime(calendarTimePicked);
        }

        AlarmHelper.getInstance(getContext()).placeCall(mCall);
    }

    @OnClick(R.id.btnCancel)
    void onCancelCall(){
        AlarmHelper.getInstance(getContext()).cancelCall(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_PHONE_UI:
                if (data == null) return;
                PhoneUI pui = (PhoneUI) data.getSerializableExtra(PhoneUI.KEY);
                tvPhoneUIValue.setText(pui.getName());
                mCall.setPhoneUI(pui);

                break;
            case REQUEST_PICK_CONTACT:
                if (data == null) return;
                try {
                    Uri contactData = data.getData();

                    ContentResolver cs = getContext().getContentResolver();

                    String[] projections = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER};

                    Cursor cursor = cs.query(contactData, projections, null, null, null);

                    if (cursor == null) return;
                    cursor.moveToFirst();

                    String name = cursor.getString(0);
                    String phoneNo = cursor.getString(1);

                    edtCallerName.setText(name);
                    edtCallerNumber.setText(phoneNo);

                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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
