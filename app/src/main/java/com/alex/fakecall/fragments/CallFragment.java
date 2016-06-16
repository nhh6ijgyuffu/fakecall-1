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
import com.alex.fakecall.controllers.AlarmController;
import com.alex.fakecall.data.AppDB;
import com.alex.fakecall.data.AppPrefs;
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


public class CallFragment extends BaseFragment {
    private Call mCall;

    @BindView(R.id.edtCallerName)
    EditText edtCallerName;

    @BindView(R.id.edtCallerNumber)
    EditText edtCallerNumber;

    @BindView(R.id.optTime)
    TwoLineTextOption optTime;

    @BindView(R.id.optTheme)
    TwoLineTextOption optTheme;

    @BindView(R.id.btnPhoto)
    ImageView callerPhoto;

    private static final int REQUEST_THEME = 1;
    private static final int REQUEST_PICK_CONTACT = 2;
    private static final int REQUEST_MORE_SETTINGS = 3;

    private long customCallTime;
    private boolean isEdit;

    private String[] time_call_value;

    public static CallFragment newInstance(Call call, boolean isEdit) {
        CallFragment frag = new CallFragment();
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
        isEdit = getArguments().getBoolean("isEdit", false);

        time_call_value = getResources().getStringArray(R.array.time_call_value);

        if (mCall == null) {
            mCall = new Call();
        }
        mCall.setAlarmed(false);

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

        displayCallInfo();
    }

    private void displayCallInfo() {
        edtCallerName.setText(mCall.getName());
        edtCallerNumber.setText(mCall.getNumber());
        Theme selectedTheme = Utils.getThemeById(mCall.getThemeId());
        if (selectedTheme != null) {
            optTheme.setValue(selectedTheme.getName());
        }

        if (mCall.getSelectedTimeOpt() == 5) {
            String selectedTime = Utils.millisToString(mCall.getTime(), getString(R.string.full_date_time_pattern));
            optTime.setValue(selectedTime);
        } else {
            optTime.setValue(time_call_value[mCall.getSelectedTimeOpt()]);
        }

        if (mCall.isPrivate()) {
            edtCallerName.setText(R.string.mn_opt_private_num);
            edtCallerNumber.setText("");
            edtCallerName.setEnabled(false);
            edtCallerNumber.setEnabled(false);
        }

        if (mCall.getPhotoUri() != null) {
            callerPhoto.setImageURI(Uri.parse(mCall.getPhotoUri()));
        }
    }

    @OnClick(R.id.optMore)
    void openOptionalSettings() {
        Intent intent = new Intent(getContext(), MoreCallSettingActivity.class);
        intent.putExtra(Call.TAG, mCall);
        startActivityForResult(intent, REQUEST_MORE_SETTINGS);
    }

    @OnClick(R.id.btnContact)
    void onChangeContact(View v) {
        DialogUtils.showPopupMenu(getContext(), v, R.array.choose_contact, Gravity.LEFT, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int id = item.getItemId();
                switch (id) {
                    case 0:
                        edtCallerName.setText(R.string.mn_opt_private_num);
                        edtCallerNumber.setText("");
                        edtCallerName.setEnabled(false);
                        edtCallerNumber.setEnabled(false);
                        mCall.setPrivate(true);
                        break;
                    case 1:
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
        DialogUtils.showPopupMenu(getContext(), v, R.array.select_time_option, Gravity.RIGHT,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case 0:
                                mCall.setSelectedTimeOpt(0);
                                optTime.setValue(R.string.time_value_now);
                                break;
                            case 1:
                                mCall.setSelectedTimeOpt(1);
                                optTime.setValue(R.string.time_value_15s);
                                break;
                            case 2:
                                mCall.setSelectedTimeOpt(2);
                                optTime.setValue(R.string.time_value_30s);
                                break;
                            case 3:
                                mCall.setSelectedTimeOpt(3);
                                optTime.setValue(R.string.time_value_1m);
                                break;
                            case 4:
                                mCall.setSelectedTimeOpt(4);
                                optTime.setValue(R.string.time_value_5m);
                                break;
                            case 5:
                                DateTimePickerDialog dtd = new DateTimePickerDialog(getContext(), null,
                                        new DateTimePickerDialog.OnDateTimeSetListener() {
                                            @Override
                                            public void OnDateTimeSet(Calendar calendar) {
                                                mCall.setSelectedTimeOpt(5);
                                                customCallTime = calendar.getTimeInMillis();
                                                String value = Utils.calendarToString(calendar,
                                                        getString(R.string.full_date_time_pattern));
                                                optTime.setValue(value);
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
        intent.putExtra("themeId", mCall.getThemeId());
        startActivityForResult(intent, REQUEST_THEME);
    }

    @OnClick(R.id.btnSave)
    void onSaveCall() {
        long timeToSet = mCall.getTime();
        if (mCall.getSelectedTimeOpt() == 0) {
            timeToSet = System.currentTimeMillis();
        } else if (mCall.getSelectedTimeOpt() == 1) {
            timeToSet = System.currentTimeMillis() + 15000;
        } else if (mCall.getSelectedTimeOpt() == 2) {
            timeToSet = System.currentTimeMillis() + 30000;
        } else if (mCall.getSelectedTimeOpt() == 3) {
            timeToSet = System.currentTimeMillis() + 60000;
        } else if (mCall.getSelectedTimeOpt() == 4) {
            timeToSet = System.currentTimeMillis() + 300000;
        } else if (mCall.getSelectedTimeOpt() == 5) {
            if (customCallTime != 0) {
                timeToSet = customCallTime;
            }
        }
        mCall.setTime(timeToSet);

        if (isEdit) {
            int num = AppDB.getInstance().updateCall(mCall);
            if (num == 1) {
                long id = mCall.getId();
                AlarmController.getInstance().cancelCall((int) id);
                AlarmController.getInstance().scheduleCall((int) id, mCall);
                getActivity().finish();
            }
        } else {
            long id = AppDB.getInstance().addCall(mCall);
            if (id != -1) {
                mCall.setId(id);
                AlarmController.getInstance().scheduleCall((int) id, mCall);
                Intent intent = new Intent(getContext(), ScheduledActivity.class);
                startActivity(intent);
            }
            AppPrefs.getInstance().saveObject(AppPrefs.KEY_LAST_CALL, mCall);
        }
    }

    @OnClick(R.id.btnPreview)
    void onPreviewCall() {
        Intent intent = Utils.getCallingIntent(getContext(), mCall.getThemeId());
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

                        mCall.setPrivate(false);
                        edtCallerName.setEnabled(true);
                        edtCallerNumber.setEnabled(true);
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
                    mCall.setThemeId(theme.getId());
                }
                break;
            case REQUEST_MORE_SETTINGS:
                mCall = data.getParcelableExtra(Call.TAG);
                break;
        }
    }
}
