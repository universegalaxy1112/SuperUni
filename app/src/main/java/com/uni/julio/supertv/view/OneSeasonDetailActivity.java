package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityOneseasonDetailBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.model.CastDevice;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MovieDetailsViewModel;
import com.uni.julio.supertv.viewmodel.MovieDetailsViewModelContract;


public class OneSeasonDetailActivity extends BaseActivity implements MovieDetailsViewModelContract.View {
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
        try {
            Bundle extras=getIntent().getExtras();
            if(extras != null){
                mainCategoryId= extras.getInt("mainCategoryId",0);
                movieCategoryId= extras.getInt("movieCategoryId",0);
                movie= new Gson().fromJson(extras.getString("movie"), Movie.class);
            }
            movieDetailsViewModel = new MovieDetailsViewModel(this, mainCategoryId);
            activityOneseaosnDetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_oneseason_detail);
            activityOneseaosnDetailBinding.setMovieDetailsVM(movieDetailsViewModel);
            showMovieDetails(movie,mainCategoryId);
        }catch (Exception e){
            Dialogs.showOneButtonDialog(getActivity(), R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    getActivity().finish();
                }
            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }

    public void onPlaySelected(final Movie movie, final int type) {
        final int movieId = movie.getContentId();
         String[] uris={};
        switch (type){
            case 0:
                uris = new String[] {movie.getStreamUrl()};
                break;
            case 1:
                uris = new String[] {movie.getSDUrl()};
                break;
            case 2:
                uris = new String[] {movie.getTrailerUrl()};
                break;
            default:
        }
        String movieUrl = uris[0].replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4");
        uris[0]=movieUrl;
        String extension = uris[0].substring(movieUrl.lastIndexOf(".") + 1);
         String[] extensions = new String[] {extension};
         long secondsToPlay=DataManager.getInstance().getLong("seconds" + movieId,0L);
         String subtitleUrl= movie.getSubtitleUrl();
         String title= movie.getTitle();
         String[] finalUris = uris;
         playVideo(finalUris,extensions, movieId,secondsToPlay, type,subtitleUrl,title);
    }

    private void playVideo(String[] uris, String[] extensions, int movieId, long secondsToPlay, int type, String subTitleUrl,String title){
        Intent launchIntent = new Intent(this, VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
                .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, secondsToPlay)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("type", type)
                .putExtra("subsURL", subTitleUrl)
                .putExtra("title", title)
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        ActivityCompat.startActivityForResult(this, launchIntent,100
                ,null);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    @Override
    public void showMovieDetails(Movie movie, int mainCategory) {
        activityOneseaosnDetailBinding.setMovieDetailItem(movie);
        movieDetailsViewModel.showMovieDetails(movie,activityOneseaosnDetailBinding,mainCategory);
    }
    @Override
    public void onDeviceLoaded(CastDevice castDevice){

    }


}
