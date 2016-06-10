package com.alex.fakecall.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.alex.fakecall.R;
import com.alex.fakecall.helper.AudioHelper;
import com.alex.fakecall.helper.DialogHelper;
import com.alex.fakecall.helper.Utils;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.models.AudioObj;
import com.alex.fakecall.views.RecordDialog;
import com.alex.fakecall.views.SwitchOption;
import com.alex.fakecall.views.TwoLineTextOption;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class MoreCallSettingsActivity extends BaseActivity {
    @BindView(R.id.optRingtone)
    TwoLineTextOption optRingtone;

    @BindView(R.id.optCallDuration)
    TwoLineTextOption optCallDuration;

    @BindView(R.id.optVoice)
    TwoLineTextOption optVoice;

    @BindView(R.id.optVibrate)
    SwitchOption optVibrate;

    private Call mCall;

    private List<AudioObj> listRingtone;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_optional_call_settings;
    }

    @Override
    protected void onSetUp() {
        mCall = getIntent().getParcelableExtra(Call.KEY);
        listRingtone = Utils.getAllRingtone();

        optRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseRingtone();
            }
        });

        optVibrate.setOnCheckedChangeListener(new SwitchOption.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CompoundButton switchView, boolean checked) {
                mCall.setVibrate(checked);
            }
        });

        optCallDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseCallDuration();
            }
        });

        optVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseVoice();
            }
        });

        displayCurrentSetting();
    }

    private void onChooseVoice() {
        DialogHelper.showPopupMenu(this, optVoice, R.menu.menu_choose_voice, Gravity.RIGHT,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final int id = item.getItemId();
                        switch (id) {
                            case R.id.action_record:
                                RecordDialog dialog = new RecordDialog(MoreCallSettingsActivity.this,
                                        new RecordDialog.OnRecordCompletedCallback() {
                                            @Override
                                            public void onCompleted(File file) {
                                                Log.e("A", file.getName());
                                            }
                                        });
                                dialog.show();
                                break;
                            case R.id.action_choose_file:
                                break;
                        }
                        return false;
                    }
                });
    }

    private void onChooseCallDuration() {
        DialogHelper.showPopupMenu(this, optCallDuration, R.menu.menu_select_call_duration,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final int id = item.getItemId();
                        switch (id) {
                            case R.id.action_15s:
                                mCall.setCallDuration(15 * 1000);
                                optCallDuration.setValue(R.string.lb_15s);
                                break;
                            case R.id.action_30s:
                                mCall.setCallDuration(30 * 1000);
                                optCallDuration.setValue(R.string.lb_30s);
                                break;
                            case R.id.action_1m:
                                mCall.setCallDuration(60 * 1000);
                                optCallDuration.setValue(R.string.lb_1min);
                                break;
                        }
                        return false;
                    }
                });
    }

    private void onChooseRingtone() {
        DialogHelper.createSingleChoiceDialog(this, getString(R.string.lb_choose_ringtone), listRingtone, getPositionRingtone(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lv = ((AlertDialog) dialog).getListView();
                        int selectedPos = lv.getCheckedItemPosition();

                        AudioObj rt = listRingtone.get(selectedPos);

                        if (which != DialogInterface.BUTTON_POSITIVE) {
                            AudioHelper.getInstance().startPlaying(rt.getUri(), false);
                        } else {
                            optRingtone.setValue(rt.getName());
                            mCall.setRingtone(rt.getUri());
                        }
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AudioHelper.getInstance().stopPlaying();
                    }
                });
    }

    private int getPositionRingtone() {
        for (int i = 0; i < listRingtone.size(); i++) {
            Uri crUri = listRingtone.get(i).getUri();
            if (crUri.equals(mCall.getRingtone())) {
                return i;
            }
        }
        return -1;
    }

    private void displayCurrentSetting() {
        boolean isVibrate = mCall.isVibrate();
        optVibrate.setChecked(isVibrate);

        int t = getPositionRingtone();
        if (t != -1) {
            optRingtone.setValue(listRingtone.get(t).getName());
        }

        long duration = mCall.getCallDuration();
        if (duration == 0) {
            optCallDuration.setValue(R.string.lb_not_set);
        } else if (duration == 15 * 1000) {
            optCallDuration.setValue(R.string.lb_15s);
        } else if (duration == 30 * 1000) {
            optCallDuration.setValue(R.string.lb_30s);
        } else if (duration == 60 * 1000) {
            optCallDuration.setValue(R.string.lb_1min);
        }


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Call.KEY, mCall);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onCleanUp() {

    }
}
