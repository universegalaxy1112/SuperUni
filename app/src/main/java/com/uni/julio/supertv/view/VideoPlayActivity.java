package com.uni.julio.supertv.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.utils.Tracking;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;

public class VideoPlayActivity extends AppCompatActivity {
    VideoPlayFragment videoPlayFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play2);
        videoPlayFragment=new VideoPlayFragment();
        Tracking.getInstance(this).onStart();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.video_container,videoPlayFragment).commit();
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onResume(){
        super.onResume();
        LiveTvApplication.getInstance().setCurrentActivity(this);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Tracking.getInstance(this).onStop();

    }
    @Override
    public void onPause(){
        super.onPause();
        AppCompatActivity appCompatActivity=LiveTvApplication.getInstance().getActivity();
        if(this.equals(appCompatActivity))
            LiveTvApplication.getInstance().setCurrentActivity(null);
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        videoPlayFragment.onNewIntent(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
            videoPlayFragment.dispatchKeyEvent();
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
            videoPlayFragment.dispatchKeyEvent();
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
            videoPlayFragment.dispatchKeyEvent();
        }
        if(keyCode==KeyEvent.KEYCODE_MEDIA_FAST_FORWARD||keyCode==KeyEvent.KEYCODE_FORWARD||keyCode==272||keyCode==274||keyCode==KeyEvent.KEYCODE_BUTTON_R1||keyCode==KeyEvent.KEYCODE_BUTTON_R2||keyCode==KeyEvent.KEYCODE_RIGHT_BRACKET ){
            videoPlayFragment.doForwardVideo();
        }
        if(keyCode==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            videoPlayFragment.playPause();
        }
        if(keyCode==KeyEvent.KEYCODE_MEDIA_REWIND){
            videoPlayFragment.doRewindVideo();

        }

        return false;
    }
}
