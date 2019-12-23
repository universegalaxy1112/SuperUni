package com.uni.julio.supertv.viewmodel;

import android.app.ProgressDialog;

public interface SplashViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {
        //        void onLoginError();
        void onCheckForUpdateCompleted(boolean z, String str);
        void onDownloadUpdateCompleted(String str);

        void onDownloadUpdateError(int i);
        void onLoginCompleted(boolean success);
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void checkForUpdate();

        void downloadUpdate(String str, ProgressDialog progressDialog);
        void login();
    }
}