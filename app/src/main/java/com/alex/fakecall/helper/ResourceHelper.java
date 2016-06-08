package com.alex.fakecall.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceHelper {
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

}
