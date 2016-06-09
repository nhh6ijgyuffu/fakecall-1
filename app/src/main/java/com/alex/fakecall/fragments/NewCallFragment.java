package com.alex.fakecall.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.ScheduledActivity;
import com.alex.fakecall.activities.ThemeSelectActivity;
import com.alex.fakecall.helper.AlarmHelper;
import com.alex.fakecall.helper.Converter;
import com.alex.fakecall.helper.DatabaseHelper;
import com.alex.fakecall.helper.DialogHelper;
import com.alex.fakecall.helper.SimpleTextWatcher;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.models.Theme;
import com.alex.fakecall.views.DateTimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;


public class NewCallFragment extends BaseFragment {
    private Call mCall;

    @BindView(R.id.tvTimeValue)
    TextView tvTimeValue;

    @BindView(R.id.tvPhoneUIValue)
    TextView tvPhoneUIValue;

    @BindView(R.id.edtCallerName)
    EditText edtCallerName;

    @BindView(R.id.edtCallerNumber)
    EditText edtCallerNumber;

    private static final int REQUEST_PHONE_UI = 1;
    private static final int REQUEST_PICK_CONTACT = 2;

    private long tmpSelectedTime;
    private boolean isCalendar;
    private boolean isEdit;

    public static NewCallFragment newInstance(Call call, boolean isEdit) {
        NewCallFragment frag = new NewCallFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEdit", isEdit);
        args.putSerializable(Call.KEY, call);
        frag.setArguments(args);
        return frag;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_new_call;
    }

    @Override
    protected void onSetUp() {
        mCall = (Call) getArguments().getSerializable(Call.KEY);
        if (mCall == null) {
            mCall = DatabaseHelper.getInstance().getLastSavedCall();
        }
        isEdit = getArguments().getBoolean("isEdit", false);

        displayLastCall();

        edtCallerName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(String text) {
                mCall.setName(text);
            }
        });

        edtCallerNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(String text) {
                mCall.setNumber(text);
            }
        });
    }

    private void displayLastCall() {
        edtCallerName.setText(mCall.getName());
        edtCallerNumber.setText(mCall.getNumber());
    }

    @OnClick(R.id.callerPhoto)
    void onChangeCaller(View v) {
        DialogHelper.showPopupMenu(getContext(), v, R.menu.change_caller, new PopupMenu.OnMenuItemClickListener() {
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
        DialogHelper.showPopupMenu(getContext(), v, R.menu.select_time, Gravity.RIGHT,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.mn_now:
                                isCalendar = false;
                                tmpSelectedTime = 0;
                                tvTimeValue.setText(R.string.default_time_value);
                                break;
                            case R.id.mn_15s:
                                isCalendar = false;
                                tmpSelectedTime = 15 * 1000;
                                tvTimeValue.setText(R.string.time_value_15s);
                                break;
                            case R.id.mn_30s:
                                isCalendar = false;
                                tmpSelectedTime = 30 * 1000;
                                tvTimeValue.setText(R.string.time_value_30s);
                                break;
                            case R.id.mn_1m:
                                isCalendar = false;
                                tmpSelectedTime = 60 * 1000;
                                tvTimeValue.setText(R.string.time_value_1m);
                                break;
                            case R.id.mn_5m:
                                isCalendar = false;
                                tmpSelectedTime = 5 * 60 * 1000;
                                tvTimeValue.setText(R.string.time_value_5m);
                                break;
                            case R.id.mn_custom:
                                DateTimePickerDialog dtd = new DateTimePickerDialog(getContext(), null,
                                        new DateTimePickerDialog.OnDateTimeSetListener() {
                                            @Override
                                            public void OnDateTimeSet(Calendar calendar) {
                                                String value = Converter.calendar2String(calendar, "dd/MM/yyyy HH:mm");
                                                isCalendar = true;
                                                tvTimeValue.setText(value);
                                                tmpSelectedTime = calendar.getTimeInMillis();
                                            }
                                        });
                                dtd.show();
                                break;
                        }
                        return false;
                    }
                });
    }

    @OnClick(R.id.btnPhoneUI)
    void onBtnPhoneUI() {
        Intent intent = new Intent(getContext(), ThemeSelectActivity.class);
        startActivityForResult(intent, REQUEST_PHONE_UI);
    }

    @OnClick(R.id.btnSave)
    void onSaveCall() {
        long a = isCalendar ? tmpSelectedTime : System.currentTimeMillis() + tmpSelectedTime;
        mCall.setTime(a);

        Long id = DatabaseHelper.getInstance().addCall(mCall);

        if (id == -1) {
            return;
        }

        mCall.setId(id);

        if (isEdit) {
            AlarmHelper.getInstance().cancelCall(id);
        }

        AlarmHelper.getInstance().placeCall(mCall);

        if (isEdit) {
            getActivity().finish();
            return;
        }
        Intent intent = new Intent(getContext(), ScheduledActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_PHONE_UI:
                Theme pui = (Theme) data.getSerializableExtra(Theme.KEY);
                tvPhoneUIValue.setText(pui.getName());
                mCall.setPhoneUI(pui);
                break;
            case REQUEST_PICK_CONTACT:
                try {
                    Uri contactData = data.getData();

                    String[] projections = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER};

                    Cursor cursor = getContext().getContentResolver()
                            .query(contactData, projections, null, null, null);

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
}
