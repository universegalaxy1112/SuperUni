package com.uni.julio.supertv.viewmodel;

import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertv.listeners.EpisodeLoadListener;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;

public interface EpisodeDetailsViewModelContract {
    interface View extends Lifecycle.View {

        void onPlaySelected(Movie movie, int fromStart, int seasonPosition);
        void finishActivity();
        void showMovieDetails(Serie movie , int maincategory);

    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieDetails(Serie movie, ActivityMultiSeasonDetailBinding movieDetailsBinding, int mainCategoryId, EpisodeLoadListener episodeLoadListener);
    }
}