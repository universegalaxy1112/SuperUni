package com.uni.julio.supertvplus.viewmodel;

import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.model.MainCategory;

public interface MainCategoriesMenuViewModelContract {
     interface View extends Lifecycle.View {
         void onMainCategorySelected(MainCategory mainCategory);
        void onAccountPressed();
    }
     interface ViewModel extends Lifecycle.ViewModel {
        void showMainCategories(TVRecyclerView mainCategoriesRV);
     }
}