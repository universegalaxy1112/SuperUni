package com.uni.julio.supertvplus.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*
import com.google.firebase.messaging.FirebaseMessaging;
*/
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.listeners.DialogListener;
import com.uni.julio.supertvplus.listeners.NotificationListener;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.User;
import com.uni.julio.supertvplus.service.NotificationReceiveService;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.Tracking;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;
import com.uni.julio.supertvplus.viewmodel.MainCategoriesMenuViewModel;
import com.uni.julio.supertvplus.viewmodel.MainCategoriesMenuViewModelContract;


public class MainActivity extends BaseActivity implements MainCategoriesMenuViewModelContract.View, NotificationListener {
    private MainCategoriesMenuViewModel mainCategoriesMenuViewModel;
    private boolean requested=false;
    private InterstitialAd mInterstitialAd;

    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return mainCategoriesMenuViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SuperTV");
        setSupportActionBar(toolbar);
        mainCategoriesMenuViewModel = new MainCategoriesMenuViewModel(this);
        if(Device.treatAsBox){
            findViewById(R.id.Appbarlayout).setVisibility(View.GONE);
        }
        User user = LiveTvApplication.getUser();
        if(user == null || !user.getMembership()) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();

                }

                @Override
                public void onAdClosed() {
                    startLoading(mainCategoryId);
                }
            });
        }

        getViewModel().onViewAttached(getLifecycleView());
        mainCategoriesMenuViewModel.showMainCategories((TVRecyclerView) findViewById(R.id.maincategory));
        if (savedInstanceState != null && savedInstanceState.getSerializable("started") != null)
            this.started = true;
        if(!started) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopup();
                }
            },500);
        }

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
        mainCategoriesMenuViewModel.onViewResumed();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
        requested = false;
    }
    private void subscribeTopic(String topic){
        /*FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.accept);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.cancel);
                        }
                        Log.d("tag", msg);
                    }
                });*/
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
            Tracking.getInstance().setAction("Sleeping");
            Tracking.getInstance().track();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Dialogs.showTwoButtonsDialog(getActivity(),R.string.ok_dialog,R.string.cancel,R.string.exit_confirm, new DialogListener() {

                        @Override
                        public void onAccept() {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onDismiss() {

                        }
                    });
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
         mainCategoryId = mainCategory.getId();
        if(mainCategoryId==10) {
            onAccountPressed();
            onAccountPressed();
            return;
        }
        if(mainCategoryId == 7) {
            String pin=DataManager.getInstance().getString("adultsPassword", "");
            openPasswordDialog(pin);
        }
        else {
            if(mInterstitialAd != null && mInterstitialAd.isLoaded())
                mInterstitialAd.show();
            else
                startLoading(mainCategoryId);
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

        String device_num = DataManager.getInstance().getString("device_num","");
        User user = LiveTvApplication.getUser();
        if(LiveTvApplication.appContext instanceof MainActivity){
            final MaterialDialog dialog=new MaterialDialog.Builder(this)
                    .customView(R.layout.castloadingdialog,false)
                    .contentLineSpacing(0)
                    .theme(Theme.LIGHT)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
            TextView titleView= dialog.getCustomView().findViewById(R.id.title);
            TextView contentView= dialog.getCustomView().findViewById(R.id.content);
            titleView.setText(R.string.attention);
            contentView.setText("Estimado " + ((user ==  null) ? "" : user.getName()) + ", " + " Tu fecha de Vencimiento es: " + user.getExpiration_date()+" Y tienes "+ device_num+" Dispositivos Conectados.");
            TextView cancel = dialog.getCustomView().findViewById(R.id.cancel);
            cancel.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Tracking.getInstance().onStart();
                    try {
                        if(LiveTvApplication.appContext instanceof MainActivity && dialog.isShowing()){
                            dialog.dismiss();
                        }


                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }

                }
            }, 3000);
        }
    }

    @Override
    public void notificationClicked(String action, int mainCategoryId) {
        startLoading(mainCategoryId);
    }
}
