package com.uni.julio.supertv.viewmodel;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.networing.NetManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashViewModel implements SplashViewModelContract.ViewModel, StringRequestListener {

//    public boolean isConnected;
    private NetManager netManager;
    private SplashViewModelContract.View viewCallback;
    private User user;
     public SplashViewModel(SplashViewModelContract.View splash) {
        this.viewCallback= splash;
//        isConnected = Connectivity.isConnected();
        netManager = NetManager.getInstance();
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (SplashViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void login() {
        String usr = "";
        String password = "";
        String id = "";
        String theUser = DataManager.getInstance().getString("theUser","");

        if(!TextUtils.isEmpty(theUser)) {
            user = new Gson().fromJson(theUser, User.class);
            usr = user.getName();
            password = user.getPassword();
            id = user.getDeviceId();
        }
        if(TextUtils.isEmpty(usr) || TextUtils.isEmpty(password) || TextUtils.isEmpty(id)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewCallback.onLoginCompleted(false);
                }
            }, 2000);
        }
        else {
            netManager.performLoginCode(password,this);
        }
    }

    @Override
    public void onCompleted(String response) {
        if(!TextUtils.isEmpty(response)) {
            try {
//                String user = DataManager.getInstance().getString("usr","");
//                String password = DataManager.getInstance().getString("pss","");

//                {"user":"abel3","status":"1","expire_date":"2017-03-21 00:00:00","message":"","user-agent":"s1oqqGfrT06PPZ70dHUqJBxMx5lJibaHzzC"}

                JSONObject jsonObject = new JSONObject(response);
                if("1".equals((String) jsonObject.getString("status"))) {
                    String userAgent = (String) jsonObject.getString("user-agent");
                    if (!jsonObject.isNull("pin")) {
                        DataManager.getInstance().saveData("adultsPassword", jsonObject.getString("pin"));
                    }
                    if(!TextUtils.isEmpty(userAgent)) {
                        user.setUser_agent(userAgent);
                        user.setExpiration_date((String) jsonObject.getString("expire_date"));
                        user.setDevice(Device.getModel() + " - " + Device.getFW());
                        user.setVersion(Device.getVersion());
                        user.setDeviceId(Device.getIdentifier());
                        DataManager.getInstance().saveData("theUser",new Gson().toJson(user));
                        viewCallback.onLoginCompleted(true);
                        return;
                    }
                }
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }
        DataManager.getInstance().saveData("theUser","");
        viewCallback.onLoginCompleted(false);
    }

    @Override
    public void onError() {
        viewCallback.onLoginCompleted(false);
    }
}