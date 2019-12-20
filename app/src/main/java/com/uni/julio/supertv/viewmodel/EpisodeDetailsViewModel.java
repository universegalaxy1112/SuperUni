package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.MultiSeasonAdapter;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LoadEpisodesForSerieResponseListener;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.listeners.SeasonSelectListener;
import com.uni.julio.supertv.model.Episode;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Season;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.networing.NetManager;

import java.util.ArrayList;
import java.util.List;

public class EpisodeDetailsViewModel implements EpisodeDetailsViewModelContract.ViewModel, LoadEpisodesForSerieResponseListener, SeasonSelectListener,MovieSelectedListener {
    private int mMainCategoryId ;
    private int mMovieCategoryId;
    private Movie mMovie;
    private Season season;
    private EpisodeDetailsViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    List<? extends VideoStream>  movieList;
    public ObservableBoolean isFavorite;
    public ObservableBoolean isSeen;
    private boolean isMovies = false;
    private boolean hidePlayFromStart = false;
    ActivityMultiSeasonDetailBinding movieDetailsBinding;
    MultiSeasonAdapter moviesRecyclerAdapter;
    TVRecyclerView rowsRecycler;
    GridLayoutManager rowslayoutmanger;
    Serie serie;
    TabLayout tabLayout;
    public EpisodeDetailsViewModel(Context context, int mainCategoryId) {
        videoStreamManager = VideoStreamManager.getInstance();
        this.mContext = context;
        mMainCategoryId=mainCategoryId;
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (EpisodeDetailsViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void showMovieDetails(Serie serie, ActivityMultiSeasonDetailBinding movieDetailsBinding , int mainCategoryId, int movieCategoryId) {
        this.mMainCategoryId=mainCategoryId;
        this.mMovieCategoryId=movieCategoryId;
        this.movieDetailsBinding=movieDetailsBinding;
        this.serie=serie;
        rowsRecycler = movieDetailsBinding.getRoot().findViewById(R.id.recycler_view);
        rowslayoutmanger = new GridLayoutManager(mContext, Integer.parseInt(mContext.getString(R.string.more_video)));
        rowslayoutmanger.setOrientation(LinearLayoutManager.VERTICAL);
        rowsRecycler.setLayoutManager(rowslayoutmanger);
        if (rowsRecycler.getItemDecorationCount() == 0) {
            rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(16,16,16,16));
        }
        if(this.serie.getSeasons().size()>0){
            addSeasonButtons();
            showSeasonList(0);
        }
    }
    private void addSeasonButtons(){
          tabLayout=movieDetailsBinding.detailTab;
        for(int i=0;i<serie.getSeasons().size();i++){

            TabItem tabItem=new TabItem(mContext);
            tabLayout.addView(tabItem);
            tabLayout.getTabAt(i).setText(serie.getSeason(i).getName());
            tabItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showSeasonList(tab.getPosition());
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
    public void showSeasonList(int seasonposition){
        season=serie.getSeason(seasonposition);
        List<Episode> episodeList=(List<Episode>)season.getEpisodeList();
        boolean needsRedraw=true;
        if(episodeList == null||episodeList.size()==0){
            if(!season.isLoading()){
                needsRedraw=false;
                NetManager.getInstance().retrieveEpisodesForSerie(serie,season,this);
            }
        }
        if(needsRedraw){
            movieList=season.getEpisodeList();
            showEpisode(0);
            moviesRecyclerAdapter = new MultiSeasonAdapter(mContext, rowsRecycler,movieList, 4, this);
            rowsRecycler.setAdapter(moviesRecyclerAdapter);
        }


    }
    public void finishActivity(View view) {
        viewCallback.finishActivity();
    }
    public void setProperty(){
        if(mMainCategoryId == 4) { //eventos
            hidePlayFromStart = true;
        }
        if(hidePlayFromStart) {
            isSeen = new ObservableBoolean(false);
        }
        else  {
            isSeen = new ObservableBoolean(videoStreamManager.isLocalSeen(String.valueOf(mMovie.getContentId())));
        }
        isFavorite = new ObservableBoolean(videoStreamManager.isLocalFavorite(String.valueOf(mMovie.getContentId())));
    }
    public void onClickFavorite(View view) {
        if(isFavorite.get()) {
            videoStreamManager.removeLocalFavorite(String.valueOf(mMovie.getContentId()));
            isFavorite.set(false);
        }
        else {
            videoStreamManager.setLocalFavorite(String.valueOf(mMovie.getContentId()));
            isFavorite.set(true);
        }
        isFavorite.notifyChange();
        DataManager.getInstance().saveData("favoriteMovies", videoStreamManager.getFavoriteMovies());

    }

    public void onClickPlayStart(View view) {
        onPlay(true);
    }

    public void onClickPlay(View view) {
        onPlay(false);
    }

    private void onPlay(boolean fromStart) {
        if(!videoStreamManager.getSeenMovies().contains(String.valueOf(mMovie.getContentId()))) {
            videoStreamManager.setLocalSeen(String.valueOf(mMovie.getContentId()));
            if(!hidePlayFromStart) {
                isSeen.set(true);
            }
        }
        isSeen.notifyChange();
        addRecentSerie();
        DataManager.getInstance().saveData("seenMovies", videoStreamManager.getSeenMovies());
        viewCallback.onPlaySelected(mMovie, fromStart);
    }

    private void addRecentMovies(Movie movie) {
        String recentMovies = DataManager.getInstance().getString("recentMovies","");
        if (TextUtils.isEmpty(recentMovies)) {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            DataManager.getInstance().saveData("recentMovies", new Gson().toJson(movies));
        }
        else {
            List<Movie> movieList = new Gson().fromJson(recentMovies, new TypeToken<List<Movie>>() { }.getType());
            boolean needsToAdd = true;
            for(Movie mov : movieList) {
                if(movie.getContentId() == mov.getContentId()) {
                    needsToAdd = false;
                    break;
                }
            }
            if(needsToAdd) {
                if(movieList.size() == 10) {
                    movieList.remove(9);
                }
                movieList.add(0,movie);
                DataManager.getInstance().saveData("recentMovies", new Gson().toJson(movieList));
            }
        }
    }

    private void addRecentSerie() {

        try {
            String lastSelectedSerie = DataManager.getInstance().getString("lastSerieSelected", null);
            if (lastSelectedSerie != null) {
                Serie serie = new Gson().fromJson(lastSelectedSerie, Serie.class);

                String recentSeries = "";

                String serieType = "";
                if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES) {
                    serieType = "recentSeries";
                } else {
                    serieType = "recentKidsSeries";
                }
                recentSeries = DataManager.getInstance().getString(serieType, "");

                if (TextUtils.isEmpty(recentSeries)) {
                    List<Serie> series = new ArrayList<>();
                    series.add(serie);

                    DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
                } else {
                    List<Serie> serieList = new Gson().fromJson(recentSeries, new TypeToken<List<Serie>>() {
                    }.getType());
                    boolean needsToAdd = true;
                    for (Serie ser : serieList) {
                        if (serie.getContentId() == ser.getContentId()) {
                            needsToAdd = false;
                            break;
                        }
                    }
                    if (needsToAdd) {
                        if (serieList.size() == 10) {
                            serieList.remove(9);
                        }
                        serieList.add(0, serie);
                        DataManager.getInstance().saveData(serieType, new Gson().toJson(serieList));
                    }
                }
            }
        }catch(Exception e) {}
    }

    public void showEpisode(int episodeposition){
         mMovie=(Movie)season.getEpisode(episodeposition);
         setProperty();
         movieDetailsBinding.setMovieDetailItem(mMovie);
        movieDetailsBinding.setMovieDetailsVM(this);
        movieDetailsBinding.scrollview.fullScroll(ScrollView.FOCUS_UP);
        movieDetailsBinding.playBtn.requestFocus();
        rowsRecycler.scrollToPosition(episodeposition);

    }
    @Override
    public void onEpisodesForSerieCompleted(Season mseason) {
        this.season=mseason;
        movieList=season.getEpisodeList();
        showEpisode(0);
        moviesRecyclerAdapter = new MultiSeasonAdapter(mContext, rowsRecycler,movieList, 4, this);
        rowsRecycler.setAdapter(moviesRecyclerAdapter);
    }
    @Override
    public void onError() {

    }
    @Override
    public void onMovieSelected(int selectedRow, int selectedEpisode) {
        showEpisode(selectedEpisode);
    }


}