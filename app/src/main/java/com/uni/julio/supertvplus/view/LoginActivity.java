package com.uni.julio.supertvplus.view;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.listeners.DialogListener;
import com.uni.julio.supertvplus.listeners.MessageCallbackListener;
import com.uni.julio.supertvplus.listeners.StringRequestListener;
import com.uni.julio.supertvplus.model.User;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.Tracking;
import com.uni.julio.supertvplus.utils.library.CustomProgressDialog;
import com.uni.julio.supertvplus.utils.networing.NetManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements StringRequestListener, MessageCallbackListener {
private EditText mUsernameView;
private EditText mPassView;
private CustomProgressDialog customProgressDialog;
boolean denyAll = false;
private AdView mAdView;

    @Override
    protected void onResume() {
        super.onResume();
        Tracking.getInstance().enableSleep(false);
        Tracking.getInstance().enableTrack(false);
        LiveTvApplication.appContext = this;
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }
    public void onCompleted(String response) {
        hideProgressDialog();
        if (!TextUtils.isEmpty(response)) {
                try {
                    final String email = mUsernameView.getText().toString();
                    String password = mPassView.getText().toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("status"))) {
                        String userAgent = jsonObject.getString("user-agent");
                        if (!TextUtils.isEmpty(userAgent)) {
                            User user = new User();
                            user.setName(email);
                            user.setPassword(password);
                            user.setUser_agent(userAgent);
                            user.setExpiration_date( jsonObject.getString("expire_date"));
                            user.setDevice(Device.getModel() + " - " + Device.getFW());
                            user.setVersion(Device.getVersion());
                            user.setDeviceId(Device.getIdentifier());
                            user.setAdultos(jsonObject.getInt("adultos"));
                            if (!jsonObject.isNull("pin")) {
                                DataManager.getInstance().saveData("adultsPassword", jsonObject.getString("pin"));
                            }
                            DataManager.getInstance().saveData("theUser", new Gson().toJson(user));
                            LiveTvApplication.user = user;
                            DataManager.getInstance().saveData("device_num", jsonObject.getString("device_num"));
                            startMain();
                            return;
                         }
                    } else {
                        DataManager.getInstance().saveData("theUser", "");
                        String errorFound = jsonObject.getString("error_found");
                        switch (errorFound) {
                            case "103":
                            case "104":
                                Dialogs.showOneButtonDialog(this, getString(R.string.attention), getString(R.string.login_error_change_device).replace("{ID}", Device.getIdentifier()), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                });
//
                                break;
                            case "105":
                                mPassView.setText("");
                                mPassView.requestFocus();
                                showErrorMessage(getString(R.string.login_error_usr_pss_incorrect));
                                break;
                            case "107":
                                showErrorMessage(getString(R.string.login_error_expired));
                                break;
                            case "108": {
                                Dialogs.showOneButtonDialog(this, getString(R.string.attention), getString(R.string.login_error_change_account).replace("{ID}", Device.getIdentifier()), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                });
//
                            }
                            break;
                            case "109": {
                                Dialogs.showOneButtonDialog(this, R.string.login_error_demo, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                });
                            }
                            break;
                            case "110": {
                                Dialogs.showOneButtonDialog(this, R.string.ip_limitation, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                });
                            }
                            break;
                            default:
                                showErrorMessage(getString(R.string.login_error_generic).replace("CODE", errorFound));
                                break;
                        }
                        return;
                    }
                } catch (JSONException e) {
//                e.printStackTrace();
                }
            }
        DataManager.getInstance().saveData("theUser", "");
    }
    private void startMain(){
        Intent launchIntent = new Intent(this, MainActivity.class);
        startActivity(launchIntent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }
    @Override
    public void onError() {
        showErrorMessage();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
        if (requestCode != 4168) {
            finish();
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            finish();
        }
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
                if (!LoginActivity.this.denyAll) {
                    DataManager.getInstance().saveData("storagePermissionRequested", Boolean.TRUE);
                    LoginActivity.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    return;
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", LoginActivity.this.getPackageName(), null));
                LoginActivity.this.startActivityForResult(intent, 4168);
            }

            public void onCancel() {
                LoginActivity.this.finish();
             }

            @Override
            public void onDismiss() {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    private void setupUI() {

        setContentView(R.layout.activity_login);
        User user = LiveTvApplication.getUser();
        if(user == null || !user.getMembership()) {
            mAdView = findViewById(R.id.adView);
            if(mAdView != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        }

        mUsernameView =  findViewById(R.id.edit_username);
        mPassView =  findViewById(R.id.edit_password);
        mPassView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        requestStoragePermission();
                    }else{
                        attemptLogin();
                    }
                    return true;
                }
                return false;
            }
        });

        TextView mEmailSignInButton =  findViewById(R.id.cv_loginScreen_login);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    requestStoragePermission();
                }else{
                    attemptLogin();
                }
            }
        });
        mEmailSignInButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    v.setSelected(true);
                else
                    v.setSelected(false);
            }
        });
    }
    private void attemptLogin(){
        mUsernameView.setError(null);
        mPassView.setError(null);
        String username=mUsernameView.getText().toString();
        String password=mPassView.getText().toString();
        boolean cancel=false;
        View focusView=null;
        if(TextUtils.isEmpty(password)){
            mPassView.setError(getString(R.string.error_invalid_password));
            focusView=mPassView;
            cancel=true;
        }
        else if(!isPasswordValid(password)){
            mPassView.setError(getString(R.string.error_invalid_password));
            focusView=mPassView;
            cancel=true;
        }
        if(TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();
        } else {
             showProgress(true);
             NetManager.getInstance().performLogin(username,password, this);
        }
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }



    public int getPermissionStatus(String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(this, androidPermissionName) == 0) {
            return 0;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, androidPermissionName) || !DataManager.getInstance().getBoolean("storagePermissionRequested", false)) {
            return 1;
        }
        return 2;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1) {
            finish();
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            finish();
        }
    }
    private void showProgress(final boolean show) {
        if(show){
           showCustomProgressDialog();
        }
        else{
            hideProgressDialog();
        }
    }
    public void showErrorMessage() {
        if(Connectivity.isConnected()) {
            showErrorMessage(getString(R.string.login_error_generic).replace("[CODE]",""));
        }
        else {
            showProgress(false);
            Dialogs.showOneButtonDialog(this, R.string.no_connection_title,  R.string.no_connection_message);
        }
    }
    public void showErrorMessage(String message) {
        showProgress(false);
        Dialogs.showOneButtonDialog(this, message);
    }
    public void showCustomProgressDialog(){
        if(customProgressDialog == null) customProgressDialog = new CustomProgressDialog(this, getString(R.string.wait));
        customProgressDialog.show();
    }
    public void hideProgressDialog(){
        if(customProgressDialog != null) customProgressDialog.dismiss();
     }

    @Override
    public void onDismiss() {
        startMain();
    }

    @Override
    public void onAccept() {

    }
}
