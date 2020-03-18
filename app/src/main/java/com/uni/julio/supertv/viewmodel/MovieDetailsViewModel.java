package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.OneSeasonAdapter;
import com.uni.julio.supertv.databinding.ActivityOneseasonDetailBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieDetailsViewModel implements MovieDetailsViewModelContract.ViewModel, MovieSelectedListener {

    private int mMainCategoryId = 0;
    private MovieDetailsViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    List<? extends VideoStream>  movieList;
    private Movie mMovie;
     public ObservableBoolean isFavorite;
    public ObservableBoolean isSeen;
    private boolean isMovies = false;
    private boolean isSerie=false;
    private boolean hidePlayFromStart = false;
    public ObservableBoolean isHD;
    public ObservableBoolean isSD;
    public ObservableBoolean isTrailer;
    private ActivityOneseasonDetailBinding movieDetailsBinding;
    public MovieDetailsViewModel(Context context, int mainCategoryId) {
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
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
        this.viewCallback = (MovieDetailsViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void showMovieDetails(Movie movie, ActivityOneseasonDetailBinding movieDetailsBinding , int mainCategoryId,int movieCategoryId) {
        mMainCategoryId=mainCategoryId;
        this.movieDetailsBinding=movieDetailsBinding;
        if(mainCategoryId == 4) { //eventos
            hidePlayFromStart = true;
        }
        if(hidePlayFromStart) {
            isSeen = new ObservableBoolean(false);
        }
        else {
            isSeen = new ObservableBoolean(videoStreamManager.isLocalSeen(String.valueOf(movie.getContentId())));
        }
        isHD=movie.getStreamUrl()==null||movie.getStreamUrl().equals("null")||movie.getStreamUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isSD=(movie.getSDUrl()==null||movie.getSDUrl().equals("null")||movie.getSDUrl().equals(""))?new ObservableBoolean(true):new ObservableBoolean(false);
        isTrailer=movie.getTrailerUrl()==null||movie.getTrailerUrl().equals("null")||movie.getTrailerUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isFavorite = new ObservableBoolean(videoStreamManager.isLocalFavorite(String.valueOf(movie.getContentId())));
        isHD.notifyChange();
        isSD.notifyChange();
        isTrailer.notifyChange();
        mMovie = movie;
        movieDetailsBinding.setMovieDetailItem(movie);
        /*TVRecyclerView rowsRecycler = movieDetailsBinding.getRoot().findViewById(R.id.recycler_view);
        GridLayoutManager rowslayoutmanger = new GridLayoutManager(mContext, 1);
        rowslayoutmanger.setOrientation(LinearLayoutManager.HORIZONTAL);
        movieList= videoStreamManager.getMainCategory(mainCategoryId).getMovieCategory(movieCategoryId).getMovieList();
        OneSeasonAdapter moviesRecyclerAdapter = new OneSeasonAdapter(mContext, rowsRecycler,movieList, movieCategoryId, this);
        rowsRecycler.setLayoutManager(rowslayoutmanger);
        rowsRecycler.setAdapter(moviesRecyclerAdapter);
        if (rowsRecycler.getItemDecorationCount() == 0) {
            rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(24,12,24,12));
        }*/
    }

     public void finishActivity(View view) {
        viewCallback.finishActivity();
     }

    public void onClickFavorite(View view) {
        if(isFavorite.get()) {
             videoStreamManager.removeLocalFavorite(String.valueOf(mMovie.getContentId()));
             isFavorite.set(false);
             removeFavorite(mMovie);
        }
        else {
             videoStreamManager.setLocalFavorite(String.valueOf(mMovie.getContentId()));
             isFavorite.set(true);
             addFavorite(mMovie);
        }
        isFavorite.notifyChange();
        DataManager.getInstance().saveData("favoriteMoviesTotal", videoStreamManager.getFavoriteMovies());
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
    public void playHD(View view){
        onPlay(0);
    }
    public void playSD(View view){
        onPlay(1);
    }
    public void playTrailor(View view) {
        onPlay(2);
    }
    private void onPlay(int type) {
        if(!videoStreamManager.getSeenMovies().contains(String.valueOf(mMovie.getContentId()))) {
            videoStreamManager.setLocalSeen(String.valueOf(mMovie.getContentId()));
            if(!hidePlayFromStart) {
                isSeen.set(true);
            }
            addRecentMovies(mMovie);
            isSeen.notifyChange();
            DataManager.getInstance().saveData("seenMovies", videoStreamManager.getSeenMovies());
        }
        viewCallback.onPlaySelected(mMovie, type);
    }
    public void like(View view){

    }
    public void dislike(View view){

    }
    private void addFavorite(Movie movie){
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.MOVIE_CATEGORIES) {
            serieType = "favoriteMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.ENTERTAINMENT_CATEGORIES){
            serieType = "favoriteEntertainment";
        }
       String favoriteMovies=DataManager.getInstance().getString(serieType,"");
       if(TextUtils.isEmpty(favoriteMovies)){
           List<Movie> movies=new ArrayList<>();
           movies.add(movie);
           DataManager.getInstance().saveData(serieType,new Gson().toJson(movies));
       }
       else{
           List<Movie> movieList=new Gson().fromJson(favoriteMovies,new TypeToken<List<Movie>>(){}.getType());
           movieList.add(0,movie);
           DataManager.getInstance().saveData(serieType,new Gson().toJson(movieList));

       }
    }
    private void removeFavorite(Movie movie){
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.MOVIE_CATEGORIES) {
            serieType = "favoriteMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.ENTERTAINMENT_CATEGORIES){
            serieType = "favoriteEntertainment";
        }
        String favoriteMovies=DataManager.getInstance().getString(serieType,"");
        if(TextUtils.isEmpty(favoriteMovies)){
            List<Movie> movies=new ArrayList<>();
            //movies.add(movie);
            DataManager.getInstance().saveData(serieType,new Gson().toJson(movies));
        }
        else{
            List<Movie> movieList=new Gson().fromJson(favoriteMovies,new TypeToken<List<Movie>>(){}.getType());
            movieList.remove(movie);
            DataManager.getInstance().saveData(serieType,new Gson().toJson(movieList));
        }
    }
    private void addRecentMovies(Movie movie) {
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.MOVIE_CATEGORIES) {
            serieType = "recentMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.ENTERTAINMENT_CATEGORIES){
            serieType = "recentEntertainment";
        }
        String recentMovies = DataManager.getInstance().getString(serieType,"");
        if (TextUtils.isEmpty(recentMovies)) {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            DataManager.getInstance().saveData(serieType, new Gson().toJson(movies));
        }
        else {
            List<Movie> movieList = new Gson().fromJson(recentMovies, new TypeToken<List<Movie>>() { }.getType());
            boolean needsToAdd = true;
            Iterator it = movieList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (movie.getContentId() == ((Movie) it.next()).getContentId()) {
                    needsToAdd = false;
                    break;
                }
            }
            if (needsToAdd) {
                if (movieList.size() == 10) {
                    movieList.remove(9);
                }
                movieList.add(0, movie);
                DataManager.getInstance().saveData(serieType, new Gson().toJson((Object) movieList));
            }
        }
    }



    @Override
    public void onMovieSelected(int selectedRow, int selectedMovie) {
       viewCallback.onMovieSelected(selectedRow,selectedMovie);

    }
}