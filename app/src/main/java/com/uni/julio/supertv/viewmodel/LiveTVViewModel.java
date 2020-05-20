package com.uni.julio.supertv.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.uni.julio.supertv.BR;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.LiveCategoryAdapter;
import com.uni.julio.supertv.adapter.LivetvAdapterNew;
import com.uni.julio.supertv.databinding.ActivityLivetvnewBinding;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.listeners.LiveTVCategorySelectedListener;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.utils.Connectivity;

import java.util.ArrayList;
import java.util.List;

public class LiveTVViewModel implements LiveTVViewModelContract.ViewModel, LiveProgramSelectedListener, LiveTVCategorySelectedListener  {

    public ObservableBoolean isConnected;
    private LiveTVViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private LivetvAdapterNew rowsRecyclerAdapter;
    private ActivityLivetvnewBinding activityLiveBinding;
    private int mProgramPosition = 0;
    private int mCategoryPosition = 0;
    private boolean isFullscreen = false;
    public LiveTVViewModel(Context context ) {
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
    }

    @Override
    public void onViewResumed() {

    }
    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (LiveTVViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @SuppressLint("ResourceType")
    @Override
    public void showProgramList(ActivityLivetvnewBinding activityLiveBinding){
        this.activityLiveBinding=activityLiveBinding;
        List<LiveProgram> liveProgramList = videoStreamManager.getAllLivePrograms();
        rowsRecyclerAdapter =new LivetvAdapterNew(mContext,liveProgramList,activityLiveBinding.programmingRecycler,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityLiveBinding.programmingRecycler.setLayoutManager(mLayoutManager);
        activityLiveBinding.programmingRecycler.setAdapter(rowsRecyclerAdapter);
        final List<LiveTVCategory> categoryList = new ArrayList<>();
        LiveTVCategory allCat = new LiveTVCategory();
        allCat.setCatName("Todos");
        allCat.setPosition(-1);
        categoryList.add(allCat);
        categoryList.addAll(videoStreamManager.getLiveTVCategoriesList());
        LiveCategoryAdapter liveCategoryAdapter = new LiveCategoryAdapter(mContext, categoryList, activityLiveBinding.liveCategoryRecycler, this);
        GridLayoutManager mCategoryLayOutManager = new GridLayoutManager(mContext,1);
        mCategoryLayOutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityLiveBinding.liveCategoryRecycler.setLayoutManager(mCategoryLayOutManager);
        activityLiveBinding.liveCategoryRecycler.setAdapter(liveCategoryAdapter);
        activityLiveBinding.setVariable(BR.currentCategory,categoryList.get(0));

    }

    public void toggleFullscreen(View view) {
        if(isFullscreen) {
            minimize(null);
        }else{
            fullScreen(null);
        }
    }
    public void minimize(View view){
        if(!isFullscreen) return;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(activityLiveBinding.exoPlayerVirtual.getMeasuredWidth(), activityLiveBinding.exoPlayerVirtual.getMeasuredHeight());
        int margin = (int)(mContext.getResources().getDisplayMetrics().density*16);
        layoutParams.setMargins(margin,margin,20,20);
        activityLiveBinding.exoPlayer.setLayoutParams(layoutParams);
        activityLiveBinding.exoPlayer.findViewById(R.id.top_bar).setVisibility(View.GONE);
        isFullscreen = false;
        requestFocusToProgramRV();
    }

    public void fullScreen(View view){
        if(isFullscreen) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, displayMetrics.heightPixels);
        layoutParams.setMargins(0,0,0,0);
        activityLiveBinding.exoPlayer.setLayoutParams(layoutParams);
        activityLiveBinding.programmingRecycler.setVisibility(View.GONE);
        activityLiveBinding.liveCategoryRecycler.setVisibility(View.GONE);
        isFullscreen = true;
    }

    private void requestFocusToProgramRV() {
        activityLiveBinding.programmingRecycler.setVisibility(View.VISIBLE);
        activityLiveBinding.programmingRecycler.requestFocus();
        activityLiveBinding.programmingRecycler.scrollToPosition(mProgramPosition);
    }

    public void showCategories(View view) {
        if(isFullscreen) return;
        activityLiveBinding.programmingRecycler.setVisibility(View.GONE);
        activityLiveBinding.liveCategoryRecycler.setVisibility(View.VISIBLE);
        activityLiveBinding.programmingRecycler.clearFocus();
        activityLiveBinding.liveCategoryRecycler.requestFocus();
        activityLiveBinding.liveCategoryRecycler.scrollToPosition(mCategoryPosition);

    }

    private void showPrograms() {
        activityLiveBinding.programmingRecycler.setVisibility(View.VISIBLE);
        activityLiveBinding.liveCategoryRecycler.setVisibility(View.GONE);
        activityLiveBinding.liveCategoryRecycler.clearFocus();
        activityLiveBinding.programmingRecycler.requestFocus();
    }

    @Override
    public void onLiveTVCategorySelected(LiveTVCategory category) {
        if(category.getPosition() == -1)
            rowsRecyclerAdapter.updateChannels(videoStreamManager.getAllLivePrograms());
        else
            rowsRecyclerAdapter.updateChannels(videoStreamManager.getLiveTVCategory(category.getPosition()).getLivePrograms());
        activityLiveBinding.setVariable(BR.currentCategory,category);
        this.mCategoryPosition = category.getPosition() + 1;
        this.mProgramPosition = 0;
        showPrograms();
    }

    @Override
    public void onLiveProgramSelected(LiveProgram liveProgram, int programPosition) {
        viewCallback.onProgramAccepted(liveProgram);
        activityLiveBinding.setVariable(BR.currentProgram,liveProgram);
        this.mProgramPosition = programPosition;

    }

}