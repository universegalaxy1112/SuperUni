package com.uni.julio.supertvplus.viewmodel;


import com.uni.julio.supertvplus.listeners.BaseResponseListener;
import com.uni.julio.supertvplus.model.Serie;

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
        void loadSeasons(int mainCategoryId, Serie serie);
//        void loadSettings();
    }
}