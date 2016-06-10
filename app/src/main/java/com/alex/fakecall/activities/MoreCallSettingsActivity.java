package com.alex.fakecall.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.alex.fakecall.R;
import com.alex.fakecall.helper.DialogHelper;
import com.alex.fakecall.helper.RingtoneHelper;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.views.SwitchOption;
import com.alex.fakecall.views.TwoLineTextOption;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreCallSettingsActivity extends BaseActivity {
    @BindView(R.id.optRingtone)
    TwoLineTextOption optRingtone;

    @BindView(R.id.optVibrate)
    SwitchOption optVibrate;

    private Call mCall;

    private List<RingtoneHelper.Ringtone> listRingtone;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_optional_call_settings;
    }

    @Override
    protected void onSetUp() {
        mCall = (Call) getIntent().getSerializableExtra(Call.KEY);
        listRingtone = RingtoneHelper.getInstance().getAllRingtone();

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

        displayCurrentSetting();
    }


    private void onChooseRingtone() {
        DialogHelper.showSingleChoiceDialog(this, getString(R.string.lb_ringtones), listRingtone, getPositionRingtone(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            return;
                        }

                        ListView lv = ((AlertDialog) dialog).getListView();
                        int selectedPos = lv.getCheckedItemPosition();
                        RingtoneHelper.Ringtone ringTone = listRingtone.get(selectedPos);

                        if (which != DialogInterface.BUTTON_POSITIVE) {
                            RingtoneHelper.getInstance().playRingtone(ringTone.uriStr, false);

                        } else {
                            RingtoneHelper.getInstance().stopRingtone();
                            optRingtone.setValue(ringTone.name);
                            mCall.setRingtoneStr(ringTone.uriStr);
                        }
                    }
                });
    }


    private int getPositionRingtone() {
        if (mCall.getRingtoneStr() == null) return -1;
        for (int i = 1; i < listRingtone.size(); i++) {
            RingtoneHelper.Ringtone r = listRingtone.get(i);
            if (r.uriStr.equalsIgnoreCase(mCall.getRingtoneStr())) {
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
            optRingtone.setValue(listRingtone.get(t).name);
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
