package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;


import com.uni.julio.supertv.R;
import com.uni.julio.supertv.listeners.MessageCallbackListener;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.viewmodel.AccountDetailsViewModel;
import com.uni.julio.supertv.viewmodel.AccountDetailsViewModelContract;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public class DialogActivity extends BaseActivity  {

    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return null;
    }
    @Override
    protected Lifecycle.View getLifecycleView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");
        String title = intent.getStringExtra("EXTRA_TITLE");
        Dialogs.showCustomDialog(this, title, message, new MessageCallbackListener() {
            @Override
            public void onDismiss() {
                finishActivity();
            }

            @Override
            public void onAccept() {

            }

            @Override
            public void onError() {

            }
        });
    }
}
