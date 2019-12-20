package com.uni.julio.supertv.listeners;


import com.uni.julio.supertv.model.MovieCategory;

public interface LoadMoviesForCategoryResponseListener extends BaseResponseListener {
    void onMoviesForCategoryCompleted(MovieCategory movieCategory);
    void onMoviesForCategoryCompletedError(MovieCategory movieCategory);
}
