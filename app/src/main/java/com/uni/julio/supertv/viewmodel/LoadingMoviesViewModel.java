package com.uni.julio.supertv.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LoadProgramsForLiveTVCategoryResponseListener;
import com.uni.julio.supertv.listeners.LoadSeasonsForSerieResponseListener;
import com.uni.julio.supertv.listeners.LoadSubCategoriesResponseListener;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.model.Season;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.networing.NetManager;

import java.util.List;

public class LoadingMoviesViewModel implements LoadingMoviesViewModelContract.ViewModel, LoadSubCategoriesResponseListener, LoadSeasonsForSerieResponseListener, LoadProgramsForLiveTVCategoryResponseListener {

    private NetManager netManager;
    private LoadingMoviesViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;

    //mainView is the LoadingMoviesActivity Activity
    //context provided in LoadingMoviesActivity using getContext() from the MVContract.MainView interface
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
    }

    @Override
    public void loadSeasons(int mainCategoryId, int movieCategoryId, int serieId) {
        Serie serie = (Serie) VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getMovieCategory(movieCategoryId).getMovie(serieId);
        //karaoke
        if(mainCategoryId == 6) {
            serie.addSeason(0, new Season());
            viewCallback.onSeasonsForSerieLoaded();
            return;
        }

        if(serie.getSeasonCount() > 0) {
            //Log.d("liveTV","DONE");
            viewCallback.onSeasonsForSerieLoaded();
        }
        else {
            //Log.d("liveTV","Get seasons");
            netManager.retrieveSeasons(serie, (LoadSeasonsForSerieResponseListener) this);
        }
    }

    @Override
    public void onError() {
        viewCallback.onError();
    }


    @Override
    public void onSubCategoriesLoaded(MainCategory mainCategory, List<MovieCategory> movieCategories) {

        //remove 4K from the list
        MovieCategory movieCategory1=new MovieCategory();
        movieCategory1.setCatName("Favorite");
        movieCategories.add(0,movieCategory1);

        videoStreamManager.getMainCategory(mainCategory.getId()).setMovieCategories(movieCategories);

//        mainCategory.setMovieCategories(movieCategories);
        viewCallback.onSubCategoriesForMainCategoryLoaded();
    }

    @Override
    public void onSubCategoriesLoadedError() {
        viewCallback.onSubCategoriesForMainCategoryLoadedError();
    }

    @Override
    public void onSeasonsLoaded(Serie serie, int seasonCount) {
        //Log.d("liveTV","onSeasonsLoaded "+seasonCount);
       //fill serie with empty seasons
        ;//Log.d("liveTV" , "onSeasonsLoaded "+serie.getSeasonCount());
        for(int i = 0; i < seasonCount; i ++) {
            serie.addSeason(i, new Season());
        }

        ;//Log.d("liveTV" , "Added "+serie.getSeasonCount());
        viewCallback.onSeasonsForSerieLoaded();
//        videoStreamManager.addMoviesForSerie(serie, moviesForSerieMap);
//        viewCallback.onAllMoviesForSerieLoaded();
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