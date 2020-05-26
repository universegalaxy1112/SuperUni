package com.uni.julio.supertv.listeners;

import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public interface MoviesGridViewModelContract {

    interface View extends Lifecycle.View {

    }

    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieList(TVRecyclerView moviesGridRV, int mainCategoryPosition, int movieCategoryPosition);
     }
}