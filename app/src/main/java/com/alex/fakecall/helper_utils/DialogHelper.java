package com.alex.fakecall.helper_utils;


import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.View;

public class DialogHelper {

    public static void showPopupMenu(Context context, View anchor, int menuRes,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        android.support.v7.widget.PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
        popupMenu.show();
    }
}
