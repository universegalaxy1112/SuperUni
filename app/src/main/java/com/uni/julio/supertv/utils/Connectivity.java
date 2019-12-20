package com.uni.julio.supertv.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.uni.julio.supertv.LiveTvApplication;

public class Connectivity {

    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) LiveTvApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
