package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.EpisodeLoadListener;
import com.uni.julio.supertv.model.Episode;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.library.CustomProgressDialog;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel;
import com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModelContract;
import com.uni.julio.supertv.viewmodel.Lifecycle;

import java.util.List;

public class MultiSeasonDetailActivity extends BaseActivity implements EpisodeDetailsViewModelContract.View, EpisodeLoadListener {
    EpisodeDetailsViewModel movieDetailsViewModel;
    ActivityMultiSeasonDetailBinding activityMultiSeasonDetailBinding;
    int mainCategoryId;
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
        try{
            Bundle extra=getIntent().getExtras();
            if(extra != null) {
                mainCategoryId=extra.getInt("mainCategoryId",-1);
                serie = new Gson().fromJson(extra.getString("serie"), Serie.class);
            }
            movieDetailsViewModel=new EpisodeDetailsViewModel(this,mainCategoryId);
            activityMultiSeasonDetailBinding= DataBindingUtil.setContentView(this, R.layout.activity_multi_season_detail);
            showMovieDetails(serie,mainCategoryId);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideProgressDialog();
            finishActivity();
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    public void onPlaySelected(Movie movie, final int type, int seasonPosition) {
        final int movieId = movie.getContentId();
        List<?extends VideoStream> episodes = serie.getSeason(seasonPosition).getEpisodeList();
        String[] uris= new String[episodes.size()];
        String[] extensions = new String[episodes.size()];
        String subtitleUrl = null;
        String title = null;
        long secondsToPlay = 0;
        String movieUrl = movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4");
        String extension = movie.getStreamUrl().substring(movieUrl.lastIndexOf(".") + 1);
        for(int i = 0 ; i < episodes.size(); i++  ){
            extensions[i] = extension;
            switch (type){
                case 0:
                    uris[i] = episodes.get(i).getStreamUrl();
                    break;
                case 1:
                    if(episodes.get(i).getSDUrl()==null){
                        uris[i] = episodes.get(i).getStreamUrl();
                    }
                    else{
                        uris[i] = episodes.get(i).getSDUrl();
                    }
                    break;
                case 2:
                    if(episodes.get(i).getSDUrl()==null){
                        uris[i] = episodes.get(i).getStreamUrl();
                    }
                    else{
                        uris[i] = episodes.get(i).getTrailerUrl();
                    }
                    break;
                default:
            }
            subtitleUrl= movie.getSubtitleUrl();
            title = serie.getTitle();
            secondsToPlay=DataManager.getInstance().getLong("seconds" + movieId,0L);
        }

         playVideo(uris,extensions, movieId, secondsToPlay, type,subtitleUrl,title, seasonPosition, movie.getPosition());
    }
 private void playVideo(String[] uris, String[] extensions, int movieId, long secondsToPlay, int type, String subTitleUrl, String title,  int seasonPosition, int episodePosition){
     Intent launchIntent = new Intent(this, VideoPlayActivity.class);
     launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
             .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
             .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
             .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, secondsToPlay)
             .putExtra("mainCategoryId", mainCategoryId)
             .putExtra("type", type)
             .putExtra("title", title)
             .putExtra("seasonPosition", seasonPosition)
             .putExtra("episodePosition", episodePosition)
             .putExtra("subsURL", subTitleUrl)
             .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
     hideProgressDialog();
     ActivityCompat.startActivityForResult(this, launchIntent,100
             ,null);
     getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
 }
    @Override
    public void showMovieDetails(Serie serie, int maincategory) {
        movieDetailsViewModel.showMovieDetails(serie,activityMultiSeasonDetailBinding,maincategory, this);
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
    public void hideProgressDialog(){
        if(customProgressDialog != null && LiveTvApplication.appContext instanceof MultiSeasonDetailActivity && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
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
                    finishActivity();
                }
            });
        }
    }
}
