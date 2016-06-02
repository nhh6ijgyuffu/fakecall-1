package com.alex.fakecall.utils;


import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

public class DialogUtils {

    public static void showPopupMenu(Context context, View anchor, int menuRes,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
        popupMenu.show();
    }
}
