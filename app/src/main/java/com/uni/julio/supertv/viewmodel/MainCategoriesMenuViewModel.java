package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.MainCategoryAdapter;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MainCategorySelectedListener;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.utils.Device;

import java.util.List;
public class MainCategoriesMenuViewModel implements MainCategoriesMenuViewModelContract.ViewModel, MainCategorySelectedListener {

    private MainCategoriesMenuViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private MainCategoryAdapter mainCategoryAdapter;
    private TVRecyclerView mMainCategoriesRV;
    private List<MainCategory> mainCategoriesList;
    private GridLayoutManager mLayoutManager;
    public ObservableBoolean isTV;

    public MainCategoriesMenuViewModel(Context context) {
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;

          if(VideoStreamManager.getInstance().getMainCategoriesList().size()<=0)
           VideoStreamManager.getInstance().FillMainCategories();
        isTV = new ObservableBoolean (Device.canTreatAsBox());
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
            this.viewCallback = (MainCategoriesMenuViewModelContract.View) viewCallback;
    }
    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }
    @Override
    public void showMainCategories(TVRecyclerView mainCategoriesRV) {
        mMainCategoriesRV=mainCategoriesRV;
        mainCategoriesList = VideoStreamManager.getInstance().getMainCategoriesList();
        mainCategoryAdapter=new MainCategoryAdapter(mContext, mainCategoriesRV, mainCategoriesList,viewCallback);
        GridLayoutManager manager = new GridLayoutManager(mContext, Integer.parseInt(mContext.getString(R.string.maincategory_column_num)));
        if(mContext.getString(R.string.maincategory_direction).equals("0")){
            manager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        else
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMainCategoriesRV.setLayoutManager(manager);
        mMainCategoriesRV.setAdapter(mainCategoryAdapter);
        mMainCategoriesRV.addItemDecoration(new RecyclerViewItemDecoration(20,mContext.getResources().getInteger(R.integer.main_padding),20,mContext.getResources().getInteger(R.integer.main_padding)));
    }



    public void onAccountClick(View view) {
        viewCallback.onAccountPressed();
    }
    @Override
    public void onMainCategorySelected(int position) {
         viewCallback.onMainCategorySelected(mainCategoriesList.get(position));
    }

}