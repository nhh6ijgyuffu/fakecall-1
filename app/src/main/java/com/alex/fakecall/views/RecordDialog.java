package com.alex.fakecall.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import com.alex.fakecall.GlobalVariables;
import com.alex.fakecall.R;
import com.alex.fakecall.helper.AudioHelper;
import com.alex.fakecall.helper.Converter;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecordDialog extends AlertDialog implements DialogInterface.OnClickListener,
        DialogInterface.OnDismissListener {
    @BindView(R.id.recordTimer)
    Chronometer recordTimer;

    private OnRecordCompletedCallback mCallback;
    private File mRecordedFile;
    private boolean mPositivePressed;

    public interface OnRecordCompletedCallback {
        void onCompleted(File file);
    }

    public RecordDialog(Context context, OnRecordCompletedCallback c) {
        super(context);
        View view = View.inflate(context, R.layout.record_dialog, null);
        setView(view);
        ButterKnife.bind(this, view);

        mCallback = c;

        String suffix = Converter.calendar2String(Calendar.getInstance(), "yyyMMddHHmmss");
        String fName = String.format("rc_%s.amr", suffix);
        setTitle(fName);

        mRecordedFile = new File(GlobalVariables.VOICE_FOLDER + "/" + fName);

        setButton(BUTTON_POSITIVE, "Save", this);
        setButton(BUTTON_NEGATIVE, "Cancel", this);

        setOnDismissListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AudioHelper.getInstance().startRecording(mRecordedFile);
        recordTimer.start();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        AudioHelper.getInstance().stopRecording();
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mPositivePressed = true;
            if (mCallback != null)
                mCallback.onCompleted(mRecordedFile);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            mPositivePressed = false;
        }
    }

    private void deleteFile() {
        if (mRecordedFile == null) return;
        if (mRecordedFile.exists())
            if (mRecordedFile.delete()) {
                Log.e("RecordDialog", "Deleted :"
                        + mRecordedFile.getName() + " due to cancellation");
            }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!mPositivePressed) {
            deleteFile();
        }
    }
}
