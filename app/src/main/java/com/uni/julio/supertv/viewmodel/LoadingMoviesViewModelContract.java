package com.uni.julio.supertv.viewmodel;


import com.uni.julio.supertv.listeners.BaseResponseListener;

public interface LoadingMoviesViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View, BaseResponseListener {
        void onSubCategoriesForMainCategoryLoaded();
        void onSubCategoriesForMainCategoryLoadedError();
        void onSeasonsForSerieLoaded();
        void onSeasonsForSerieLoadedError();
        void onProgramsForLiveTVCategoriesLoaded();
        void onProgramsForLiveTVCategoriesLoadedError();
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void loadSubCategories(int mainCategoryPosition);
        void loadSeasons(int mainCategoryId, int movieCategoryId, int seriePosition);
//        void loadSettings();
    }
}