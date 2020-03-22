package com.uni.julio.supertv.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.utils.Tracking;
public class MoviesTvActivity extends Activity {
    MoviesMenuTVFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_tv);
        LiveTvApplication.appContext = this;
        fragment = new MoviesMenuTVFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_browse_fragment,fragment).commit();
    }
    @Override

    public void onResume(){
        super.onResume();
        Tracking.getInstance(this).enableTrack(true);
        Tracking.getInstance(this).enableSleep(false);
        Tracking.getInstance(this).setAction(getClass().getSimpleName());
        Tracking.getInstance(this).track();
        LiveTvApplication.appContext = this;
    }
    @Override
    public void onPause(){
        super.onPause();
        Tracking.getInstance(this).enableSleep(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Tracking.getInstance(LiveTvApplication.appContext).getSleep()){
                    Tracking.getInstance(LiveTvApplication.appContext).setAction("Sleeping");
                    Tracking.getInstance(LiveTvApplication.appContext).track();
                    Tracking.getInstance(LiveTvApplication.appContext).enableSleep(false);
                    Tracking.getInstance(LiveTvApplication.appContext).enableTrack(false);
                }
            }
        },1000);
        Context appCompatActivity= LiveTvApplication.appContext;
        if(this.equals(appCompatActivity))
            LiveTvApplication.appContext = null;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

       /* if(keyCode==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            fragment.loadData();
        }*/

        return false;
    }
}
