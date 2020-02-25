package com.uni.julio.supertv.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.julio.supertv.R;


public class MoviesTvActivity extends AppCompatActivity {
    MoviesMenuTVFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_tv);
        fragment=new MoviesMenuTVFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.browse_fragment,fragment).commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        if(keyCode==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            fragment.loadData();
        }


        return false;
    }
}
