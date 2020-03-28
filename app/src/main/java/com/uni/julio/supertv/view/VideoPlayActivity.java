package com.uni.julio.supertv.view;

import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.leanback.app.VideoFragment;

import com.google.android.exoplayer2.video.VideoFrameReleaseTimeHelper;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.listeners.LiveTVToggleUIListener;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public class VideoPlayActivity extends BaseActivity implements LiveTVToggleUIListener {
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Stop Sound
            VideoPlayFragment fragment = getVideoPlayFragment();
            fragment.mute();
        }
    };

    private  boolean isReceiverRegistered = false;
    private VideoPlayFragment videoPlayFragment;
    private FrameLayout frameLayout;
    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return null;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
    }
    @Override
    public void onStart(){
        super.onStart();
        Intent intent = getIntent();
        mainCategoryId = intent.getIntExtra("mainCategoryId",0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoPlayFragment=new VideoPlayFragment();
        frameLayout = findViewById(R.id.video_container);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.video_container,videoPlayFragment).commit();
        sendBroadcast(new Intent("PauseSound"));
    }

    public VideoPlayFragment getVideoPlayFragment()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.video_container);
        if(fragment instanceof VideoPlayFragment)
        {
            return (VideoPlayFragment) fragment;
        }
        else
            return null;
    }
    @Override
    public void onResume(){
        super.onResume();
        if(isReceiverRegistered)
        {
            unregisterReceiver(receiver);
            isReceiverRegistered = false;
            getVideoPlayFragment().unMute();
        }
        videoPlayFragment.useController();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(isReceiverRegistered)
        {
            unregisterReceiver(receiver);
            isReceiverRegistered = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isReceiverRegistered)
        {
            unregisterReceiver(receiver);
            isReceiverRegistered = false;
        }
        PackageManager packageManager = getPackageManager();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)){
            finishAndRemoveTask();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        videoPlayFragment.onNewIntent(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enterPIPMode(){
        registerReceiver(receiver, new IntentFilter("PauseSound"));
        isReceiverRegistered = true;
        videoPlayFragment.hideController();
        Rational aspectRatio = new Rational(frameLayout.getWidth()+100, frameLayout.getHeight()+100);
        PictureInPictureParams.Builder mPictureInPictureParamsBuilder =
                new PictureInPictureParams.Builder();
        mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
        enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUserLeaveHint() {
        if (!isInPictureInPictureMode()) {
            Rational aspectRatio = new Rational(frameLayout.getWidth(), frameLayout.getHeight());
            PictureInPictureParams.Builder mPictureInPictureParamsBuilder =
                    new PictureInPictureParams.Builder();
            mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration newConfig) {
        if (isInPictureInPictureMode) {

        } else {

        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            PackageManager packageManager = getPackageManager();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)){
                enterPIPMode();
            }else{
                finishAndRemoveTask();
            }
            return false;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
            videoPlayFragment.dispatchKeyEvent();
            return false;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
            videoPlayFragment.dispatchKeyEvent();
            return false;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
            videoPlayFragment.toggleTitle();
            videoPlayFragment.dispatchKeyEvent();
            return false;
        }
        if(keyCode==KeyEvent.KEYCODE_MEDIA_FAST_FORWARD||keyCode==KeyEvent.KEYCODE_FORWARD||keyCode==272||keyCode==274||keyCode==KeyEvent.KEYCODE_BUTTON_R1||keyCode==KeyEvent.KEYCODE_BUTTON_R2||keyCode==KeyEvent.KEYCODE_RIGHT_BRACKET ){
            videoPlayFragment.doForwardVideo();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            videoPlayFragment.playPause();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_MEDIA_REWIND){
            videoPlayFragment.doRewindVideo();
            return true;
        }
        /*videoPlayFragment.controlVolumn(event);
        super.dispatchKeyEvent(event);*/
        return false;
    }

    @Override
    public void onToggleUI(boolean show) {
        videoPlayFragment.toggleTitle();
    }
}
