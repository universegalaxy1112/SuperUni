package com.uni.julio.supertvplus.viewmodel;

import com.uni.julio.supertvplus.databinding.ActivityOneseasonDetailBinding;
import com.uni.julio.supertvplus.model.CastDevice;
import com.uni.julio.supertvplus.model.Movie;

public interface MovieDetailsViewModelContract {
     interface View extends Lifecycle.View {

        void onPlaySelected(Movie movie, int fromStart);
        void finishActivity();
        void onDeviceLoaded(CastDevice castDevice);
        void showMovieDetails(Movie movie ,int maincategory);

     }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
         void showMovieDetails(Movie movie, ActivityOneseasonDetailBinding movieDetailsBinding,   int mainCategoryId);
    }
}