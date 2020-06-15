package com.uni.julio.supertvplus.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uni.julio.supertvplus.adapter.MoviesCategoryAdapter;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.listeners.ShowAsGridListener;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.view.LoadingActivity;
import com.uni.julio.supertvplus.view.OneSeasonDetailActivity;
import com.uni.julio.supertvplus.view.VideoPlayActivity;
import com.uni.julio.supertvplus.view.exoplayer.VideoPlayFragment;

import java.util.List;

public class MoviesMenuViewModel implements MoviesMenuViewModelContract.ViewModel,  MovieSelectedListener, ShowAsGridListener {

    public ObservableBoolean isConnected;
    private MoviesMenuViewModelContract.View viewCallback;
    private Context mContext;
    private MoviesCategoryAdapter moviesCategoryAdapter;
    private int mainCategoryPosition = 0;
    public MoviesMenuViewModel(Context context) {
        isConnected = new ObservableBoolean(Connectivity.isConnected());
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
        if(!Device.canTreatAsBox()) categoriesRecyclerview.setIsAutoProcessFocus(false);

        List<MovieCategory> mMoviesList = VideoStreamManager.getInstance().getMainCategory(mainCategoryPosition).getMovieCategories();
        moviesCategoryAdapter =new MoviesCategoryAdapter(mContext, mMoviesList, mainCategoryPosition,this,this);
        this.mainCategoryPosition = mainCategoryPosition;
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoriesRecyclerview.setLayoutManager(mLayoutManager);
        categoriesRecyclerview.setAdapter(moviesCategoryAdapter);
    }


    private void addRecentSerie(Serie serie) {
         DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
    }

    @Override
    public void onShowAsGridSelected(Integer position) {
        viewCallback.onShowAsGridSelected(position);
    }

    @Override
    public void onMovieSelected(VideoStream movie) {
        if (movie instanceof Serie) {
            addRecentSerie((Serie) movie);
            Bundle extras = new Bundle();
            extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
            extras.putInt("mainCategoryId", mainCategoryPosition);
            extras.putString("serie", new Gson().toJson(movie));
            Intent launchIntent = new Intent(mContext, LoadingActivity.class);
            launchIntent.putExtras(extras);
            mContext.startActivity(launchIntent);
        } else {
            if (mainCategoryPosition == 4 || mainCategoryPosition == 7) {
                onPlaySelectedDirect((Movie) movie, mainCategoryPosition);
            } else {
                Bundle extras = new Bundle();
                extras.putString("movie", new Gson().toJson(movie));
                extras.putInt("mainCategoryId", mainCategoryPosition);
                Intent launchIntent = new Intent(mContext, OneSeasonDetailActivity.class);
                launchIntent.putExtras(extras);
                ActivityCompat.startActivityForResult((AppCompatActivity) mContext, launchIntent, 100,
                        null);
            }
        }
    }

    private void onPlaySelectedDirect(Movie movie, int mainCategoryId) {
        int movieId = movie.getContentId();
        String[] uris = {movie.getStreamUrl()};
        String[] extensions = {movie.getStreamUrl().substring(movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        Intent launchIntent = new Intent(mContext, VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
                .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, 0L)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("type", 0)
                .putExtra("subsURL", movie.getSubtitleUrl())
                .putExtra("title", movie.getTitle())
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        ActivityCompat.startActivityForResult((AppCompatActivity)mContext, launchIntent,100
                ,null);
    }
}