package com.uni.julio.supertv.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.listeners.DialogListener;
import com.uni.julio.supertv.listeners.MessageCallbackListener;

public class Dialogs {

    

    

    public static void showOneButtonDialog(Context activity, int title, int message) {
        showOneButtonDialog(activity, activity.getString(title),activity.getString(message), null);
    }
    public static void showCustomDialog(Context activity, String title, String message, final MessageCallbackListener messageCallbackListener){
        final MaterialDialog dialog=new MaterialDialog.Builder(activity)
                .customView(R.layout.castloadingdialog,false)
                .contentLineSpacing(0)
                .theme(Theme.LIGHT)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        messageCallbackListener.onAccept();
                    }
                })
                .backgroundColor(activity.getResources().getColor(R.color.white))
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        messageCallbackListener.onDismiss();
                    }
                })
                .show();
        TextView titleView= dialog.getCustomView().findViewById(R.id.title);
        TextView contentView= dialog.getCustomView().findViewById(R.id.content);
        titleView.setText(title);
        contentView.setText(message);
        TextView cancel = dialog.getCustomView().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
   
    public static void showCustomDialog(Context activity, int title, String message, final MessageCallbackListener messageCallbackListener){
        final MaterialDialog dialog=new MaterialDialog.Builder(activity)
                .customView(R.layout.castloadingdialog,false)
                .contentLineSpacing(0)
                .theme(Theme.LIGHT)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        messageCallbackListener.onAccept();
                    }
                })
                .backgroundColor(activity.getResources().getColor(R.color.white))
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        messageCallbackListener.onDismiss();
                    }
                })
                .show();
        TextView titleView= dialog.getCustomView().findViewById(R.id.title);
        TextView contentView= dialog.getCustomView().findViewById(R.id.content);
        titleView.setText(title);
        contentView.setText(message);
        TextView cancel = dialog.getCustomView().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void showOneButtonDialog(Context activity, String message) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), message, null);
    }

    public static void showOneButtonDialog(Context activity, int message, DialogInterface.OnClickListener dialogListener) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), activity.getString(message), dialogListener);
    }

    public static void showOneButtonDialog(Context activity, int title, int message, DialogInterface.OnClickListener dialogListener) {
        showOneButtonDialog(activity, activity.getString(title), activity.getString(message), dialogListener);
    }

    public static void showOneButtonDialog(Context activity, String title, String message, final DialogInterface.OnClickListener dialogListener) {
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
                .backgroundColor(activity.getResources().getColor(R.color.white))
                .positiveColorRes(R.color.netflix_red)
                .titleColorRes(R.color.bg_general)
                .contentColorRes(R.color.bg_general)
                .show();

        View po=dialog.getActionButton(DialogAction.NEGATIVE);
        po.setBackground(activity.getResources().getDrawable(R.drawable.dialog_btn_background));
        po.setPadding(16,4,16,4);
     }

    public static void showTwoButtonsDialog(Context activity, int message, final DialogListener dialogListener) {
        showTwoButtonsDialog(activity, R.string.accept,R.string.cancel, message, dialogListener);
    }

    public static void showTwoButtonsDialog(Context activity, int accept, int cancel, int message, final DialogListener dialogListener) {
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
                .backgroundColor(activity.getResources().getColor(R.color.white))
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