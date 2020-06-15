package com.uni.julio.supertvplus.listeners;

import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.MovieCategory;

import java.util.List;

public interface LoadSubCategoriesResponseListener extends BaseResponseListener {
    void onSubCategoriesLoaded(MainCategory mainCategory, List<MovieCategory> movieCategories);
    void onSubCategoriesLoadedError();
}
