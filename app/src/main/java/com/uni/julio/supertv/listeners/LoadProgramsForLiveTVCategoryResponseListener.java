package com.uni.julio.supertv.listeners;


import com.uni.julio.supertv.model.LiveTVCategory;

public interface LoadProgramsForLiveTVCategoryResponseListener extends BaseResponseListener {
    void onProgramsForLiveTVCategoriesCompleted();
    void onProgramsForLiveTVCategoryCompleted(LiveTVCategory liveTVCategory);
    void onProgramsForLiveTVCategoryError(LiveTVCategory liveTVCategory);
}
