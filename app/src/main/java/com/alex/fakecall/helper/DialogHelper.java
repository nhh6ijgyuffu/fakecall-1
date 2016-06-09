package com.alex.fakecall.helper;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.MenuRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.alex.fakecall.R;

import java.util.List;

public class DialogHelper {

    public static <T> void showSingleChoiceDialog(Context context, CharSequence title, List<T> list, int checkedPos,
                                                  DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ArrayAdapter<T> arrayAdapter = new ArrayAdapter<>(context, R.layout.single_list_dialog_item, list);
        builder.setTitle(title);
        builder.setPositiveButton("OK", onClickListener);
        builder.setNegativeButton("Cancel", onClickListener);
        builder.setSingleChoiceItems(arrayAdapter, checkedPos, onClickListener);
        builder.show();
    }

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
