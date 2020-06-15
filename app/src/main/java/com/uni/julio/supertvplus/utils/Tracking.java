package com.uni.julio.supertvplus.utils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.util.Log;

import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.listeners.MessageCallbackListener;
import com.uni.julio.supertvplus.listeners.StringRequestListener;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.utils.networing.WebConfig;
import com.uni.julio.supertvplus.view.LoadingActivity;
import com.uni.julio.supertvplus.view.MainActivity;
import com.uni.julio.supertvplus.view.SplashActivity;
import java.net.URLEncoder;

public class Tracking implements StringRequestListener, OnClickListener {
    private static Tracking mInstance;
    private String action = "IDLE";
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    /* access modifiers changed from: private */
    private boolean isTracking = false;
    private boolean sleeping = false;
    private Runnable trackingThread = new Runnable() {
        public void run() {
            Tracking.this.track();
            Tracking.this.handler.postDelayed(this, 60000);
        }
    };
    public void enableTrack(boolean isTracking){
        this.isTracking = isTracking;
    }
    public void enableSleep(boolean isSleeping){
        this.sleeping = isSleeping;
    }
    public boolean getSleep(){
        return sleeping;
    }
    public static Tracking getInstance() {
        if (mInstance == null) {
            mInstance = new Tracking();
        }
        return mInstance;
    }

    public void onStart() {
        this.isTracking = true;
        this.handler.removeCallbacks(trackingThread);
        this.handler.postDelayed(trackingThread,0);
    }

    public void track() {
        if(this.isTracking && LiveTvApplication.appContext != null && !(LiveTvApplication.appContext instanceof LoadingActivity || LiveTvApplication.appContext instanceof MainActivity  || LiveTvApplication.appContext instanceof SplashActivity)){
            try{
                String istv = "1"; //Device.treatAsBox ? "1":"0";
                String url = WebConfig.trackingURL.replace("{USER}", (LiveTvApplication.getUser().getName())).replace("{MOVIE}", (URLEncoder.encode(this.action, "UTF-8")).replace("+", "%20")).replace("{DEVICE_ID}",(LiveTvApplication.getUser().getDeviceId())).replace("{ISTV}",istv);
                NetManager.getInstance().makeStringRequest(url, this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public void onError() {
        Log.d("DNLS", "Tracking ERROR");
    }

    public void onCompleted(String response) {
        try {
            if(!response.contains("false") && !response.contains("Mantenimiento") && !response.equals("") && !(LiveTvApplication.appContext instanceof MainActivity)) {
                Dialogs.showCustomDialog(LiveTvApplication.appContext, "Atencion", response, new MessageCallbackListener() {
                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onAccept() {

                    }

                    @Override
                    public void onError() {

                    }
                });

            }
        } catch (Exception e) {
        }

    }

    public void onClick(DialogInterface dialog, int which) {
        //String url = WebConfig.deleteMessageURL.replace("{USER}", this.usr);
        //NetManager.getInstance().makeStringRequest(url, this);
    }
}
