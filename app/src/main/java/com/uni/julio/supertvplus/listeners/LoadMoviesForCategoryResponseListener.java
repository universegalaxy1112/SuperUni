package com.uni.julio.supertvplus.listeners;


import com.uni.julio.supertvplus.model.MovieCategory;

public interface LoadMoviesForCategoryResponseListener extends BaseResponseListener {
    void onMoviesForCategoryCompleted(MovieCategory movieCategory);
    void onMoviesForCategoryCompletedError(MovieCategory movieCategory);
}
