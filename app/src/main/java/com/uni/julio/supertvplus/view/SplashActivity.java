package com.uni.julio.supertvplus.view;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.listeners.DialogListener;
import com.uni.julio.supertvplus.model.User;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.networing.HttpRequest;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;
import com.uni.julio.supertvplus.viewmodel.SplashViewModel;
import com.uni.julio.supertvplus.viewmodel.SplashViewModelContract;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SplashActivity extends BaseActivity implements SplashViewModelContract.View {
    private boolean isInit = false;
    private SplashViewModel splashViewModel;
    boolean denyAll=false;
    public ProgressDialog downloadProgress;
    private String updateLocation;
    private FirebaseAnalytics mFirebaseAnalytics;
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
        Device.setHDMIStatus();
        HttpRequest.getInstance().checkCertificate();
        splashViewModel = new SplashViewModel(this);
        setContentView(R.layout.activity_splash);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        List<String> testDeviceIds = Arrays.asList("F8B846125A3D8A8BBEA68195D12D547F");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

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
            if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestStoragePermission();
            } else{
                splashViewModel.checkForUpdate();
                isInit = true;
            }
        }
    }

    @Override
    public void onCheckForUpdateCompleted(boolean hasNewVersion, String location) {
        this.updateLocation = location;
        if (hasNewVersion) {
            try{
                Dialogs.showTwoButtonsDialog( getActivity(),R.string.download , R.string.cancel, R.string.new_version_available,  new DialogListener() {
                    public void onAccept() {
                       if (Connectivity.isConnected()) {
                            downloadUpdate(updateLocation);
                        } else {
                            goToNoConnectionError();
                        }
                    }
                    public void onCancel() {
                        splashViewModel.login();
                    }

                    @Override
                    public void onDismiss() {
                        splashViewModel.login();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            splashViewModel.login();
        }
    }
    private void downloadUpdate(String location) {
        if (Connectivity.isConnected()) {
            downloadProgress = new ProgressDialog(getActivity(),ProgressDialog.THEME_HOLO_LIGHT);
            downloadProgress.setProgressStyle(1);
            downloadProgress.setMessage("Downloading");
            downloadProgress.setIndeterminate(false);
            downloadProgress.setCancelable(false);
            downloadProgress.show();
            splashViewModel.downloadUpdate(location, this.downloadProgress);
            return;
        }
        goToNoConnectionError();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);

        if (requestCode != 4168) {
            return;
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            splashViewModel.checkForUpdate();
            isInit = true;
        } else {
            finishActivity();
        }
    }
    public int getPermissionStatus(String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(getActivity(), androidPermissionName) == 0) {
            return 0;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), androidPermissionName) || !DataManager.getInstance().getBoolean("storagePermissionRequested", false)) {
            return 1;
        }
        return 2;
    }

    public void requestStoragePermission() {
        if (Build.VERSION.SDK_INT < 23 || getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return;
        }
        this.denyAll = false;
        int accept = R.string.accept;
        int message = R.string.permission_storage;
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 2) {
            this.denyAll = true;
            accept = R.string.config;
            message = R.string.permission_storage_config;
        }
        Dialogs.showTwoButtonsDialog( this, accept,  R.string.cancel, message,  new DialogListener() {
            @TargetApi(23)
            public void onAccept() {
                if (!denyAll) {
                    DataManager.getInstance().saveData("storagePermissionRequested", Boolean.TRUE);
                    requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    return;
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivityForResult(intent, 4168);
            }

            public void onCancel() {
                finishActivity();
            }

            @Override
            public void onDismiss() {
                finishActivity();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1) {
            finishActivity();
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            splashViewModel.checkForUpdate();
            isInit = true;
        } else {
            finishActivity();
        }
    }
    public void goToNoConnectionError() {
        noInternetConnection(new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchActivity(LoginActivity.class);
                getActivity().finish();
            }
        });
    }
    @Override
    public void onDownloadUpdateCompleted(String location) {
        this.downloadProgress.dismiss();
        try
        {
            File file = new File(location);
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri downloaded_apk = getFileUri(this, file);
                intent = new Intent(Intent.ACTION_VIEW).setDataAndType(downloaded_apk,
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                intent = new Intent("android.intent.action.INSTALL_PACKAGE");
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.putExtra("android.intent.extra.RETURN_RESULT", false);
            intent.putExtra("android.intent.extra.INSTALLER_PACKAGE_NAME", getPackageName());
            finishActivity();
            startActivity(intent);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    Uri getFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context,
                "com.uni.julio.supertv.fileprovider"
                , file);
    }
    @Override
    public void onDownloadUpdateError(int error) {
        downloadProgress.dismiss();

        if (error == 1) {
            Dialogs.showOneButtonDialog(getActivity(), R.string.verify_unknown_sources, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    splashViewModel.login();
                }
            });
            return;
        }
        Dialogs.showOneButtonDialog(getActivity(), R.string.new_version_generic_error_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                splashViewModel.login();
            }
        });
    }

    @Override
    public void onLoginCompleted(boolean success) {
        if(success){
            launchActivity(MainActivity.class);
            finishActivity();
        }
        else{
            if(Connectivity.isConnected()){
                launchActivity(LoginActivity.class);
                finishActivity();
            }
            else{
                noInternetConnection(new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                });
            }
         }
    }

}
