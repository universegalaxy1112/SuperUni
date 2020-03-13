package com.uni.julio.supertv.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

        /*fragment=new MoviesMenuTVFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.browse_fragment,fragment).commit();*/
    }
    @Override
    public void onResume(){
        super.onResume();
        Tracking.getInstance(this).onStart();
        LiveTvApplication.appContext = this;
    }
    @Override
    public void onPause(){
        super.onPause();
        Tracking.getInstance(this).setAction("IDLE");
        Tracking.getInstance(this).track();
        Tracking.getInstance(this).onStop();
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
