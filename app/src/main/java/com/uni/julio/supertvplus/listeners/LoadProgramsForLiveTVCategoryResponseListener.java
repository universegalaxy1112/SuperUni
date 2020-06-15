package com.uni.julio.supertvplus.listeners;


import com.uni.julio.supertvplus.model.LiveTVCategory;

public interface LoadProgramsForLiveTVCategoryResponseListener extends BaseResponseListener {
    void onProgramsForLiveTVCategoriesCompleted();
    void onProgramsForLiveTVCategoryCompleted(LiveTVCategory liveTVCategory);
    void onProgramsForLiveTVCategoryError(LiveTVCategory liveTVCategory);
}
