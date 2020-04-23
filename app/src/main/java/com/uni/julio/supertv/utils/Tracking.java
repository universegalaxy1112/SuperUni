package com.uni.julio.supertv.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.uni.julio.supertv.listeners.MessageCallbackListener;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.utils.networing.WebConfig;
import com.uni.julio.supertv.view.LoadingActivity;
import com.uni.julio.supertv.view.MainActivity;
import com.uni.julio.supertv.view.SplashActivity;
import com.uni.julio.supertv.view.VideoPlayActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Tracking implements StringRequestListener, OnClickListener {
    private static Context mContext = null;
    private static Tracking mInstance = null;
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
    private User usr = null;
    public void enableTrack(boolean isTracking){
        this.isTracking = isTracking;
    }
    public void enableSleep(boolean isSleeping){
        this.sleeping = isSleeping;
    }
    public boolean getSleep(){
        return sleeping;
    }
    public static Tracking getInstance(Context activity) {
        if (mInstance == null) {
            mInstance = new Tracking();
        }
        mContext = activity;
        return mInstance;
    }

    public void onStart() {
        String theUser = DataManager.getInstance().getString("theUser", "");
        if (!TextUtils.isEmpty(theUser))
            this.usr = ( new Gson().fromJson(theUser, User.class));
        this.isTracking = true;
        this.handler.removeCallbacks(trackingThread);
        this.handler.postDelayed(trackingThread,0);
    }

    /* access modifiers changed from: private */
    public void track() {
        if(this.isTracking && !(mContext instanceof LoadingActivity || mContext instanceof MainActivity  || mContext instanceof SplashActivity)){
            try{
                String ip =Device.ip;
                String istv = "1"; //Device.treatAsBox ? "1":"0";
                String theUser = DataManager.getInstance().getString("theUser", "");
                if (!TextUtils.isEmpty(theUser))
                    this.usr = ( new Gson().fromJson(theUser, User.class));
                String url = WebConfig.trackingURL.replace("{USER}", (this.usr.getName())).replace("{MOVIE}", (URLEncoder.encode(this.action, "UTF-8")).replace("+", "%20")).replace("{IP}",(ip)).replace("{DEVICE_ID}",(this.usr.getDeviceId())).replace("{ISTV}",istv);
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
            if(!response.contains("false") && !response.contains("Mantenimiento") && !(mContext instanceof MainActivity)) {
                Dialogs.showCustomDialog(mContext, "Atencion", response, new MessageCallbackListener() {
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
