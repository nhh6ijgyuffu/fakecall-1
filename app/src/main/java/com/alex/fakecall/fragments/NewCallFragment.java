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
import android.widget.ImageView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.ChooseThemeActivity;
import com.alex.fakecall.activities.MoreCallSettingActivity;
import com.alex.fakecall.activities.ScheduledActivity;
import com.alex.fakecall.appdata.SharedPrefController;
import com.alex.fakecall.controllers.AlarmController;
import com.alex.fakecall.controllers.DatabaseController;
import com.alex.fakecall.dialogs.DateTimePickerDialog;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.models.Theme;
import com.alex.fakecall.utils.DialogUtils;
import com.alex.fakecall.utils.SimpleTextWatcher;
import com.alex.fakecall.utils.Utils;
import com.alex.fakecall.views.TwoLineTextOption;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;


public class NewCallFragment extends BaseFragment {
    private Call mCall;

    @BindView(R.id.edtCallerName)
    EditText edtCallerName;

    @BindView(R.id.edtCallerNumber)
    EditText edtCallerNumber;

    @BindView(R.id.optTime)
    TwoLineTextOption optTime;

    @BindView(R.id.optTheme)
    TwoLineTextOption optTheme;

    @BindView(R.id.callerPhoto)
    ImageView callerPhoto;

    private static final int REQUEST_THEME = 1;
    private static final int REQUEST_PICK_CONTACT = 2;
    private static final int REQUEST_MORE_SETTINGS = 3;

    private long tmpSelectedTime;
    private boolean isCalendar;

    private boolean isEdit;

    public static NewCallFragment newInstance(Call call, boolean isEdit) {
        NewCallFragment frag = new NewCallFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEdit", isEdit);
        args.putParcelable(Call.TAG, call);
        frag.setArguments(args);
        return frag;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_new_call;
    }

    @Override
    protected void onSetUp() {
        mCall = getArguments().getParcelable(Call.TAG);
        if (mCall == null) {
            mCall = new Call();
        }
        mCall.setAlarmed(false);

        isEdit = getArguments().getBoolean("isEdit", false);

        displayCallInfo();

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

    private void displayCallInfo() {
        edtCallerName.setText(mCall.getName());
        edtCallerNumber.setText(mCall.getNumber());
        if (mCall.getTheme() != null)
            optTheme.setValue(mCall.getTheme().getName());
    }

    @OnClick(R.id.optMore)
    void openOptionalSettings() {
        Intent intent = new Intent(getContext(), MoreCallSettingActivity.class);
        intent.putExtra(Call.TAG, mCall);
        startActivityForResult(intent, REQUEST_MORE_SETTINGS);
    }

    @OnClick(R.id.callerPhoto)
    void onChangeCaller(View v) {
        DialogUtils.showPopupMenu(getContext(), v, R.menu.menu_change_caller, Gravity.LEFT, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int id = item.getItemId();
                switch (id) {
                    case R.id.action_private:
                        edtCallerName.setText(R.string.lb_private_num);
                        edtCallerNumber.setText("");
                        break;
                    case R.id.action_open_contact:
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

    @OnClick(R.id.optTime)
    void onSelectTime(View v) {
        DialogUtils.showPopupMenu(getContext(), v, R.menu.menu_select_time, Gravity.RIGHT,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_now:
                                isCalendar = false;
                                tmpSelectedTime = 0;
                                optTime.setValue(R.string.default_time_value);
                                break;
                            case R.id.action_15s:
                                isCalendar = false;
                                tmpSelectedTime = 15 * 1000;
                                optTime.setValue(R.string.time_value_15s);
                                break;
                            case R.id.action_30s:
                                isCalendar = false;
                                tmpSelectedTime = 30 * 1000;
                                optTime.setValue(R.string.time_value_30s);
                                break;
                            case R.id.action_1m:
                                isCalendar = false;
                                tmpSelectedTime = 60 * 1000;
                                optTime.setValue(R.string.time_value_1m);
                                break;
                            case R.id.action_5m:
                                isCalendar = false;
                                tmpSelectedTime = 5 * 60 * 1000;
                                optTime.setValue(R.string.time_value_5m);
                                break;
                            case R.id.action_custom:
                                DateTimePickerDialog dtd = new DateTimePickerDialog(getContext(), null,
                                        new DateTimePickerDialog.OnDateTimeSetListener() {
                                            @Override
                                            public void OnDateTimeSet(Calendar calendar) {
                                                String value = Utils.calendarToString(calendar, "dd/MM/yyyy HH:mm");
                                                isCalendar = true;
                                                optTime.setValue(value);
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

    @OnClick(R.id.optTheme)
    void onChooseTheme() {
        Intent intent = new Intent(getContext(), ChooseThemeActivity.class);
        intent.putExtra(Theme.TAG, mCall.getTheme());
        startActivityForResult(intent, REQUEST_THEME);
    }

    @OnClick(R.id.btnSave)
    void onSaveCall() {
        if (isCalendar) {
            mCall.setTime(tmpSelectedTime);
        } else {
            mCall.setTime(System.currentTimeMillis() + tmpSelectedTime);
        }

        if (isEdit) {
            int num = DatabaseController.getInstance().updateCall(mCall);
            if (num == 1) {
                long id = mCall.getId();
                AlarmController.getInstance().cancelCall((int) id);
                AlarmController.getInstance().scheduleCall((int) id, mCall);
                getActivity().finish();
            }
        } else {
            long id = DatabaseController.getInstance().addCall(mCall);
            if (id != -1) {
                mCall.setId(id);
                AlarmController.getInstance().scheduleCall((int) id, mCall);
                Intent intent = new Intent(getContext(), ScheduledActivity.class);
                startActivity(intent);
            }
            SharedPrefController.getInstance().saveObject(SharedPrefController.KEY_LAST_CALL, mCall);
        }
    }

    @OnClick(R.id.btnPreview)
    void onPreviewCall() {
        Intent intent = Utils.getCallingIntent(getContext(), mCall.getTheme().getId());
        if (intent == null) return;
        intent.putExtra("isPreview", true);
        intent.putExtra(Call.TAG, mCall);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_PICK_CONTACT:
                try {
                    Uri contactData = data.getData();

                    String[] projections = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};

                    Cursor cursor = getContext().getContentResolver()
                            .query(contactData, projections, null, null, null);

                    if (cursor == null) return;

                    if (cursor.moveToFirst()) {
                        String name = cursor.getString(0);
                        String phoneNo = cursor.getString(1);
                        String photoUri = cursor.getString(2);

                        edtCallerName.setText(name);
                        edtCallerNumber.setText(phoneNo);

                        Uri uri = Uri.parse(photoUri);
                        callerPhoto.setImageURI(uri);
                        mCall.setPhotoUri(uri.toString());
                    }
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_THEME:
                Theme theme = data.getParcelableExtra(Theme.TAG);
                if (theme != null) {
                    optTheme.setValue(theme.getName());
                    mCall.setTheme(theme);
                }
                break;
            case REQUEST_MORE_SETTINGS:
                mCall = data.getParcelableExtra(Call.TAG);
                break;
        }
    }
}
