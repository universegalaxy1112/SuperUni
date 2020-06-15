package com.uni.julio.supertvplus.viewmodel;

import com.uni.julio.supertvplus.databinding.ActivitySearchBinding;
import com.uni.julio.supertvplus.helper.TVRecyclerView;

public interface SearchViewModelContract{

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {//}, LoadMoviesForCategoryResponseListener {

    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieList(ActivitySearchBinding activitySearchBinding, TVRecyclerView moviesGridRV, String query, boolean searchSerie);
        void onConfigurationChanged();
    }
}