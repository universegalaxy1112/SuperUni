package com.uni.julio.supertv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDexApplication;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.gson.Gson;
import com.uni.julio.supertv.listeners.MessageCallbackListener;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.view.LoginActivity;
import com.uni.julio.supertv.view.MainActivity;
import com.uni.julio.supertv.view.OneSeasonDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LiveTvApplication extends MultiDexApplication implements StringRequestListener, MessageCallbackListener {
    private static Context applicationContext;
    private static LiveTvApplication mInstance;
    protected String userAgent;
    public Handler handler;
    public User user;
    String theUser = "";
    protected String userName = "";
    protected String password ="";
    protected String id = "";
    public AppCompatActivity appCompatActivity=null;
    public static LiveTvApplication getInstance() {
        if(mInstance == null) {
            mInstance = new LiveTvApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler=new Handler();
        applicationContext = getApplicationContext();
        if(mInstance == null) {
            mInstance = new LiveTvApplication();
        }
    }
    public void setCurrentActivity(AppCompatActivity activity){
          appCompatActivity=activity;
          sendLocation();
    }
    public void sendLocation(){
        if( appCompatActivity != null){
            if(userName.equals("")){
                theUser = DataManager.getInstance().getString("theUser","");
                if(!TextUtils.isEmpty(theUser)) {
                    user = new Gson().fromJson(theUser, User.class);
                    userName = user.getName();
                    password = user.getPassword();
                    id = user.getDeviceId();
                }
            }
            NetManager.getInstance().performLoginCode(userName,password, id,this);
        }
    }
    public AppCompatActivity getActivity(){
        return appCompatActivity;
    }
    public static Context getAppContext() {
        return applicationContext;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        userAgent = "";
        String theUser = DataManager.getInstance().getString("theUser","");
        if(!TextUtils.isEmpty(theUser)) {
            User user = new Gson().fromJson(theUser, User.class);
            userAgent = user.getUser_agent();
        }
        return new DefaultHttpDataSourceFactory(userAgent);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

    @Override
    public void onCompleted(String response) {
        try{
            if(appCompatActivity !=null){
                if (!TextUtils.isEmpty(response)) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("status") && "1".equals(jsonObject.getString("status"))) {
                            return;
                        }else{
                            String errorFound = jsonObject.getString("error_found");
                            switch (errorFound) {
                                case "103":
                                case "104":
                                    Dialogs.showOneButtonDialog(appCompatActivity, appCompatActivity.getString(R.string.attention), appCompatActivity.getString(R.string.login_error_change_device).replace("{ID}", Device.getIdentifier()), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closeApp();
                                        }
                                    });
//
                                    break;
                                case "105":
                                    Dialogs.showOneButtonDialog(appCompatActivity, appCompatActivity.getString(R.string.attention), appCompatActivity.getString(R.string.login_error_usr_pss_incorrect).replace("{ID}", Device.getIdentifier()), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closeApp();
                                        }
                                    });
                                    break;
                                case "107":
                                    Dialogs.showCustomDialog(appCompatActivity,appCompatActivity.getString(R.string.attention),"Dear "+user.getName()+", Your Membership is expired! Please extend your membership.",this);
                                     break;
                                case "108": {

                                   break;
                                }
                                case "109": {
                                    Dialogs.showOneButtonDialog(appCompatActivity, R.string.login_error_demo, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closeApp();
                                        }
                                    });
                                }
                                break;
                                default:
                                    Dialogs.showCustomDialog(appCompatActivity,appCompatActivity.getString(R.string.attention),"Dear "+user.getName()+", Your account is inactive due to some problems. Please contact the support.",this);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                    }

                }

            }

        }catch (NullPointerException e){

        }
    }
    public void closeApp(){
        //Intent launchIntent = new Intent(appCompatActivity, LoginActivity.class);
        /*appCompatActivity.startActivity(launchIntent);
        appCompatActivity.finish();*/
        System.exit(0);
       // appCompatActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }
    public void showErrorMessage() {
        if(appCompatActivity != null)
            Dialogs.showOneButtonDialog(appCompatActivity, R.string.no_connection_title,  R.string.no_connection_message);
    }

    @Override
    public void onError() {
        showErrorMessage();
    }

    @Override
    public void onDismiss() {
        closeApp();
    }

    @Override
    public void onAccept() {

    }
}
