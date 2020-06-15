package com.uni.julio.supertvplus.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.LoadProgramsForLiveTVCategoryResponseListener;
import com.uni.julio.supertvplus.listeners.LoadSeasonsForSerieResponseListener;
import com.uni.julio.supertvplus.listeners.LoadSubCategoriesResponseListener;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.model.Season;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.utils.networing.NetManager;

import java.util.List;

public class LoadingMoviesViewModel implements LoadingMoviesViewModelContract.ViewModel, LoadSubCategoriesResponseListener, LoadSeasonsForSerieResponseListener, LoadProgramsForLiveTVCategoryResponseListener {

    private NetManager netManager;
    private LoadingMoviesViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    public LoadingMoviesViewModel() {
        netManager = NetManager.getInstance();
        videoStreamManager = VideoStreamManager.getInstance();
        Log.i("aa","videoStreamManager");
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (LoadingMoviesViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void loadSubCategories(int mainCategoryPosition) {
        try
        {
            if(videoStreamManager.getMainCategory(mainCategoryPosition).getModelType() == ModelTypes.LIVE_TV_CATEGORIES) {
                netManager.retrieveLiveTVPrograms(videoStreamManager.getMainCategory(mainCategoryPosition), (LoadProgramsForLiveTVCategoryResponseListener)this);
            }
            else {
                if(videoStreamManager.getMainCategory(mainCategoryPosition).getMovieCategories().size() > 0 && mainCategoryPosition != 8 ) {
                    viewCallback.onSubCategoriesForMainCategoryLoaded();
                }
                else {
                    netManager.retrieveSubCategories(videoStreamManager.getMainCategory(mainCategoryPosition), (LoadSubCategoriesResponseListener) this);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void loadSeasons(int mainCategoryId, Serie serie) {
        if(mainCategoryId == 6) {
            serie.addSeason(0, new Season());
            viewCallback.onSeasonsForSerieLoaded();
            return;
        }

        if(serie.getSeasonCount() > 0) {
            viewCallback.onSeasonsForSerieLoaded();
        }
        else {
            netManager.retrieveSeasons(serie,  this);
        }
    }

    @Override
    public void onError() {
        viewCallback.onError();
    }


    @Override
    public void onSubCategoriesLoaded(MainCategory mainCategory, List<MovieCategory> movieCategories) {

        MovieCategory movieCategory1=new MovieCategory();
        movieCategory1.setCatName("Favorite");
        int mainCategoryId = mainCategory.getId();
        if(!(mainCategoryId == 7 || mainCategoryId ==8 || mainCategoryId ==4 || mainCategoryId == 9))
            movieCategories.add(0, movieCategory1);
        videoStreamManager.getMainCategory(mainCategory.getId()).setMovieCategories(movieCategories);
        viewCallback.onSubCategoriesForMainCategoryLoaded();
    }

    @Override
    public void onSubCategoriesLoadedError() {
        viewCallback.onSubCategoriesForMainCategoryLoadedError();
    }

    @Override
    public void onSeasonsLoaded(Serie serie, int seasonCount) {
        for(int i = 0; i < seasonCount; i ++) {
            serie.addSeason(i, new Season());
        }
        viewCallback.onSeasonsForSerieLoaded();
    }

    @Override
    public void onSeasonsLoadedError() {
        viewCallback.onSeasonsForSerieLoadedError();
    }

    @Override
    public void onProgramsForLiveTVCategoriesCompleted() {
        viewCallback.onProgramsForLiveTVCategoriesLoaded();
    }

    @Override
    public void onProgramsForLiveTVCategoryCompleted(LiveTVCategory liveTVCategory) {
        videoStreamManager.setLiveTVCategory(liveTVCategory);
    }

    @Override
    public void onProgramsForLiveTVCategoryError(LiveTVCategory liveTVCategory) {
//        LiveTVCategory liveTVCategory is null
        viewCallback.onProgramsForLiveTVCategoriesLoadedError();
    }

//    @Override
//    public void onAllMoviesForSerieLoaded(Serie serie) {
//        videoStreamManager.addMoviesForSerie(serie, moviesForSerieMap);
//        viewCallback.onAllMoviesForSerieLoaded();
//    }
}