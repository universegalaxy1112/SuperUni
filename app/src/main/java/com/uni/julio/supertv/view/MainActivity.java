package com.uni.julio.supertv.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.listeners.MessageCallbackListener;
import com.uni.julio.supertv.listeners.NotificationListener;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.service.NotificationReceiveService;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.Tracking;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MainCategoriesMenuViewModel;
import com.uni.julio.supertv.viewmodel.MainCategoriesMenuViewModelContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements MainCategoriesMenuViewModelContract.View, NotificationListener {
    private MainCategoriesMenuViewModel mainCategoriesMenuViewModel;
    private boolean requested=false;
    JSONArray videoArray = null;
    int index=0;

    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return mainCategoriesMenuViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mainCategoriesMenuViewModel = new MainCategoriesMenuViewModel(this.getBaseContext());
         this.requested=false;
        setContentView(R.layout.activity_main);
        getViewModel().onViewAttached(getLifecycleView());
        NotificationReceiveService.setNotificationListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SuperTV");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showPopup();
            }
        },500);
        //subscribeTopic("all");
        setSupportActionBar(toolbar);
        if(Device.treatAsBox){
            findViewById(R.id.Appbarlayout).setVisibility(View.GONE);
        }
        TVRecyclerView mainCategoryRecycler=findViewById(R.id.maincategory);
        mainCategoriesMenuViewModel.showMainCategories(mainCategoryRecycler);
        /*Bundle extras = getIntent().getExtras();
        int mainCategoryId = extras.getInt("mainCategoryId",-1);
        if(mainCategoryId != -1)
            startLoading(mainCategoryId);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onResume(){
         super.onResume();
         requested = false;
    }
    @Override
    public void onPause(){
        super.onPause();
        requested = false;

    }
    private void subscribeTopic(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.accept);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.cancel);
                        }
                        Log.d("tag", msg);
                        //Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Tracking.getInstance(this).setAction("Sleeping");
            Tracking.getInstance(this).track();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.killProcess(android.os.Process.myPid());

                }
            },1000);
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            onAccountPressed();
        }else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMainCategorySelected(MainCategory mainCategory) {
         if(requested) return;
        int mainCategoryId = mainCategory.getId();
        if(mainCategoryId==9){
            onAccountPressed();
            return;
        }
        if(mainCategoryId == 7) {
            String pin=DataManager.getInstance().getString("adultsPassword", "");
            if(TextUtils.isEmpty(pin)) {
                //openSetPasswordDialog();
             }
            else{
                openPasswordDialog(pin);
            }
        }
        else {
           startLoading(mainCategory.getId());
        }
    }
    private void startLoading(int mainCategoryId){
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.MAIN_CATEGORY);
        extras.putInt("mainCategoryId", mainCategoryId);
        launchActivity(LoadingActivity.class, extras);
    }
    private void openPasswordDialog(String pin) {
        requested = true;
        new MaterialDialog.Builder(this)
                .title(R.string.prompt_password)
                .content(R.string.adults_password_message)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if(input.toString().equals(DataManager.getInstance().getString("adultsPassword", ""))) {
                            Bundle extras = new Bundle();
                            extras.putSerializable("selectedType", ModelTypes.SelectedType.MAIN_CATEGORY);
                            extras.putInt("mainCategoryId", 7);
                            launchActivity(LoadingActivity.class, extras);
                        }
                        else {
                            dialog.dismiss();
                            requested = false;
                            Toast.makeText(getBaseContext(), R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
                        }
                     }
                })
                .backgroundColor( getResources().getColor(R.color.white))
                .positiveColorRes(R.color.netflix_red)
                .negativeColorRes(R.color.netflix_red)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requested = false;

                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requested = true;
                    }
                })
                .contentColorRes(R.color.bg_general)
                .titleColorRes(R.color.bg_general)
                .contentLineSpacing(1)
                .show();
    }

    @Override
    public void onAccountPressed() {
        requested = true;
        launchActivity(AccountActivity.class);
    }


    private void showPopup(){
            String theUser = DataManager.getInstance().getString("theUser","");
            String device_num = DataManager.getInstance().getString("device_num","");
            User user = new Gson().fromJson(theUser, User.class);
            //subscribeTopic(user.getName());
        final MaterialDialog dialog=new MaterialDialog.Builder(this)
                .customView(R.layout.castloadingdialog,false)
                .contentLineSpacing(0)
                .theme(Theme.LIGHT)
                .backgroundColor(getResources().getColor(R.color.white))
                .show();
        TextView titleView= dialog.getCustomView().findViewById(R.id.title);
        TextView contentView= dialog.getCustomView().findViewById(R.id.content);
        titleView.setText(R.string.attention);
        contentView.setText("Dear " + user.getName() + ", " + "Your expiration date is " + user.getExpiration_date()+" and you have "+ device_num+" devices working.");
        TextView cancel = dialog.getCustomView().findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Tracking.getInstance(LiveTvApplication.appContext).onStart();
                dialog.dismiss();
            }
        }, 5000);
    }

    @Override
    public void notificationClicked(String action, int mainCategoryId) {
        startLoading(mainCategoryId);
    }
}
