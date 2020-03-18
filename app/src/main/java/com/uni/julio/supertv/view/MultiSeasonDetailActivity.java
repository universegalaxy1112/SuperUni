package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.EpisodeLoadListener;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.library.CustomProgressDialog;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel;
import com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModelContract;
import com.uni.julio.supertv.viewmodel.Lifecycle;

public class MultiSeasonDetailActivity extends BaseActivity implements EpisodeDetailsViewModelContract.View, EpisodeLoadListener {
    EpisodeDetailsViewModel movieDetailsViewModel;
    ActivityMultiSeasonDetailBinding activityMultiSeasonDetailBinding;
    int mainCategoryId;
    int movieCategoryId;
    int serieId;
    Serie serie;
    private CustomProgressDialog customProgressDialog;

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
        serie=(Serie) VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getMovieCategory(movieCategoryId).getMovie(serieId);
        movieDetailsViewModel=new EpisodeDetailsViewModel(getBaseContext(),mainCategoryId);
        activityMultiSeasonDetailBinding= DataBindingUtil.setContentView(this, R.layout.activity_multi_season_detail);
        showMovieDetails(serie,mainCategoryId,movieCategoryId);
     }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideProgressDialog();
            finishActivity();
            return true;
        }
        return false;
    }
    public void onPlaySelected(Movie movie, final int type) {
        final int movieId = movie.getContentId();
        String[] uris={};
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
        if(uris[0]==null){
            uris = new String[] {movie.getStreamUrl()};
        }
         String movieUrl = uris[0].replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4");
        String extension = movie.getStreamUrl().substring(movieUrl.lastIndexOf(".") + 1);
         String[] extensions = new String[] {extension};
         String subtitleUrl= movie.getSubtitleUrl();
         String title = serie.getTitle();
         long secondsToPlay=DataManager.getInstance().getLong("seconds" + movieId,0);
         String[] finalUris = uris;
        if(false){
            playTrailer(finalUris,extensions,subtitleUrl,title);
        }else{
            playVideo(finalUris,extensions, movieId,secondsToPlay, type,subtitleUrl,title);
        }
    }
    private void playTrailer(String[] uris, String[] extensions,  String subTitleUrl,String title){
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), TrailerActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("subsURL", subTitleUrl)
                .putExtra("title", title)
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
 private void playVideo(String[] uris, String[] extensions, int movieId, long secondsToPlay, int type, String subTitleUrl, String title){
     Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoPlayActivity.class);
     launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
             .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
             .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
             .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, secondsToPlay)
             .putExtra("mainCategoryId", mainCategoryId)
             .putExtra("type", type)
             .putExtra("title", title)
             .putExtra("subsURL", subTitleUrl)
             .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
     hideProgressDialog();
     startActivity(launchIntent);
     getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
 }
    @Override
    public void showMovieDetails(Serie serie, int maincategory, int moviecategory) {
        movieDetailsViewModel.showMovieDetails(serie,activityMultiSeasonDetailBinding,maincategory,moviecategory, this);
    }

@Override
    public void showCustomProgress(){
        if(customProgressDialog == null) customProgressDialog = new CustomProgressDialog(this, getString(R.string.wait));
        customProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideProgressDialog();
                    finishActivity();
                    return true;
                }
                return false;
            }
        });
        customProgressDialog.show();
    }
    public void showCustomProgressDialog(String message){
        customProgressDialog = new CustomProgressDialog(this, message);
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
    }
    public void hideProgressDialog(){
        if(customProgressDialog != null) customProgressDialog.dismiss();
    }

    @Override
    public void onLoaded() {
        hideProgressDialog();
    }
    @Override
    public void onError(){
        hideProgressDialog();
        if(Connectivity.isConnected()) {
            Dialogs.showOneButtonDialog(getActivity(), R.string.generic_error_title, R.string.generic_loading_message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishActivity();
                }
            });
        }else {
            noInternetConnection(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    launchActivity(LoginActivity.class);
//                    getActivity().finish();
                    finishActivity();
                }
            });
        }
    }
}
