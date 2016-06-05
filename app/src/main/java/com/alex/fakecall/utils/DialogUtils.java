package com.alex.fakecall.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

public class DialogUtils {

    @TargetApi(14)
    public static void showPopupMenu(Context context, View anchor, int menuRes,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
        popupMenu.show();
    }
}
