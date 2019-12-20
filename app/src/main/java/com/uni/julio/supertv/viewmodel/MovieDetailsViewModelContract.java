package com.uni.julio.supertv.viewmodel;

import com.uni.julio.supertv.databinding.ActivityOneseaosnDetailBinding;
import com.uni.julio.supertv.model.Movie;

public interface MovieDetailsViewModelContract {
     interface View extends Lifecycle.View {

        void onPlaySelected(Movie movie, int fromStart);
        void finishActivity();
        void showMovieDetails(Movie movie ,int maincategory, int moviecategory);
        void onMovieSelected(int selectedRow,int SelectedMovie);

     }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
         void showMovieDetails(Movie movie, ActivityOneseaosnDetailBinding movieDetailsBinding,   int mainCategoryId,int movieCategoryId);
    }
}