package com.alex.fakecall.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.alex.fakecall.R;
import com.alex.fakecall.controllers.AudioController;
import com.alex.fakecall.controllers.AudioController.PlayerTag;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.models.Ringtone;
import com.alex.fakecall.utils.DialogUtils;
import com.alex.fakecall.utils.Utils;
import com.alex.fakecall.views.SwitchOption;
import com.alex.fakecall.views.TwoLineTextOption;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreCallSettingActivity extends BaseActivity {
    @BindView(R.id.optRingtone)
    TwoLineTextOption optRingtone;

    @BindView(R.id.optCallDuration)
    TwoLineTextOption optCallDuration;

    @BindView(R.id.optVoice)
    TwoLineTextOption optVoice;

    @BindView(R.id.optVibrate)
    SwitchOption optVibrate;

    private Call mCall;

    private List<Ringtone> listRingtone;

    private static final int REQUEST_CHOOSE_VOICE = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_more_call_settings;
    }

    @Override
    protected void onSetUp() {
        mCall = getIntent().getParcelableExtra(Call.TAG);
        listRingtone = Utils.getAllRingtone();
        displayCurrentSetting();

        optVibrate.setOnCheckedChangeListener(new SwitchOption.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CompoundButton switchView, boolean checked) {
                mCall.setVibrate(checked);
            }
        });
    }

    private void displayCurrentSetting() {
        optVibrate.setChecked(mCall.isVibrate());

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

        if (mCall.getVoiceUri() != null) {
            Uri uri = Uri.parse(mCall.getVoiceUri());
            optVoice.setValue(uri.getLastPathSegment());
        }
    }

    @OnClick(R.id.optVoice)
    protected void onChooseVoice() {
        Intent intent = new Intent(this, ChooseVoiceActivity.class);
        intent.putExtra("mUri", mCall.getVoiceUri());
        startActivityForResult(intent, REQUEST_CHOOSE_VOICE);
    }

    @OnClick(R.id.optCallDuration)
    protected void onChooseCallDuration() {
        DialogUtils.showPopupMenu(this, optCallDuration, R.menu.menu_select_call_duration,
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

    @OnClick(R.id.optRingtone)
    protected void onChooseRingtone() {
        DialogUtils.createSingleChoiceDialog(this, getString(R.string.lb_choose_ringtone), listRingtone, getPositionRingtone(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lv = ((AlertDialog) dialog).getListView();
                        int selectedPos = lv.getCheckedItemPosition();

                        Ringtone rt = listRingtone.get(selectedPos);

                        if (which != DialogInterface.BUTTON_POSITIVE) {
                            AudioController.getInstance().startPlaying(PlayerTag.RINGTONE,
                                    rt.getUri(), false);
                        } else {
                            optRingtone.setValue(rt.getName());
                            mCall.setRingtoneUri(rt.getUri().toString());
                        }
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AudioController.getInstance().stopPlaying(PlayerTag.RINGTONE);
                    }
                });
    }

    private int getPositionRingtone() {
        for (int i = 0; i < listRingtone.size(); i++) {
            String crUri = listRingtone.get(i).getUri().toString();
            if (crUri.equals(mCall.getRingtoneUri())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_VOICE:
                    String selectedUri = data.getStringExtra("mUri");
                    if (selectedUri != null) {
                        mCall.setVoiceUri(selectedUri);
                        Uri uri = Uri.parse(selectedUri);
                        optVoice.setValue(uri.getLastPathSegment());
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Call.TAG, mCall);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
