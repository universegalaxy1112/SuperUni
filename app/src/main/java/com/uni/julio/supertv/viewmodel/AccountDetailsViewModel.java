package com.uni.julio.supertv.viewmodel;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableBoolean;

import com.google.gson.Gson;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityAccountBinding;
import com.uni.julio.supertv.listeners.DialogListener;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.utils.networing.WebConfig;

public class AccountDetailsViewModel implements AccountDetailsViewModelContract.ViewModel {

    private final AppCompatActivity mActivity;
    //    private int mMainCategoryId = 0;
    private AccountDetailsViewModelContract.View viewCallback;
    public ObservableBoolean isLoading;
    public ObservableBoolean isTV;

//    private VideoStreamManager videoStreamManager;
//    private Context mContext;

    public AccountDetailsViewModel(AppCompatActivity activity) {
        isLoading = new ObservableBoolean(false);
        isTV = new ObservableBoolean(Device.canTreatAsBox());
//        videoStreamManager = VideoStreamManager.getInstance();
        mActivity = activity;

    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (AccountDetailsViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void onGoToMenu(View view) {
        viewCallback.onError();
    }
   public void onCloseSession(View view) {
        if (Device.canTreatAsBox()) {
            Dialogs.showTwoButtonsDialog(this.mActivity,R.string.accept ,  (R.string.cancel),  R.string.end_session_message,  new DialogListener() {
                public void onAccept() {
                    AccountDetailsViewModel.this.onCloseSession();
                }

                public void onCancel() {
                }
            });
        } else {
            onCloseSession();
        }
    }
    public void onCloseSession() {
        if (Connectivity.isConnected()) {
            this.isLoading.set(true);
            String theUser = DataManager.getInstance().getString("theUser", "");
            if (!TextUtils.isEmpty(theUser)) {
                String url=WebConfig.removeUserURL.replace("{USER}", ((User) new Gson().fromJson(theUser, User.class)).getName()).replace("{DEVICE_ID}",new Gson().fromJson(theUser,User.class).getDeviceId());
                NetManager.getInstance().makeStringRequest(url, new StringRequestListener() {
                    public void onCompleted(String response) {
                        if (response.toLowerCase().contains("success")) {
                            AccountDetailsViewModel.this.viewCallback.onCloseSessionSelected();
                        }
                    }

                    public void onError() {
                        AccountDetailsViewModel.this.isLoading.set(false);
                    }
                });
                return;
            }
            return;
        }
        this.viewCallback.onCloseSessionNoInternet();

    }



    @Override
    public void showAccountDetails(ActivityAccountBinding accountDetailsFragmentBinding) {
        String theUser = DataManager.getInstance().getString("theUser","");
        if(!TextUtils.isEmpty(theUser)) {
            User user = new Gson().fromJson(theUser, User.class);
            accountDetailsFragmentBinding.setUser(user);
        }
        else {
            viewCallback.onError();
        }
    }
}