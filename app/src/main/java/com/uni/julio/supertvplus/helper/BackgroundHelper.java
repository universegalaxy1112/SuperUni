package com.uni.julio.supertvplus.helper;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.squareup.picasso.Target;

import java.util.Timer;

public class BackgroundHelper {

    private static long BACKGROUND_UPDATE_DELAY = 200;

    private final Handler mHandler = new Handler();

    private Activity mActivity;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundURL;


    private Drawable mDefaultBackground;
    private Target mBackgroundTarget;

    public BackgroundHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.mBackgroundURL = backgroundUrl;
    }
}
