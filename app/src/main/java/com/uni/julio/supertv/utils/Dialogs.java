package com.uni.julio.supertv.utils;


import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.listeners.DialogListener;

public class Dialogs {

     public static void showToast(int messageId) {
        Toast.makeText(LiveTvApplication.getAppContext(), messageId, Toast.LENGTH_LONG).show();
    }

    public static void showToast(String message) {
        Toast.makeText(LiveTvApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showOneButtonDialog(AppCompatActivity activity, int message) {
        showOneButtonDialog(activity, activity.getString(R.string.attention),activity.getString(message), null);
    }

    public static void showOneButtonDialog(AppCompatActivity activity, int title, int message) {
        showOneButtonDialog(activity, activity.getString(title),activity.getString(message), null);
    }

    public static void showOneButtonDialog(AppCompatActivity activity, String message) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), message, null);
    }

    public static void showOneButtonDialog(AppCompatActivity activity, int message, DialogInterface.OnClickListener dialogListener) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), activity.getString(message), dialogListener);
    }

    public static void showOneButtonDialog(AppCompatActivity activity, int title, int message, DialogInterface.OnClickListener dialogListener) {
        showOneButtonDialog(activity, activity.getString(title), activity.getString(message), dialogListener);
    }

    public static void showOneButtonDialog(AppCompatActivity activity, String title, String message, final DialogInterface.OnClickListener dialogListener) {
        MaterialDialog dialog=new MaterialDialog.Builder(activity)
                .title(title)
                .content(message)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(dialogListener!=null){
                            dialogListener.onClick(new DialogInterface() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void dismiss() {

                                }
                            },0);
                        }
                    }
                })
                .backgroundColor(activity.getBaseContext().getResources().getColor(R.color.white))
                .positiveColorRes(R.color.netflix_red)
                .titleColorRes(R.color.bg_general)
                .contentColorRes(R.color.bg_general)
                .show();

        View po=dialog.getActionButton(DialogAction.NEGATIVE);
        po.setBackground(activity.getResources().getDrawable(R.drawable.dialog_btn_background));
        po.setPadding(16,4,16,4);
     }

    public static void showTwoButtonsDialog(AppCompatActivity activity, int message, final DialogListener dialogListener) {
        showTwoButtonsDialog(activity, R.string.accept,R.string.cancel, message, dialogListener);
    }

    public static void showTwoButtonsDialog(AppCompatActivity activity, int accept, int cancel, int message, final DialogListener dialogListener) {
        MaterialDialog dialg=new MaterialDialog.Builder(activity)
                .content(message)
                .positiveText(accept)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogListener.onAccept();
                    }
                })
                .theme(Theme.LIGHT)
                .negativeText(cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogListener.onCancel();
                    }
                })
                .backgroundColor(activity.getBaseContext().getResources().getColor(R.color.white))
                .positiveColorRes(R.color.netflix_red)
                .negativeColorRes(R.color.netflix_red)
                .contentColorRes(R.color.bg_general)
                .show();
        View ne=dialg.getActionButton(DialogAction.POSITIVE);
        View po=dialg.getActionButton(DialogAction.NEGATIVE);
        ne.setBackground(activity.getResources().getDrawable(R.drawable.dialog_btn_background));
        po.setBackground(activity.getResources().getDrawable(R.drawable.dialog_btn_background));
        ne.setPadding(16,4,16,4);
        po.setPadding(16,4,16,4);
        po.requestFocus();
    }
}