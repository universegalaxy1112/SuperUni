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
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.adapter.GridViewAdapter;
import com.uni.julio.supertvplus.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.listeners.MoviesGridViewModelContract;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.view.LoadingActivity;
import com.uni.julio.supertvplus.view.OneSeasonDetailActivity;
import com.uni.julio.supertvplus.view.VideoPlayActivity;
import com.uni.julio.supertvplus.view.exoplayer.VideoPlayFragment;

import java.util.ArrayList;
import java.util.List;

public class MoviesGridViewModel implements MoviesGridViewModelContract.ViewModel, MovieSelectedListener {

    public ObservableBoolean isConnected;
    private MoviesGridViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private int mMainCategoryPosition;
    private Object SkeletonScreen;

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
        this.mMainCategoryPosition=mainCategoryPosition;
        List<Movie> movies = new ArrayList<>();
        if(VideoStreamManager.getInstance().getMainCategory(mainCategoryPosition) != null && videoStreamManager.getMainCategory(mainCategoryPosition).getMovieCategories().size() > movieCategoryPosition){
            movies = (List<Movie>)(videoStreamManager.getMainCategory(mainCategoryPosition).getMovieCategories().get(movieCategoryPosition).getMovieList());
        }
        GridViewAdapter moreVideoAdapter=new GridViewAdapter(mContext,moviesGridRV,movies,this);
        mLayoutManager=new GridLayoutManager(mContext,Integer.parseInt(mContext.getString(R.string.more_video)));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moviesGridRV.setLayoutManager(mLayoutManager);
        moviesGridRV.setAdapter(moreVideoAdapter);
     if (moviesGridRV.getItemDecorationCount() == 0) {
         moviesGridRV.addItemDecoration(new RecyclerViewItemDecoration(mContext.getResources().getInteger(R.integer.recycler_decoration_padding),
                 mContext.getResources().getInteger(R.integer.recycler_decoration_padding),
                 mContext.getResources().getInteger(R.integer.recycler_decoration_padding),
                 mContext.getResources().getInteger(R.integer.recycler_decoration_padding)));
     }
    }


    @Override
    public void onMovieSelected(VideoStream movie) {
        if (movie instanceof Serie) {
            addRecentSerie((Serie) movie);
            Bundle extras = new Bundle();
            extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
            extras.putInt("mainCategoryId", mMainCategoryPosition);
            extras.putString("serie", new Gson().toJson(movie));
            Intent launchIntent = new Intent(mContext, LoadingActivity.class);
            launchIntent.putExtras(extras);
            mContext.startActivity(launchIntent);
        } else {
            if (mMainCategoryPosition == 4 || mMainCategoryPosition == 7) {
                onPlaySelectedDirect((Movie) movie, mMainCategoryPosition);
            } else {
                Bundle extras = new Bundle();
                extras.putString("movie", new Gson().toJson(movie));
                extras.putInt("mainCategoryId", mMainCategoryPosition);
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

    private void addRecentSerie(Serie serie) {
        //just save the Serie in localPreferences, for future use
        DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
    }
}