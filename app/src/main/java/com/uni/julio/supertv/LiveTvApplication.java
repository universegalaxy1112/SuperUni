package com.uni.julio.supertv;

import android.content.Context;
import android.text.TextUtils;

import androidx.multidex.MultiDexApplication;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.gson.Gson;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.DataManager;

public class LiveTvApplication extends MultiDexApplication {

    private static Context applicationContext;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
//        userAgent = "s1oqqGfrT06PPZ70dHUqJBxMx5lJibaHzzC";//Util.getUserAgent(this, "ExoPlayerDemo");
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
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }


}
