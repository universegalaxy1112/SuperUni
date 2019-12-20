package com.uni.julio.supertv.listeners;

import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public interface MoviesGridViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {//}, LoadMoviesForCategoryResponseListener {
        void onMovieAccepted(int selectedRow,Movie movie);//it needs the view to share the elements in the animation
        void onSerieAccepted(int selectedRow,Serie serie);//it needs the view to share the elements in the animation
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieList(TVRecyclerView moviesGridRV, int mainCategoryPosition, int movieCategoryPosition);
        void onConfigurationChanged();
     }
}