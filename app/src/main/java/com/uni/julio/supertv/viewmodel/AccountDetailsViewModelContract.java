package com.uni.julio.supertv.viewmodel;

import com.uni.julio.supertv.databinding.ActivityAccountBinding;

public interface AccountDetailsViewModelContract {

    interface View extends Lifecycle.View {
        void onCloseSessionSelected();
        void onCloseSessionNoInternet();
        void onError();
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void showAccountDetails();
    }
}