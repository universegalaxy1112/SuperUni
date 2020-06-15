package com.uni.julio.supertvplus.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import androidx.fragment.app.FragmentActivity;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.utils.Tracking;

public class SearchTvActivity extends FragmentActivity {
    SearchTvFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv);
        LiveTvApplication.appContext = this;
        fragment = new SearchTvFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_search_fragment,fragment).commit();
    }
    @Override

    public void onResume(){
        super.onResume();
        Tracking.getInstance().enableTrack(true);
        Tracking.getInstance().enableSleep(false);
        Tracking.getInstance().setAction(getClass().getSimpleName());
        Tracking.getInstance().track();
        LiveTvApplication.appContext = this;
    }
    @Override
    public void onPause(){
        try{
            super.onPause();
            Tracking.getInstance().enableSleep(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Tracking.getInstance().getSleep()){
                        Tracking.getInstance().setAction("Sleeping");
                        Tracking.getInstance().track();
                        Tracking.getInstance().enableSleep(false);
                        Tracking.getInstance().enableTrack(false);
                    }
                }
            },1000);
            Context appCompatActivity= LiveTvApplication.appContext;
            if(this.equals(appCompatActivity))
                LiveTvApplication.appContext = null;
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return false;
    }
}
