package com.uni.julio.supertv.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;

import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel;
import com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModelContract;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public class MultiSeasonDetailActivity extends BaseActivity implements EpisodeDetailsViewModelContract.View {
    EpisodeDetailsViewModel movieDetailsViewModel;
    ActivityMultiSeasonDetailBinding activityMultiSeasonDetailBinding;
    int mainCategoryId;
    int movieCategoryId;
    int serieId;

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
        Bundle extra=getIntent().getExtras();
        mainCategoryId=extra.getInt("mainCategoryId",-1);
        movieCategoryId=extra.getInt("movieCategoryId",-1);
        serieId=extra.getInt("serieId",-1);
        Serie serie=(Serie) VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getMovieCategory(movieCategoryId).getMovie(serieId);
        movieDetailsViewModel=new EpisodeDetailsViewModel(getBaseContext(),mainCategoryId);
        activityMultiSeasonDetailBinding= DataBindingUtil.setContentView(this, R.layout.activity_multi_season_detail);
        showMovieDetails(serie,mainCategoryId,movieCategoryId);
     }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }
    public void onPlaySelected(Movie movie, boolean fromStart) {
        int movieId = movie.getContentId();
        String[] uris = new String[] {movie.getStreamUrl()};
        String movieUrl = movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4");
        String extension = movie.getStreamUrl().substring(movieUrl.lastIndexOf(".") + 1);
        String[] extensions = new String[] {extension};

        long secondsToPlay = 0;
        if(!fromStart) {
            secondsToPlay = DataManager.getInstance().getLong("seconds" + movieId, 0);
        }
        else {
            DataManager.getInstance().saveData("seconds" + movieId, 0);
        }

        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
                .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, secondsToPlay)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("subsURL", movie.getSubtitleUrl())
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void showMovieDetails(Serie serie, int maincategory, int moviecategory) {
        movieDetailsViewModel.showMovieDetails(serie,activityMultiSeasonDetailBinding,maincategory,moviecategory);

    }


}
