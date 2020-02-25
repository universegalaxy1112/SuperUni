package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.uni.julio.supertv.adapter.MoviesCategoryAdapter;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MovieAcceptedListener;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.listeners.SearchSelectedListener;
import com.uni.julio.supertv.listeners.ShowAsGridListener;
import com.uni.julio.supertv.model.Episode;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;

import java.util.List;

public class MoviesMenuViewModel implements MoviesMenuViewModelContract.ViewModel, MovieAcceptedListener, MovieSelectedListener, ShowAsGridListener, SearchSelectedListener {

    public ObservableBoolean isConnected;
    public ObservableBoolean isTV;
    private MoviesMenuViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private TVRecyclerView mCategoriesRecyclerview;
    private int mMainCategoryPosition = -1;
    private int mMovieCategoryPosition = -1;
    private int mSeriePosition = -1;
    private List<MovieCategory> mMoviesList;
    private List<MovieCategory> mMoviesList1;
    private ImageView imageView;
    private MoviesCategoryAdapter moviesCategoryAdapter;
    //mainView is the LoadingMoviesActivity Activity
    //context provided in LoadingMoviesActivity using getContext() from the MVContract.MainView interface
    public MoviesMenuViewModel(Context context) {
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        isTV = new ObservableBoolean(Device.canTreatAsBox());
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
    }

    @Override
    public void onViewResumed() {
        if(moviesCategoryAdapter != null) {
            moviesCategoryAdapter.onResume();
        }
    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (MoviesMenuViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void showMovieLists(TVRecyclerView categoriesRecyclerview, int mainCategoryPosition) {
        mCategoriesRecyclerview = categoriesRecyclerview;
        mMainCategoryPosition = mainCategoryPosition;
        mMoviesList = VideoStreamManager.getInstance().getMainCategory(mainCategoryPosition).getMovieCategories();
        moviesCategoryAdapter =new MoviesCategoryAdapter(mContext,categoriesRecyclerview,mMoviesList,mMainCategoryPosition,this,this,this,this);
        mLayoutManager = new GridLayoutManager(mContext,1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCategoriesRecyclerview.setLayoutManager(mLayoutManager);
        mCategoriesRecyclerview.setAdapter(moviesCategoryAdapter);
    }

    @Override
    public void showEpisodeLists(RecyclerView categoriesRecyclerview, int mainCategoryId, int movieCategoryId, int serieId) {

    }

    private void addRecentSerie(Serie serie) {
         DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
    }

    @Override
    public void onShowAsGridSelected(Integer position) {
        viewCallback.onShowAsGridSelected(position);
    }

    @Override
    public void onMovieSelected(int selectedRow, int selectedMovie) {
        onMovieAccepted(selectedRow, videoStreamManager.getMainCategory(mMainCategoryPosition).getMovieCategory(selectedRow).getMovie(selectedMovie));
    }

    public void onSearchClick(View view) {
        viewCallback.onSearchSelected(true);
    }
    @Override
    public void onSearchSelected(boolean isAccepted) {
        viewCallback.onSearchSelected(isAccepted);
    }

    @Override
    public void onMovieAccepted(int selectedRow, VideoStream video) {

        if( video instanceof  Serie) {
             addRecentSerie((Serie) video);
             viewCallback.onSerieAccepted(selectedRow,(Serie) video);
        }
        else if( video instanceof  Movie || video instanceof Episode) {
            viewCallback.onMovieAccepted(selectedRow,(Movie) video);
        }
    }
}