package com.uni.julio.supertvplus.view;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.Tracking;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract Lifecycle.ViewModel getViewModel();

    protected abstract Lifecycle.View getLifecycleView();
    protected ModelTypes.SelectedType selectedType = null;
    protected int mainCategoryId = -1;
    protected int movieCategoryId = -1;
    protected boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        LiveTvApplication.appContext = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        Tracking.getInstance().enableTrack(true);
        Tracking.getInstance().enableSleep(false);
        if(!(this instanceof VideoPlayActivity)){
            Tracking.getInstance().setAction(getClass().getSimpleName());
        }
        Tracking.getInstance().track();
        if(getViewModel() != null)
            getViewModel().onViewResumed();
        LiveTvApplication.appContext = this;

    }
    @Override
    public void onPause(){
        super.onPause();
        Tracking.getInstance().enableTrack(true);
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
        Context appCompatActivity=LiveTvApplication.appContext;
        if(this.equals(appCompatActivity))
            LiveTvApplication.appContext = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getViewModel() != null)
            getViewModel().onViewAttached(getLifecycleView());
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putSerializable("started", true);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     }

    public void launchActivity(Class classToLaunch) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void launchActivity(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public Intent getLaunchIntent(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        return launchIntent;
    }

    public void noInternetConnection(DialogInterface.OnClickListener listener ) {
        Dialogs.showOneButtonDialog(getActivity(), R.string.no_connection_title,  R.string.no_connection_message, listener);
    }
    public void finishActivity() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    final public BaseActivity getActivity(){
        return this;
    }
}
