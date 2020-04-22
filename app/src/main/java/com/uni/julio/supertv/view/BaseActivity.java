package com.uni.julio.supertv.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.Tracking;
import com.uni.julio.supertv.utils.library.CustomProgressDialog;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract Lifecycle.ViewModel getViewModel();

    //force sub-fragments to implement the getFragmentInstance to pass to onViewAttached
    protected abstract Lifecycle.View getLifecycleView();
    protected ModelTypes.SelectedType selectedType;
    protected int mainCategoryId;
    protected int movieCategoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LiveTvApplication.appContext = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        Tracking.getInstance(this).enableTrack(true);
        Tracking.getInstance(this).enableSleep(false);
        Tracking.getInstance(this).setAction(getClass().getSimpleName());
        Tracking.getInstance(this).track();
        if(getViewModel() != null)
            getViewModel().onViewResumed();
        LiveTvApplication.appContext = this;

    }
    @Override
    public void onPause(){
        super.onPause();
        Tracking.getInstance(this).enableTrack(true);
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
    public void noInternetConnection() {
        Dialogs.showOneButtonDialog(getActivity(), R.string.no_connection_title,  R.string.no_connection_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishActivity();
            }
        });
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
