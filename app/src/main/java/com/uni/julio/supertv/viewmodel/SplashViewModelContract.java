package com.uni.julio.supertv.viewmodel;
public interface SplashViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {
        //        void onLoginError();
        void onLoginCompleted(boolean success);
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void login();
    }
}