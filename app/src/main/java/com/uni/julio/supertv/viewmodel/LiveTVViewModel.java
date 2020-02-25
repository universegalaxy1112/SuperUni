package com.uni.julio.supertv.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.LivetvAdapter;
import com.uni.julio.supertv.databinding.ActivityLiveBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.listeners.LiveTVCategorySelectedListener;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.utils.Connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LiveTVViewModel implements LiveTVViewModelContract.ViewModel, LiveProgramSelectedListener, LiveTVCategorySelectedListener  {

    public ObservableBoolean isConnected;
    private LiveTVViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
     private TVRecyclerView mProgramRV;
     private LivetvAdapter rowsRecyclerAdapter;
     private int currentCategory=0;
    public static int lastContentIdSelected = -1;
    private int lastProgramPosition;
    private ActivityLiveBinding activityLiveBinding;
    TabLayout tabLayout;
    public LiveTVViewModel(Context context ) {
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
        lastContentIdSelected = -1;
        lastProgramPosition = 0;
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
    public void showProgramList(ActivityLiveBinding activityLiveBinding){
        this.activityLiveBinding=activityLiveBinding;

        mProgramRV = activityLiveBinding.getRoot().findViewById(R.id.programming_recycler);
        List<LiveProgram> liveProgramList = videoStreamManager.getAllLivePrograms();

        rowsRecyclerAdapter =new LivetvAdapter(mContext,liveProgramList,mProgramRV,this);
        mLayoutManager = new GridLayoutManager(mContext,1);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mProgramRV.setLayoutManager(mLayoutManager);
        mProgramRV.setAdapter(rowsRecyclerAdapter);
        if (mProgramRV.getItemDecorationCount() == 0) {
            mProgramRV.addItemDecoration(new RecyclerViewItemDecoration(8,32,16,0));
        }
        final List<LiveTVCategory> categoryList = new ArrayList<>();
        LiveTVCategory allCat = new LiveTVCategory();
        allCat.setCatName("Todos");
        allCat.setPosition(-1);

        categoryList.add(allCat);
        categoryList.addAll(videoStreamManager.getLiveTVCategoriesList());
        tabLayout=activityLiveBinding.categoryTab;
        for(int i=0;i<categoryList.size();i++){

            TabItem tabItem=new TabItem(mContext);
            tabLayout.addView(tabItem);
            tabLayout.getTabAt(i).setText(categoryList.get(i).getCatName());
         }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentCategory = tab.getPosition();
                onLiveTVCategorySelected(categoryList.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onConfigurationChanged() {

    }

    @Override
    public void onLiveProgramSelected(LiveProgram liveProgram, int programPosition) {
        int hideTimeout=3000;
        if(liveProgram.getContentId()==lastContentIdSelected){
            hideTimeout=1;
        }else
            {
            viewCallback.onProgramAccepted(liveProgram);
        }
        lastContentIdSelected=liveProgram.getContentId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideChannels();
            }
        },hideTimeout);

    }

    @Override
    public void onLiveTVCategorySelected(LiveTVCategory category) {
         if(category.getPosition() == -1) {
            rowsRecyclerAdapter.updateChannels(videoStreamManager.getAllLivePrograms());
        }
        else {
            rowsRecyclerAdapter.updateChannels(videoStreamManager.getLiveTVCategory(category.getPosition()).getLivePrograms());
        }
    }

    public void showChannels() {
        Animation bottomUp = AnimationUtils.loadAnimation(LiveTvApplication.getAppContext(), R.anim.show_to_bottom);
        Animation upbottom = AnimationUtils.loadAnimation(LiveTvApplication.getAppContext(), R.anim.show_from_bottom);
        //activityLiveBinding.getRoot().findViewById(R.id.live_category_tab).startAnimation(bottomUp);
        activityLiveBinding.getRoot().findViewById(R.id.live_programs).startAnimation(upbottom);
        activityLiveBinding.getRoot().findViewById(R.id.live_category_tab).setVisibility(View.VISIBLE);
        activityLiveBinding.getRoot().findViewById(R.id.live_programs).setVisibility(View.VISIBLE);


    }

    public void hideChannels() {
        Animation bottomUp = AnimationUtils.loadAnimation(LiveTvApplication.getAppContext(), R.anim.show_from_bottom);
        Animation upbottom = AnimationUtils.loadAnimation(LiveTvApplication.getAppContext(), R.anim.show_to_bottom);
        //activityLiveBinding.getRoot().findViewById(R.id.live_category_tab).startAnimation(bottomUp);
        activityLiveBinding.getRoot().findViewById(R.id.live_programs).startAnimation(upbottom);
        activityLiveBinding.getRoot().findViewById(R.id.live_category_tab).setVisibility(View.GONE);
        activityLiveBinding.getRoot().findViewById(R.id.live_programs).setVisibility(View.GONE);
     }

    public void toggleChannels() {
        if(activityLiveBinding.getRoot().findViewById(R.id.live_category_tab).getVisibility()== View.VISIBLE){
             hideChannels();
        }
        else{
            showChannels();
        }
    }

    public void toggleCategoryOption() {

    }





}