package com.uni.julio.supertv.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityOneseasonDetailBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MovieDetailsViewModel;
import com.uni.julio.supertv.viewmodel.MovieDetailsViewModelContract;
public class OneSeasonDetailActivity extends BaseActivity implements MovieDetailsViewModelContract.View  {
    MovieDetailsViewModel movieDetailsViewModel;
    ActivityOneseasonDetailBinding activityOneseaosnDetailBinding;
    Movie movie;
     @Override
    protected Lifecycle.ViewModel getViewModel() {
        return movieDetailsViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras=getIntent().getExtras();
          mainCategoryId= extras.getInt("mainCategoryId",0);
          movieCategoryId= extras.getInt("movieCategoryId",0);
          movie=(Movie) new Gson().fromJson(extras.getString("movie"), Movie.class);
        if(mainCategoryId == 4 || mainCategoryId == 6 || mainCategoryId == 7){
             onPlaySelectedDirect(movie, mainCategoryId);
             finish();
             return;
        }
        movieDetailsViewModel = new MovieDetailsViewModel(getBaseContext(), mainCategoryId);
        activityOneseaosnDetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_oneseason_detail);
        activityOneseaosnDetailBinding.setMovieDetailsVM(movieDetailsViewModel);
        showMovieDetails(movie,mainCategoryId,movieCategoryId);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
             finishActivity();
            return true;
        }
        return false;
    }
    
    public void onPlaySelectedDirect(Movie movie, int mainCategoryId) {
        int movieId = movie.getContentId();
        String[] uris = {movie.getStreamUrl()};
        String[] extensions = {movie.getStreamUrl().substring(movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoPlayActivity.class);
        launchIntent.putExtra("uri_list", uris).putExtra("extension_list", extensions).putExtra("movie_id_extra", movieId).putExtra("seconds_to_start", 0).putExtra("mainCategoryId", mainCategoryId).putExtra("subsURL", movie.getSubtitleUrl()).setAction("com.google.android.exoplayer.demo.action.VIEW_LIST");
        startActivity(launchIntent);
    }

    public void onPlaySelected(Movie movie, int type) {
         int movieId = movie.getContentId();   String[] uris={};
        switch (type){
            case 0:
                uris = new String[] {movie.getStreamUrl()};
                break;
            case 1:
                if(movie.getSDUrl()==null){
                    uris = new String[] {movie.getStreamUrl()};
                }
                else{
                    uris = new String[] {movie.getSDUrl()};
                }
                break;
            case 2:
                if(movie.getTrailerUrl()==null){
                    uris = new String[] {movie.getStreamUrl()};
                }
                else{
                    uris = new String[] {movie.getTrailerUrl()};
                }
                break;
            default:
        }

        String movieUrl = uris[0].replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4");
        String extension = movie.getStreamUrl().substring(movieUrl.lastIndexOf(".") + 1);
        String[] extensions = new String[] {extension};
        long secondsToPlay=DataManager.getInstance().getLong("seconds" + movieId,0);
        //DataManager.getInstance().saveData("seconds" + movieId, temp);
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
                .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, secondsToPlay)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("type", type)
                .putExtra("subsURL", movie.getSubtitleUrl())
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void showMovieDetails(Movie movie, int mainCategory, int movieCategory) {
        activityOneseaosnDetailBinding.setMovieDetailItem(movie);
        movieDetailsViewModel.showMovieDetails(movie,activityOneseaosnDetailBinding,mainCategory,movieCategory);
    }

    @Override
    public void onMovieSelected(int selectedRow, int selectedMovie) {
        Movie movie=(Movie) VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getMovieCategory(selectedRow).getMovie(selectedMovie);
        Bundle extras = new Bundle();
        extras.putString("movie", new Gson().toJson(movie));
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putInt("movieCategoryId", selectedRow);
        Intent launchIntent = getLaunchIntent(OneSeasonDetailActivity.class, extras);
        startActivity(launchIntent);
        finishActivity();
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
     }

}
