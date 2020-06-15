package com.uni.julio.supertvplus.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.databinding.ActivityAccountBinding;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.viewmodel.AccountDetailsViewModel;
import com.uni.julio.supertvplus.viewmodel.AccountDetailsViewModelContract;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;

public class AccountActivity extends BaseActivity implements AccountDetailsViewModelContract.View {
    private AccountDetailsViewModel accountDetailsViewModel;
    private ActivityAccountBinding activityAccountBinding;
    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return accountDetailsViewModel;
    }
    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        Toolbar toolbar = activityAccountBinding.toolbar;
        toolbar.setTitle("Mi Cuenta");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(Device.treatAsBox){
             (activityAccountBinding.Appbarlayout).setVisibility(View.GONE);
        }
        accountDetailsViewModel = new AccountDetailsViewModel(getActivity(), activityAccountBinding);
        activityAccountBinding.setAccountDetailsVM(accountDetailsViewModel);
        accountDetailsViewModel.showAccountDetails();
        TextView textView = activityAccountBinding.testspeed;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(SpeedTestActivity.class);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCloseSessionSelected() {
        DataManager.getInstance().saveData("theUser","");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           finishActivity();
            return true;
        }
        return false;
    }
    @Override
    public void onCloseSessionNoInternet() {
        Dialogs.showOneButtonDialog(getActivity(), R.string.no_connection_title,  R.string.close_session_no_internet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void onError() {
        finishActivity();
    }
}
