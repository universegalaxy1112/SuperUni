package com.uni.julio.supertvplus.viewmodel;


import androidx.annotation.NonNull;

public interface Lifecycle {

    interface View {

    }

    interface ViewModel {
        void onViewResumed();
        void onViewAttached(@NonNull Lifecycle.View viewCallback);
        void onViewDetached();
    }
}
