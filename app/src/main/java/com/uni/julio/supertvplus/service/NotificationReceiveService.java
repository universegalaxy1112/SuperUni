package com.uni.julio.supertvplus.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.uni.julio.supertvplus.listeners.NotificationListener;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.IntentUtil;
import com.uni.julio.supertvplus.view.SplashActivity;

public class NotificationReceiveService extends Service {

    public static final String NOTIFICATION_CANCEL = "com.uni.julio.supertv.cancel";
    public static final String NOTIFICATION_OPEN = "com.uni.julio.supertv.open";
    private static NotificationListener notificationListener;
    public static void setNotificationListener(NotificationListener listener){
        notificationListener = listener;
    }

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
            int NOTIFICATION_ID = intent.getIntExtra(IntentUtil.TAG_NOTIFICATION_ID, 0);
            mNotificationManager.cancel(NOTIFICATION_ID);
            if(intent.getAction().equals(NOTIFICATION_OPEN)){
                String type = intent.getStringExtra("type");
                if(type.equals("new_contents")){
                    int mainCategoryId = intent.getIntExtra("mainCategoryId",0);
                    if(mainCategoryId == 4 ) mainCategoryId = 6;
                    if(Connectivity.isForeground(this)){
                        if(notificationListener != null)
                            notificationListener.notificationClicked(type,mainCategoryId);
                    }else{
                        Intent replyIntent = new Intent(this, SplashActivity.class);
                        replyIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        replyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        replyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        replyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        replyIntent.putExtra("type", type);
                        replyIntent.putExtra("mainCategoryId",0);
                        startActivity(replyIntent);
                    }
                }

            }

        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
