package com.uni.julio.supertv.viewmodel;

import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.MainCategory;

public interface MainCategoriesMenuViewModelContract {
     interface View extends Lifecycle.View {
         void onMainCategorySelected(MainCategory mainCategory);
        void onAccountPressed();
    }
     interface ViewModel extends Lifecycle.ViewModel {
        void showMainCategories(TVRecyclerView mainCategoriesRV);
     }
}