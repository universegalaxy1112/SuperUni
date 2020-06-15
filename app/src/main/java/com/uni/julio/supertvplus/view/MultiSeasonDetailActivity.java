package com.uni.julio.supertvplus.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertvplus.listeners.EpisodeLoadListener;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.User;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.library.CustomProgressDialog;
import com.uni.julio.supertvplus.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertvplus.viewmodel.EpisodeDetailsViewModel;
import com.uni.julio.supertvplus.viewmodel.EpisodeDetailsViewModelContract;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;

import java.util.List;

public class MultiSeasonDetailActivity extends BaseActivity implements EpisodeDetailsViewModelContract.View, EpisodeLoadListener {
    EpisodeDetailsViewModel movieDetailsViewModel;
    ActivityMultiSeasonDetailBinding activityMultiSeasonDetailBinding;
    int mainCategoryId;
    Serie serie;
    private AdView mAdView;

    private CustomProgressDialog customProgressDialog;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdView != null) {
            mAdView.resume();
        }
    }

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
            User user = LiveTvApplication.getUser();
            if(user == null || !user.getMembership()) {
                mAdView = findViewById(R.id.adView);
                if(mAdView != null) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                }
            }
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
