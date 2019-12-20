package com.uni.julio.supertv.view;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.library.CustomProgressDialog;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract Lifecycle.ViewModel getViewModel();
    //force sub-fragments to implement the getFragmentInstance to pass to onViewAttached
    protected abstract Lifecycle.View getLifecycleView();
    protected ModelTypes.SelectedType selectedType;
    protected int mainCategoryId;
    protected int movieCategoryId;
    private CustomProgressDialog customProgressDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);

    //just send the key event to the fragment
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return false;
    }
    @Override
    public void onResume() {

        super.onResume();
        getViewModel().onViewResumed();

    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().onViewAttached(getLifecycleView());
    }

    @Override
    public void onStop() {

        super.onStop();
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
