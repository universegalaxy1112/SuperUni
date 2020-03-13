package com.uni.julio.supertv.view;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.listeners.DialogListener;
import com.uni.julio.supertv.listeners.MessageCallbackListener;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.library.CustomProgressDialog;
import com.uni.julio.supertv.utils.networing.NetManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements StringRequestListener, MessageCallbackListener {
private EditText mUsernameView;
private EditText mPassView;
private CustomProgressDialog customProgressDialog;
public static final int BLOCKED_OR_NEVER_ASKED = 2;
public static final int DENIED = 1;
private static final String[] DUMMY_CREDENTIALS = {"foo@example.com:hello", "bar@example.com:world"};
public static final int GRANTED = 0;
private static final int REQUEST_READ_PHONE_STATE = 0;
private static final int REQUEST_STORAGE = 1;
boolean denyAll = false;
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
                            user.setExpiration_date((String) jsonObject.getString("expire_date"));
                            user.setDevice(Device.getModel() + " - " + Device.getFW());
                            user.setVersion(Device.getVersion());
                            user.setDeviceId(Device.getIdentifier());
                            if (!jsonObject.isNull("pin")) {
                                DataManager.getInstance().saveData("adultsPassword", jsonObject.getString("pin"));
                            }
                            DataManager.getInstance().saveData("theUser", new Gson().toJson(user));
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
        if (requestCode == 4161) {
            requestPhonePermission();
        }
        if (requestCode != 4168) {
            return;
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {

        } else {
            requestStoragePermission();
        }
    }

    public boolean requestStoragePermission() {
        if (Build.VERSION.SDK_INT < 23 || getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        this.denyAll = false;
        int accept = R.string.accept;
        int message = R.string.permission_storage;
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 2) {
            this.denyAll = true;
            accept = R.string.config;
            message = R.string.permission_storage_config;
        }
        Dialogs.showTwoButtonsDialog( this, accept, (int) R.string.cancel, message, (DialogListener) new DialogListener() {
            @TargetApi(23)
            public void onAccept() {
                if (!LoginActivity.this.denyAll) {
                    DataManager.getInstance().saveData("storagePermissionRequested", Boolean.valueOf(true));
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
        });
        return false;
    }

    private void setupUI() {
        setContentView(R.layout.activity_login);
        String user = "";
        String password = "";
        if(mUsernameView != null) {
            user = mUsernameView.getText().toString();
        }
        if(mPassView != null) {
            password = mPassView.getText().toString();
        }

        mUsernameView = (EditText) findViewById(R.id.edit_username);
        mPassView = (EditText) findViewById(R.id.edit_password);

        mPassView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        TextView mEmailSignInButton =  findViewById(R.id.cv_loginScreen_login);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
             showProgress(true);
             NetManager.getInstance().performLogin(username,password, this);
        }
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }


    private boolean requestPhonePermission() {
        if (Build.VERSION.SDK_INT < 23 || getPermissionStatus("android.permission.READ_PHONE_STATE") == 0) {
            return true;
        }
        this.denyAll = false;
        int accept = R.string.accept;
        int message = R.string.permission_rationale;
        if (getPermissionStatus("android.permission.READ_PHONE_STATE") == 2) {
            this.denyAll = true;
            accept = R.string.config;
            message = R.string.permission_rationale_config;
        }
        Dialogs.showTwoButtonsDialog(  this, accept, (int) R.string.cancel, message, (DialogListener) new DialogListener() {
            @TargetApi(23)
            public void onAccept() {
                if (!LoginActivity.this.denyAll) {
                    DataManager.getInstance().saveData("permissionRequested", Boolean.valueOf(true));
                    LoginActivity.this.requestPermissions(new String[]{"android.permission.READ_PHONE_STATE"}, 0);
                    return;
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", LoginActivity.this.getPackageName(), null));
                LoginActivity.this.startActivityForResult(intent, 4161);
            }
            public void onCancel() {
                LoginActivity.this.finish();
            }
        });
        return false;
    }

    public int getPermissionStatus(String androidPermissionName) {

        if ((!TextUtils.isEmpty(DataManager.getInstance().getString("deviceIdentifier", "")) && androidPermissionName == "android.permission.READ_PHONE_STATE") || ContextCompat.checkSelfPermission(this, androidPermissionName) == 0) {
            return 0;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, androidPermissionName)) {
            return 1;
        }
        boolean pref = false;
        char c = 65535;
        switch (androidPermissionName.hashCode()) {
            case -5573545:
                if (androidPermissionName.equals("android.permission.READ_PHONE_STATE")) {
                    c = 0;
                    break;
                }
                break;
            case 1365911975:
                if (androidPermissionName.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                pref = DataManager.getInstance().getBoolean("permissionRequested", false);
                break;
            case 1:
                pref = DataManager.getInstance().getBoolean("storagePermissionRequested", false);
                break;
        }
        if (!pref) {
            return 1;
        }
        return 2;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults.length == 1 && grantResults[0] != 0) {
            requestPhonePermission();
        }
        if (requestCode != 1) {
            return;
        }
        if (grantResults.length != 1 || grantResults[0] == 0) {

        } else {
            requestStoragePermission();
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
    public void showCustomProgressDialog(String message){
        customProgressDialog = new CustomProgressDialog(this, message);
        customProgressDialog.setCancelable(false);
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
