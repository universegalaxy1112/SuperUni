package com.uni.julio.supertvplus.listeners;

import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;

public interface MoviesGridViewModelContract {

    interface View extends Lifecycle.View {

    }

    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieList(TVRecyclerView moviesGridRV, int mainCategoryPosition, int movieCategoryPosition);
     }
}