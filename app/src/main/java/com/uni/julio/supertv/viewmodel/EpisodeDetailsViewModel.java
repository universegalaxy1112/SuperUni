package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.MultiSeasonAdapter;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.EpisodeLoadListener;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EpisodeDetailsViewModel implements EpisodeDetailsViewModelContract.ViewModel, LoadEpisodesForSerieResponseListener, SeasonSelectListener,MovieSelectedListener {
    private int mMainCategoryId ;
    private int mMovieCategoryId;
    private Movie mMovie;
    private Season season;
    private int seasonPosition = 0;
    private EpisodeDetailsViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    List<? extends VideoStream>  movieList;
    public ObservableBoolean isFavorite;
    public ObservableBoolean isSeen;
    private boolean isMovies = false;
    private boolean isSerie = false;
    public ObservableBoolean isHD;
    public ObservableBoolean isSD;
    public ObservableBoolean isTrailer;
    private boolean hidePlayFromStart = false;
    ActivityMultiSeasonDetailBinding movieDetailsBinding;
    MultiSeasonAdapter moviesRecyclerAdapter;
    TVRecyclerView rowsRecycler;
    GridLayoutManager rowslayoutmanger;
    Serie serie;
    TabLayout tabLayout;
    EpisodeLoadListener episodeLoadListener;
    public EpisodeDetailsViewModel(Context context, int mainCategoryId) {
        videoStreamManager = VideoStreamManager.getInstance();
        this.mContext = context;
        mMainCategoryId=mainCategoryId;
        if(mainCategoryId == 0) { //is the position for the movies
            isMovies = true;
        }
        else if(mainCategoryId == 1 || mainCategoryId == 2 || mainCategoryId == 6) { //is the position for the series or series jids
            isSerie = true;
            mMainCategoryId = mainCategoryId;
        }
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
    public void showMovieDetails(Serie serie, ActivityMultiSeasonDetailBinding movieDetailsBinding , int mainCategoryId, int movieCategoryId, EpisodeLoadListener episodeLoadListener) {
        this.mMainCategoryId=mainCategoryId;
        this.mMovieCategoryId=movieCategoryId;
        this.movieDetailsBinding=movieDetailsBinding;
        this.episodeLoadListener=episodeLoadListener;
        this.serie=serie;
        ImageView fondoImage=movieDetailsBinding.fondoUrl;
        try{
            if(!TextUtils.isEmpty(serie.getHDFondoUrl()) && !serie.getHDFondoUrl().equals(" ") && !serie.getHDFondoUrl().equals("")) {
                Picasso.get().load(serie.getHDFondoUrl()).placeholder(R.drawable.placeholder).into(fondoImage);
            }
        }catch (IllegalArgumentException e){

        }

        rowsRecycler = movieDetailsBinding.getRoot().findViewById(R.id.recycler_view);
        rowslayoutmanger = new GridLayoutManager(mContext, Integer.parseInt(mContext.getString(R.string.episode)));
        rowslayoutmanger.setOrientation(LinearLayoutManager.VERTICAL);
        rowsRecycler.setLayoutManager(rowslayoutmanger);
        if (rowsRecycler.getItemDecorationCount() == 0) {
            rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(6,16,6,16));
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
            tabItem.setPadding(2,0,2,0);

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
        seasonPosition = seasonposition;
        season=serie.getSeason(seasonposition);
        List<Episode> episodeList=(List<Episode>)season.getEpisodeList();
        boolean needsRedraw=true;
        if(episodeList == null||episodeList.size()==0){
            if(!season.isLoading()){
                needsRedraw=false;
                this.episodeLoadListener.showCustomProgress();
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
        isHD=mMovie.getStreamUrl()==null||mMovie.getStreamUrl().equals("null")||mMovie.getStreamUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isSD=mMovie.getSDUrl()==null||mMovie.getSDUrl().equals("null")||mMovie.getSDUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isTrailer=mMovie.getTrailerUrl()==null||mMovie.getTrailerUrl().equals("null")||mMovie.getTrailerUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isHD.notifyChange();
        isSD.notifyChange();
        isTrailer.notifyChange();
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
        DataManager.getInstance().saveData("favoriteMoviesTotal", videoStreamManager.getFavoriteMovies());
        addFavorite();
     }
    public void play(View view){
        if(!isSD.get()){
            final MaterialDialog dialog=new MaterialDialog.Builder(mContext)
                    .customView(R.layout.castasklayout,false)
                    .contentLineSpacing(0)
                    .theme(Theme.LIGHT)
                    .backgroundColor(mContext.getResources().getColor(R.color.white))
                    .show();
            TextView hd= dialog.getCustomView().findViewById(R.id.playHD);
            TextView sd= dialog.getCustomView().findViewById(R.id.playSD);
            hd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onPlay(0);
                }
            });
            sd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onPlay(1);
                }
            });
        }else{
            onPlay(0);
        }

    }

    private void addFavorite(){
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES) {
            serieType = "favoriteSerie";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_KIDS_CATEGORIES) {
            serieType = "favoriteKids";
        }else{
            serieType = "favoriteKara";
        }
        String favoriteSeries=DataManager.getInstance().getString(serieType,"");
        Serie newserie=new Serie();
        newserie=serie.clone();
        newserie.setSeasons(new ArrayList<Season>());
        if(TextUtils.isEmpty(favoriteSeries)){
            List<Serie> series=new ArrayList<>();
            if(checkNeedAdd()){
                series.add(newserie);
                DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
            }
        }
        else{
             List<Serie> series=new Gson().fromJson(favoriteSeries,new TypeToken<List<Serie>>(){}.getType());
            if(checkNeedAdd()){
                for(Serie serie1:series){
                    if(serie1.getContentId()==serie.getContentId()) return ;
                }
                series.add(0,newserie);
            }else{
                if(series.contains(newserie))
                series.remove(newserie);
            }
            DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
        }

    }

    private boolean checkNeedAdd(){
        List<Season> seasons;
        seasons=this.serie.getSeasons();
        for(Season season:seasons){
            List<? extends VideoStream>  movieList=season.getEpisodeList();
            for(VideoStream movie:movieList){
               if(videoStreamManager.isLocalFavorite(String.valueOf(movie.getContentId()))) return true;
            }
        }
     return false;
    }
    public void playTrailor(View view) {
            onPlay(2);
    }
    public void playHD(View view){
        onPlay(0);
    }
    public void playSD(View view){
        onPlay(1);
    }
    private void onPlay(int type) {
        if(!videoStreamManager.getSeenMovies().contains(String.valueOf(mMovie.getContentId()))) {
            videoStreamManager.setLocalSeen(String.valueOf(mMovie.getContentId()));
            if(!hidePlayFromStart) {
                isSeen.set(true);
            }
        }
        isSeen.notifyChange();
        addRecentSerie();
        DataManager.getInstance().saveData("seenMovies", videoStreamManager.getSeenMovies());
        viewCallback.onPlaySelected(mMovie, type, seasonPosition);
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
                } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_KIDS_CATEGORIES) {
                    serieType = "recentKids";
                }else{
                    serieType = "recentKara";
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
        movieDetailsBinding.play.requestFocus();
        rowsRecycler.scrollToPosition(episodeposition);

    }
    @Override
    public void onEpisodesForSerieCompleted(Season mseason) {
        this.season=mseason;
        movieList=season.getEpisodeList();
        showEpisode(0);
        moviesRecyclerAdapter = new MultiSeasonAdapter(mContext, rowsRecycler,movieList, 4, this);
        rowsRecycler.setAdapter(moviesRecyclerAdapter);
        this.episodeLoadListener.onLoaded();

    }
    @Override
    public void onError() {
        this.episodeLoadListener.onError();
    }
    @Override
    public void onMovieSelected(int selectedRow, int selectedEpisode) {
        showEpisode(selectedEpisode);
    }

}