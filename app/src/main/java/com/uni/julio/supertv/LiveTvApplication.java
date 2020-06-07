package com.uni.julio.supertv;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import androidx.multidex.MultiDexApplication;
/*import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.RenderersFactory;*/
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
import com.uni.julio.supertv.utils.Tracking;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.view.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LiveTvApplication extends MultiDexApplication implements StringRequestListener, MessageCallbackListener {
    private static Context applicationContext;
    protected String userAgent;
    public Handler handler;
    public static User user = null;
    public static Context appContext=null;

    @Override
    public void onCreate() {
        super.onCreate();
        handler=new Handler();
        applicationContext = getApplicationContext();
        handler.postDelayed(new Runnable(){
            public void run(){
                performLogin();
                handler.postDelayed(this, 600000);
            }
        }, 600000);
    }

    public void performLogin(){
        if( appContext != null && !(appContext instanceof SplashActivity) && LiveTvApplication.getUser() != null){
            NetManager.getInstance().performLoginCode(LiveTvApplication.getUser().getName(),LiveTvApplication.getUser().getPassword(), LiveTvApplication.getUser().getDeviceId(),this);
        }
    }

    public static User getUser(){

        if(LiveTvApplication.user == null){
            String theUser = DataManager.getInstance().getString("theUser","");
            if(!TextUtils.isEmpty(theUser)) {
                LiveTvApplication.user = new Gson().fromJson(theUser, User.class);
            }
        }
        return LiveTvApplication.user;

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
        userAgent = LiveTvApplication.getUser().getUser_agent();
        return new DefaultHttpDataSourceFactory(userAgent,bandwidthMeter);
    }


   /* public RenderersFactory buildRenderersFactory(boolean preferExtensionRenderer) {
        @DefaultRenderersFactory.ExtensionRendererMode
        int extensionRendererMode =
                useExtensionRenderers()
                        ? (preferExtensionRenderer
                        ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(*//* context= *//* this)
                .setExtensionRendererMode(extensionRendererMode);
    }*/

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

    @Override
    public void onCompleted(String response) {
       try{
            if(appContext !=null){
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("status") && "1".equals(jsonObject.getString("status"))) {
                            LiveTvApplication.getUser().setAdultos(jsonObject.getInt("adultos"));
                        }else{
                            Tracking.getInstance().enableTrack(false);
                            String errorFound = jsonObject.getString("error_found");
                            switch (errorFound) {
                                case "103":
                                case "104":
                                    Dialogs.showOneButtonDialog(appContext, appContext.getString(R.string.attention), appContext.getString(R.string.login_error_change_device).replace("{ID}", LiveTvApplication.getUser().getDeviceId()), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closeApp();
                                        }
                                    });
//
                                    break;
                                case "105":
                                    Dialogs.showOneButtonDialog(appContext, appContext.getString(R.string.attention), appContext.getString(R.string.login_error_usr_pss_incorrect).replace("{ID}", LiveTvApplication.getUser().getDeviceId()), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closeApp();
                                        }
                                    });
                                    break;
                                case "107":
                                    Dialogs.showCustomDialog(appContext,appContext.getString(R.string.attention),"Dear "+LiveTvApplication.getUser().getName()+", Your Membership is expired! Please extend your membership.",this);
                                     break;
                                case "108": {
                                    closeApp();
                                   break;
                                }
                                case "109": {
                                    Dialogs.showOneButtonDialog(appContext, R.string.login_error_demo, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closeApp();
                                        }
                                    });
                                }
                                break;
                                default:
                                    Dialogs.showCustomDialog(appContext,appContext.getString(R.string.attention),"Dear "+ LiveTvApplication.getUser().getName()+", Your account is inactive due to some problems. Please contact the support.",this);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }catch (NullPointerException e){
           e.printStackTrace();
        }
    }
    public void closeApp(){
        ((Activity)appContext).finishAffinity();
        System.exit(0);
    }
    public void showErrorMessage() {
        if(appContext != null)
            Tracking.getInstance().enableTrack(true);
            Dialogs.showOneButtonDialog(appContext, R.string.no_connection_title,  R.string.no_connection_message);
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
