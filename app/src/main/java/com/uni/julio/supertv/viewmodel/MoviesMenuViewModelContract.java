package com.uni.julio.supertv.viewmodel;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;

public interface MoviesMenuViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {//}, LoadMoviesForCategoryResponseListener {
        void onMovieAccepted(int selectedRow,Movie movie);//it needs the view to share the elements in the animation
        void onShowAsGridSelected(Integer position);
        void onSerieAccepted(int selectedRow, Serie serie);//it needs the view to share the elements in the animation
        void onSearchSelected(boolean isAccepted);
//        void onMovieAccepted(Bundle extras, android.view.View view);//callback method here
//        Context getContext(); //MainView should ALWAYS provide its context in getContext
//        void onMoviesForCategoryLoadedError();//callback method here
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        //void loadMoviesForCategory(MainCategory category);
        void showMovieLists(ImageView imageView,TVRecyclerView categoriesRecyclerview, int mainCategoryPosition);
        void showEpisodeLists(RecyclerView categoriesRecyclerview, int mainCategoryId, int movieCategoryId, int serieId);
    }
}