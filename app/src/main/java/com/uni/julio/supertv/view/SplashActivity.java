package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.networing.HttpRequest;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.SplashViewModel;
import com.uni.julio.supertv.viewmodel.SplashViewModelContract;
public class SplashActivity extends BaseActivity implements SplashViewModelContract.View {
    private boolean isInit = false;
    private SplashViewModel splashViewModel;

    protected Lifecycle.ViewModel getViewModel() {
        return splashViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Device.setHDMIStatus();
        HttpRequest.getInstance().trustAllHosts();//trust all HTTPS hosts
        splashViewModel = new SplashViewModel(this);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this.getBaseContext(), "Can not go back!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isInit){
            splashViewModel.login();
            isInit = true;
        }

    }

    @Override
    public void onCheckForUpdateCompleted(boolean z, String str) {

    }

    @Override
    public void onDownloadUpdateCompleted(String str) {

    }

    @Override
    public void onDownloadUpdateError(int i) {

    }

    @Override
    public void onLoginCompleted(boolean success) {
        if(success){
            launchActivity(MainActivity.class);
         }
        else{
            if(Connectivity.isConnected()){
                launchActivity(LoginActivity.class);
            }
            else{
                noInternetConnection(new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
         }
        finishActivity();
    }

}
