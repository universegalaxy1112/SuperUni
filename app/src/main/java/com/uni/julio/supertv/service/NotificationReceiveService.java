package com.uni.julio.supertv.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
public class NotificationReceiveService extends Service {

    public static final String NOTIFICATION_CANCEL = "com.heavykiwi.notification.cancel";
    public static final String NOTIFICATION_REPLY = "com.heavykiwi.notification.reply";


    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {


            return START_STICKY;
        }

        return 2;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
