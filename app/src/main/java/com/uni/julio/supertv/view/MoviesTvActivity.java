package com.uni.julio.supertv.view;

import android.app.Activity;
import android.os.Bundle;


import androidx.databinding.DataBindingUtil;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityMoviesTvBinding;


public class MoviesTvActivity extends Activity {
    ActivityMoviesTvBinding activityMoviesTvBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoviesTvBinding= DataBindingUtil.setContentView(this,R.layout.activity_movies_tv);
    }
}
