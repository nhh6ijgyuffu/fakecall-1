package com.alex.fakecall.utils;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;

import com.alex.fakecall.FakeCallApp;
import com.alex.fakecall.models.Ringtone;
import com.alex.fakecall.themes.Android6xActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static Calendar stringToCalendar(String dateStr, String pattern) {
        Calendar calendar = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            Date date = simpleDateFormat.parse(dateStr);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static String calendarToString(Calendar calendar, String pattern) {
        String rs = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            rs = simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String millisToString(long millis, String pattern) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return Utils.calendarToString(c, pattern);
    }

    public static Bitmap decodeResource(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        } catch (Throwable t1) {
            t1.printStackTrace();
            try {
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            } catch (Throwable t2) {
                t2.printStackTrace();
                try {
                    options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
                } catch (Throwable t3) {
                    t3.printStackTrace();
                    bitmap = null;
                }
            }
        }
        return bitmap;
    }

    public static List<Ringtone> getAllRingtone() {
        RingtoneManager manager = new RingtoneManager(FakeCallApp.getInstance());
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();

        List<Ringtone> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String titleStr = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uriStr = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String idStr = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);

            Uri uri = Uri.parse(uriStr + "/" + idStr);
            list.add(new Ringtone(titleStr, uri));
        }
        cursor.close();
        return list;
    }

    public static long getDurationOfAudioFile(Uri uri) {
        MediaMetadataRetriever mm = new MediaMetadataRetriever();
        mm.setDataSource(FakeCallApp.getInstance(), uri);

        String durationLong = mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long totalSecs = Long.parseLong(durationLong);
        mm.release();

        return totalSecs;
    }

    public static Intent getCallingIntent(Context ctx, int themeId) {
        switch (themeId) {
            case 1:
                return new Intent(ctx, Android6xActivity.class);
            default:
                return null;
        }
    }
}
