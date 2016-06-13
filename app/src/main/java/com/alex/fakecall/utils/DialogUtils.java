package com.alex.fakecall.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.MenuRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.alex.fakecall.R;

import java.util.List;

public class DialogUtils {

    public static <T> Dialog createSingleChoiceDialog(Context context, String title, List<T> list, int checkedPos,
                                                      DialogInterface.OnClickListener onClickListener, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ArrayAdapter<T> arrayAdapter = new ArrayAdapter<>(context, R.layout.single_choice_dialog_item, list);
        builder.setTitle(title);
        builder.setPositiveButton(context.getString(R.string.lb_ok), onClickListener);
        builder.setNegativeButton(context.getString(R.string.lb_cancel), null);
        builder.setSingleChoiceItems(arrayAdapter, checkedPos, onClickListener);
        builder.setOnDismissListener(dismissListener);
        builder.show();
        return builder.create();
    }

    public static void showConfirmationDialog(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(context.getString(R.string.lb_ok), listener);
        builder.setNegativeButton(context.getString(R.string.lb_cancel), listener);
        builder.show();
    }

    public static void showPopupMenu(Context context, View anchor, @MenuRes int menuRes,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        DialogUtils.showPopupMenu(context, anchor, menuRes, Gravity.NO_GRAVITY, menuItemClickListener);
    }

    public static void showPopupMenu(Context context, View anchor, @MenuRes int menuRes, int gravity,
                                     PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        PopupMenu popupMenu = new PopupMenu(context, anchor, gravity);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
        popupMenu.show();
    }
}
