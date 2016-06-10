package com.alex.fakecall.helper;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;

import com.alex.fakecall.App;
import com.alex.fakecall.models.AudioObj;

import java.util.ArrayList;
import java.util.List;

public class Utils {
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

    public static List<AudioObj> getAllRingtone() {
        RingtoneManager manager = new RingtoneManager(App.getInstance());
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();

        List<AudioObj> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String titleStr = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uriStr = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String idStr = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);

            Uri uri = Uri.parse(uriStr + "/" + idStr);
            list.add(new AudioObj(titleStr, uri));
        }
        cursor.close();
        return list;
    }

    public static String getDurationOfAudioFile(Uri uri) {
        MediaMetadataRetriever mm = new MediaMetadataRetriever();
        mm.setDataSource(App.getInstance(), uri);

        String durationLong = mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long l = Long.parseLong(durationLong);

        mm.release();
        return dur;
    }

}
