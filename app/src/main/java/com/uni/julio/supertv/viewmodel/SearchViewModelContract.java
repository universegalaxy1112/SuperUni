package com.uni.julio.supertv.viewmodel;

import android.widget.EditText;

import com.uni.julio.supertv.databinding.ActivitySearchBinding;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;

public interface SearchViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {//}, LoadMoviesForCategoryResponseListener {
        void onMovieAccepted(int selectedRow,Movie movie);//it needs the view to share the elements in the animation
        void onSerieAccepted(int selectedRow,Serie serie);//it needs the view to share the elements in the animation
        void closeKeyboard();
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieList(ActivitySearchBinding activitySearchBinding, TVRecyclerView moviesGridRV, String query, boolean searchSerie);
        void onConfigurationChanged();
    }
}