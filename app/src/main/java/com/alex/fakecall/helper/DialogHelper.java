package com.alex.fakecall.helper;


import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.View;

public class DialogHelper {

    public static void showPopupMenu(Context context, View anchor, @MenuRes int menuRes,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        DialogHelper.showPopupMenu(context, anchor, menuRes, Gravity.NO_GRAVITY, menuItemClickListener);
    }

    public static void showPopupMenu(Context context, View anchor, @MenuRes int menuRes, int gravity,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        PopupMenu popupMenu = new PopupMenu(context, anchor, gravity);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
        popupMenu.show();
    }

    public static void showSnackbar(Context ctx, View anchor, String msg, String actionLabel,
                                    View.OnClickListener onActionListener) {
        Snackbar snackbar = Snackbar.make(anchor, msg, Snackbar.LENGTH_SHORT);
        snackbar.setAction(actionLabel, onActionListener);
        snackbar.show();
    }
}
