package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.OneSeasonAdapter;
import com.uni.julio.supertv.databinding.ActivityOneseaosnDetailBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.DataManager;

import java.util.ArrayList;
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
    private boolean hidePlayFromStart = false;
    ActivityOneseaosnDetailBinding movieDetailsBinding;
    public MovieDetailsViewModel(Context context, int mainCategoryId) {
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
        mMainCategoryId=mainCategoryId;
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
    public void showMovieDetails(Movie movie, ActivityOneseaosnDetailBinding movieDetailsBinding , int mainCategoryId,int movieCategoryId) {
        mMainCategoryId=mainCategoryId;
        this.movieDetailsBinding=movieDetailsBinding;
        if(mainCategoryId == 4) { //eventos
            hidePlayFromStart = true;
        }
        if(hidePlayFromStart) {
            isSeen = new ObservableBoolean(false);
        }
        else  {
            isSeen = new ObservableBoolean(videoStreamManager.isLocalSeen(String.valueOf(movie.getContentId())));
        }
        isFavorite = new ObservableBoolean(videoStreamManager.isLocalFavorite(String.valueOf(movie.getContentId())));
        mMovie = movie;
        movieDetailsBinding.setMovieDetailItem(movie);
        TVRecyclerView rowsRecycler = movieDetailsBinding.getRoot().findViewById(R.id.recycler_view);
        GridLayoutManager rowslayoutmanger = new GridLayoutManager(mContext, 1);
        rowslayoutmanger.setOrientation(LinearLayoutManager.HORIZONTAL);
        movieList= videoStreamManager.getMainCategory(mainCategoryId).getMovieCategory(movieCategoryId).getMovieList();
        OneSeasonAdapter moviesRecyclerAdapter = new OneSeasonAdapter(mContext, rowsRecycler,movieList, movieCategoryId, this);
        rowsRecycler.setLayoutManager(rowslayoutmanger);
        rowsRecycler.setAdapter(moviesRecyclerAdapter);
        if (rowsRecycler.getItemDecorationCount() == 0) {
            rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(20,48,32,16));
        }
    }

     public void finishActivity(View view) {
        viewCallback.finishActivity();
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
            addRecentMovies(mMovie);
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


    @Override
    public void onMovieSelected(int selectedRow, int selectedMovie) {
       viewCallback.onMovieSelected(selectedRow,selectedMovie);

    }
}