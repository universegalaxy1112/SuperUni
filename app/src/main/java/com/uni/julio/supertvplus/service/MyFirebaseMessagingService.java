/*
package com.uni.julio.supertvplus.service;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.utils.IntentUtil;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager mNotificationManager;
    private int notKey = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
         return;
        */
/*if(!ObjectUtils.isEmpty(from)){
            Map<String, String> dataMap= remoteMessage.getData();
                if (dataMap.size() > 0) {
                    try {
                        String type = dataMap.get("type");
                        //String title = remoteMessage.getNotification().getTitle();
                        //String body = remoteMessage.getNotification().getBody();
                        String titleNotification = "";
                        String contentForTV = "";
                        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        if(type.equals("new_contents")){
                                JSONObject content = new JSONObject((dataMap.get("data")));
                                JSONArray main = content.getJSONArray("main");
                                JSONArray cap = content.getJSONArray("capital");
                                for(int i = 0; i<main.length();i++){
                                    titleNotification = "New content";
                                    String contentNotification = main.getJSONObject(i).getString("title") + " In " + buildSecction(main.getJSONObject(i).getInt("type"));
                                    contentForTV += contentNotification + "\r\n";
                                    String poster = "";
                                    if(main.getJSONObject(i).getString("poster").contains("https")){
                                        poster = main.getJSONObject(i).getString("poster");
                                    }else{
                                        poster = "https://supertvplus.com/eventos_posters/"+main.getJSONObject(i).getString("poster");
                                    }
                                    showNotification(titleNotification, contentNotification,poster,type,main.getJSONObject(i).getInt("type"));
                                }
                                for(int i =0 ; i< cap.length();i++){
                                     titleNotification = "New content";
                                    String contentNotification ="Episode "+ cap.getJSONObject(i).getString("title") + " In Season " + (cap.getJSONObject(i).getString("season")) + (cap.getJSONObject(i).getString("serie"));
                                    contentForTV += contentNotification + "\r\n";
                                    String poster = "";
                                    if(cap.getJSONObject(i).getString("poster").contains("https")){
                                        poster = cap.getJSONObject(i).getString("poster");
                                    }else{
                                        poster = "https://supertvplus.com/eventos_posters/"+cap.getJSONObject(i).getString("poster");
                                    }
                                        showNotification(titleNotification, contentNotification,poster,type,2);
                                }
                            }else{
                                 titleNotification = "Message from the dealer";
                                String  contentNotification =(dataMap.get("data"));
                                contentForTV = contentNotification;
                                showNotification(titleNotification, contentNotification,"https://",type,-1);
                            }

                    } catch (JSONException | NullPointerException e) {
                        JSONObject content = new JSONObject();
                        e.printStackTrace();
                    }
                }
        }*//*

    }
    private void showNotification(String title,String tickerText,final String posterImage,String type, int mainCategoryId){
        final int notificationID = new Random().nextInt(60000);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
         RemoteViews customSmallView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        int profileImg =  R.drawable.ic_launcher;
        customSmallView.setImageViewResource(R.id.profileImgViewer, profileImg);
        customSmallView.setTextViewText(R.id.titleViewer, title);
        customSmallView.setTextViewText(R.id.contentViewer,  tickerText );
       final RemoteViews customLargeView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        customLargeView.setImageViewResource(R.id.profileImgViewer, profileImg);
        customLargeView.setTextViewText(R.id.titleViewer, title);
        customLargeView.setTextViewText(R.id.contentViewer,   tickerText);
        ComponentName notificationService = new ComponentName(this, NotificationReceiveService.class);
        Intent cancelIntent = new Intent(NotificationReceiveService.NOTIFICATION_CANCEL);
        cancelIntent.putExtra(IntentUtil.TAG_NOTIFICATION_ID, notificationID);
        cancelIntent.setComponent(notificationService);
        customLargeView.setOnClickPendingIntent(R.id.poster, PendingIntent.getService(this, 0, cancelIntent, 0));
       final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"channel1");
        mBuilder.setSmallIcon(R.drawable.ic_launcher).setContentTitle(getString(R.string.app_name));
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setSubText("New Content");
        mBuilder.setTicker(tickerText);
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setAutoCancel(true);
        mBuilder.setColor(0x00000000);
        Intent replyIntent = new Intent(NotificationReceiveService.NOTIFICATION_OPEN);
        replyIntent.putExtra(IntentUtil.TAG_NOTIFICATION_ID, notificationID);
        replyIntent.setComponent(notificationService);
        replyIntent.putExtra("type",type);
        replyIntent.putExtra("mainCategoryId",mainCategoryId-1);
        customLargeView.setOnClickPendingIntent(R.id.poster, PendingIntent.getService(this, 0, replyIntent, 0));
        PendingIntent contentIntent = PendingIntent.getService(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setCustomContentView(customSmallView);
        mBuilder.setCustomBigContentView(customLargeView);
        mNotificationManager.notify(notificationID, mBuilder.build());
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Picasso.get()
                        .load(posterImage)
                        .into(customLargeView, R.id.poster, notificationID, mBuilder.build());

            }
        });
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
                return "Adultos";
            case 6:
                return "Live TV";
            case 7:
                return "Capitulo";
            case 8:
                return "Karaoke";
                default:
                    return "Unknown";
        }
    }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void setupChannels(){
            CharSequence adminChannelName = getString(R.string.channel_Name);
            String adminChannelDescription = getString(R.string.channel_description);
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel("channel1", adminChannelName, NotificationManager.IMPORTANCE_HIGH);
            adminChannel.setDescription(adminChannelDescription);
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.enableVibration(true);
            adminChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(adminChannel);
            }
        }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
}
*/
