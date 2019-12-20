package com.uni.julio.supertv.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.GridViewAdapter;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.listeners.MoviesGridViewModelContract;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;

import java.util.List;

public class MoviesGridViewModel implements MoviesGridViewModelContract.ViewModel, MovieSelectedListener {

    public ObservableBoolean isConnected;
    private MoviesGridViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private int mMainCategoryPosition;
    private int mMovieCategoryPosition;
    private int mSerieId = -1;
    private int mSeasonId = -1;

    public MoviesGridViewModel(Context context, ModelTypes.SelectedType catPosition) {
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
        this.viewCallback = (MoviesGridViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }


     @Override
    public void showMovieList(TVRecyclerView moviesGridRV, int mainCategoryPosition, int movieCategoryPosition) {
         boolean showTitles = false;
         this.mMovieCategoryPosition=movieCategoryPosition;
         this.mMainCategoryPosition=mainCategoryPosition;
        List<Movie> movies;
        movies=(List<Movie>)videoStreamManager.getMainCategory(mainCategoryPosition).getMovieCategories().get(movieCategoryPosition).getMovieList();
        GridViewAdapter moreVideoAdapter=new GridViewAdapter(mContext,moviesGridRV,movies,movieCategoryPosition,this);
        mLayoutManager=new GridLayoutManager(mContext,Integer.parseInt(mContext.getString(R.string.more_video)));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moviesGridRV.setLayoutManager(mLayoutManager);
        moviesGridRV.setAdapter(moreVideoAdapter);
     if (moviesGridRV.getItemDecorationCount() == 0) {
         moviesGridRV.addItemDecoration(new RecyclerViewItemDecoration(48,16,0,16));
     }
    }

    @Override
    public void onConfigurationChanged() {

    }

    @Override
    public void onMovieSelected(int selectedRow, int selectedMovie) {
        VideoStream movie =   videoStreamManager.getMainCategory(mMainCategoryPosition).getMovieCategories().get(mMovieCategoryPosition).getMovie(selectedMovie);
        if(movie instanceof Serie)
        {
            addRecentSerie((Serie)movie);
            viewCallback.onSerieAccepted(selectedRow,(Serie)movie);
        }
        else if(movie instanceof Movie)
        {
            viewCallback.onMovieAccepted(selectedRow,(Movie)movie);
        }
    }

    private void addRecentSerie(Serie serie) {
        //just save the Serie in localPreferences, for future use
        DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
    }
}