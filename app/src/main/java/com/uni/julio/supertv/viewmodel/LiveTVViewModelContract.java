package com.uni.julio.supertv.viewmodel;

import com.uni.julio.supertv.databinding.ActivityLiveBinding;
import com.uni.julio.supertv.model.LiveProgram;

public interface LiveTVViewModelContract {

    //this will have methods that the ViewModel will call from the Activity/Fragment to update it's view
    interface View extends Lifecycle.View {//}, LoadMoviesForCategoryResponseListener {
        void onProgramAccepted(LiveProgram liveProgram);//it needs the view to share the elements in the animation
        void showActionBar();
        void hideActionBar();
    }

    //this will have methods that the activity/fragment will call from the ViewModel
    interface ViewModel extends Lifecycle.ViewModel {
        void showProgramList(ActivityLiveBinding activityLiveBinding);
        void onConfigurationChanged();
        void toggleCategoryOption();
//        void showEpisodesList(RecyclerView moviesGridRV, int seriePosition, int season);
    }
}