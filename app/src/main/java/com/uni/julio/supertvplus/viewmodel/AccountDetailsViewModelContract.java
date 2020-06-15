package com.uni.julio.supertvplus.viewmodel;


public interface AccountDetailsViewModelContract {

    interface View extends Lifecycle.View {
        void onCloseSessionSelected();
        void onCloseSessionNoInternet();
        void onError();
    }
    interface ViewModel extends Lifecycle.ViewModel {
        void showAccountDetails();
    }
}