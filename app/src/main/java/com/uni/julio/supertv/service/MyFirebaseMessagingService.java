package com.uni.julio.supertv.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.utils.ObjectUtils;
import com.uni.julio.supertv.view.MainActivity;
import com.uni.julio.supertv.view.MoviesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager mNotificationManager;
    private int notKey = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        if(!ObjectUtils.isEmpty(from)){
            Map<String, String> dataMap= remoteMessage.getData();
                if (dataMap.size() > 0) {
                    try {
                        Log.d("asdf",dataMap.get("data"));
                        JSONArray jsonArray = new JSONArray(dataMap.get("data"));
                        String title = remoteMessage.getNotification().getTitle();
                        String body = remoteMessage.getNotification().getBody();

                        String content= "\r\n";
                        for(int i=0; i<jsonArray.length();i++){
                            int category = jsonArray.getJSONArray(i).getInt(16);
                            String section = buildSecction(category);
                            content += jsonArray.getJSONArray(i).getString(2)+" In"+section+" Section\r\n";
                        }
                        content += "Tap to check\r\n";
                        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        createNotificationChannel();
                        final int notificationID = notKey++;
                        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"channel_id");

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Following Contents Are newly added.")
                                .setContentText(content)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(content)).setContentText("Please check!")
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                .setAutoCancel(true)
                                .setColor(0x00000000)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        mNotificationManager.notify(notificationID, mBuilder.build());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }


            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }

    }
    private String buildSecction(int category){
        switch (category){
            case 1:
                return "Movie";
            case 2:
                return "Serie";
            case 3:
                return "Kids";
            case 4:
                return "Eventos";
            case 5:
                return "KaraOK";
                default:
                    return "Unknown";
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_Name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
}
