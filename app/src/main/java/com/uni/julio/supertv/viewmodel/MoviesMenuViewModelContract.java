package com.uni.julio.supertv.viewmodel;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;

public interface MoviesMenuViewModelContract {

    interface View extends Lifecycle.View {
        void onShowAsGridSelected(Integer position);
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void showMovieLists(TVRecyclerView categoriesRecyclerview, int mainCategoryPosition);
    }
}