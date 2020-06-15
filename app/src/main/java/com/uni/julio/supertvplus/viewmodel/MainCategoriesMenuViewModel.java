package com.uni.julio.supertvplus.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.adapter.MainCategoryAdapter;
import com.uni.julio.supertvplus.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.MainCategorySelectedListener;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.utils.Device;
public class MainCategoriesMenuViewModel implements MainCategoriesMenuViewModelContract.ViewModel, MainCategorySelectedListener {

    private MainCategoriesMenuViewModelContract.View viewCallback;
    private Context mContext;
    private MainCategoryAdapter mainCategoryAdapter;
    private GridLayoutManager mLayoutManager;
    public ObservableBoolean isTV;

    public MainCategoriesMenuViewModel(Context context) {
        mContext = context;
          if(VideoStreamManager.getInstance().getMainCategoriesList().size()<=0)
           VideoStreamManager.getInstance().FillMainCategories();
        isTV = new ObservableBoolean (Device.canTreatAsBox());
    }

    @Override
    public void onViewResumed() {
        if(LiveTvApplication.getUser().getAdultos() == 1){
            if(VideoStreamManager.getInstance().getMainCategoriesList().size() > 9 && VideoStreamManager.getInstance().getMainCategoriesList().get(9).getId() == 7)
            {
                VideoStreamManager.getInstance().getMainCategoriesList().remove(9);
                mainCategoryAdapter.update(VideoStreamManager.getInstance().getMainCategoriesList());
            }
        }else{
            if(VideoStreamManager.getInstance().getMainCategoriesList().size() < 10
            || VideoStreamManager.getInstance().getMainCategoriesList().get(9).getId() != 7)
            {
                VideoStreamManager.getInstance().getMainCategoriesList().add(9,VideoStreamManager.getInstance().createMainCategory("Adultos", R.drawable.adults, ModelTypes.ADULTS_CATEGORIES, 7));
                mainCategoryAdapter.update(VideoStreamManager.getInstance().getMainCategoriesList());
            }
        }
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
        mainCategoryAdapter=new MainCategoryAdapter(mContext, mainCategoriesRV, viewCallback);
        GridLayoutManager manager = new GridLayoutManager(mContext, Integer.parseInt(mContext.getString(R.string.maincategory_column_num)));
        if(mContext.getString(R.string.maincategory_direction).equals("0")){
            manager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        else
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.supportsPredictiveItemAnimations();
        DefaultItemAnimator animator = new DefaultItemAnimator();
        mainCategoriesRV.setItemAnimator(animator);
        mainCategoriesRV.setLayoutManager(manager);
        mainCategoriesRV.setAdapter(mainCategoryAdapter);
        if (mainCategoriesRV.getItemDecorationCount() == 0)
            mainCategoriesRV.addItemDecoration(new RecyclerViewItemDecoration(20,mContext.getResources().getInteger(R.integer.main_padding),20,mContext.getResources().getInteger(R.integer.main_padding)));
    }

    @Override
    public void onMainCategorySelected(int position) {
         viewCallback.onMainCategorySelected(VideoStreamManager.getInstance().getMainCategory(position));
    }
}