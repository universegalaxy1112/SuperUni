package com.uni.julio.supertv.utils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.utils.networing.WebConfig;

import org.json.JSONObject;

public class Tracking implements StringRequestListener, OnClickListener {
    private static AppCompatActivity mActivity = null;
    private static Tracking mInstance = null;
    private String action = "IDLE";
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    /* access modifiers changed from: private */
    public boolean isTracking = false;
    private Runnable trackingThread = new Runnable() {
        public void run() {
            Tracking.this.track();
            if (Tracking.this.isTracking) {
                Tracking.this.handler.postDelayed(this, 30000);
                return;
            }
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    };
    private String usr = "";

    public static Tracking getInstance(AppCompatActivity activity) {
        if (mInstance == null) {
            mInstance = new Tracking();
        }
        mActivity = activity;
        return mInstance;
    }

    public void onStart() {
        String theUser = DataManager.getInstance().getString("theUser", "");
        if (!TextUtils.isEmpty(theUser)) {
            this.usr = ((User) new Gson().fromJson(theUser, User.class)).getName();
        }
        if (!this.isTracking) {
            this.isTracking = true;
            this.handler.post(this.trackingThread);
        }
    }

    public void onStop() {
        this.isTracking = false;
    }

    /* access modifiers changed from: private */
    public void track() {
        String ip =Device.ip;
        String url = WebConfig.trackingURL.replace("{USER}", this.usr).replace("{MOVIE}", this.action).replace("{IP}",ip).replace("{DEVICE_ID}",Device.getIdentifier());
        NetManager.getInstance().makeStringRequest(url, this);
    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public void onError() {
        Log.d("DNLS", "Tracking ERROR");
    }

    public void onCompleted(String response) {
        Log.d("DNLS", "Tracking response: " + response);
        try {
            JSONObject messageJson = new JSONObject(response);
            if (!messageJson.isNull("message") && !TextUtils.isEmpty(messageJson.getString("message"))) {
                Dialogs.showOneButtonDialog( mActivity, "Atención", messageJson.getString("message"),  this);
            }
        } catch (Exception e) {
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        //String url = WebConfig.deleteMessageURL.replace("{USER}", this.usr);
        //NetManager.getInstance().makeStringRequest(url, this);
    }
}
