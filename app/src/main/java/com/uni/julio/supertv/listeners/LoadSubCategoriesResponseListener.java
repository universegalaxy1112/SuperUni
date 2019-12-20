package com.uni.julio.supertv.listeners;

import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.MovieCategory;

import java.util.List;

public interface LoadSubCategoriesResponseListener extends BaseResponseListener {
    void onSubCategoriesLoaded(MainCategory mainCategory, List<MovieCategory> movieCategories);
    void onSubCategoriesLoadedError();
}
