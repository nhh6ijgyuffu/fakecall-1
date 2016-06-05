package com.alex.fakecall.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceUtils {
    public static Bitmap decodeResource(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        } catch (OutOfMemoryError o1) {
            o1.printStackTrace();
            try {
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            } catch (OutOfMemoryError o2) {
                o2.printStackTrace();
                try {
                    options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
                } catch (OutOfMemoryError o3) {
                    o3.printStackTrace();
                    bitmap = null;
                }
            }
        }
        return bitmap;
    }

}
