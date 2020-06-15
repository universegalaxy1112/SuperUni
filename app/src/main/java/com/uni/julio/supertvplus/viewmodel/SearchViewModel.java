package com.uni.julio.supertvplus.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.adapter.GridViewAdapter;
import com.uni.julio.supertvplus.databinding.ActivitySearchBinding;
import com.uni.julio.supertvplus.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.DialogListener;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.listeners.StringRequestListener;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.DataManager;

import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.networing.LiveTVServicesManual;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.utils.networing.WebConfig;
import com.uni.julio.supertvplus.view.LoadingActivity;
import com.uni.julio.supertvplus.view.OneSeasonDetailActivity;
import com.uni.julio.supertvplus.view.VideoPlayActivity;
import com.uni.julio.supertvplus.view.exoplayer.VideoPlayFragment;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchViewModel implements SearchViewModelContract.ViewModel, MovieSelectedListener {

    private final MainCategory mMainCategory;
    public ObservableBoolean isConnected;
    private SearchViewModelContract.View viewCallback;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private List<? extends VideoStream> movies;
    private int columns = 3;
    private Pattern pattern;
    private GridViewAdapter moreVideoAdapter;
    public ObservableBoolean isLoading;
    private EditText editText;
    public SearchViewModel(Context context, int mainCategory) {
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        mContext = context;
        mMainCategory = VideoStreamManager.getInstance().getMainCategory(mainCategory);
        pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        isLoading = new ObservableBoolean(false);
    }

    private String removeSpecialChars(String s) {
        String nfdNormalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (SearchViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void showMovieList(final ActivitySearchBinding activitySearchBinding, TVRecyclerView moviesGridRV, final String query, final boolean searchSerie) {
        isLoading.set(true);
        columns = 3;
        movies = new ArrayList<>();
        this.editText = activitySearchBinding.editPassword;
        moreVideoAdapter=new GridViewAdapter(mContext,moviesGridRV,movies,this);
        mLayoutManager=new GridLayoutManager(mContext,Integer.parseInt(mContext.getString(R.string.more_video)));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moviesGridRV.setLayoutManager(mLayoutManager);
        moviesGridRV.setAdapter(moreVideoAdapter);
        if (moviesGridRV.getItemDecorationCount() == 0) {
            moviesGridRV.addItemDecoration(
                    new RecyclerViewItemDecoration(mContext.getResources().getInteger(R.integer.recycler_decoration_padding),
                    mContext.getResources().getInteger(R.integer.recycler_decoration_padding),
                    mContext.getResources().getInteger(R.integer.recycler_decoration_padding),
                    mContext.getResources().getInteger(R.integer.recycler_decoration_padding)));
        }
        activitySearchBinding.noResult.setVisibility(View.GONE);
        LiveTVServicesManual.searchVideo(mMainCategory,removeSpecialChars(query),45)
                .delay(2, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<? extends VideoStream>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("error","error");
                        isLoading.set(false);
                        Toast.makeText(mContext, R.string.time_out, Toast.LENGTH_SHORT).show();
                        activitySearchBinding.noResult.setVisibility(View.VISIBLE);
                        hideKeyboard();
                    }
                    @Override
                    public void onNext(List<? extends VideoStream> videos) {
                        isLoading.set(false);
                        movies = videos;
                        if(movies.size() < 1){
                            activitySearchBinding.noResult.setVisibility(View.VISIBLE);
                            Dialogs.showTwoButtonsDialog(mContext,R.string.ok_dialog,R.string.cancel,R.string.title_order_message, new DialogListener() {

                                @Override
                                public void onAccept() {
                                    sendOrder(query);
                                }

                                @Override
                                public void onCancel() {

                                }

                                @Override
                                public void onDismiss() {

                                }
                            });
                            return;
                        }
                        moreVideoAdapter.updateMovies(movies);
                    }
                });

    }
    private void hideKeyboard(){
        editText.clearFocus();
    }
    @Override
    public void onConfigurationChanged() {
        if(mLayoutManager != null) {
            columns = 3;//default portrait

            mLayoutManager.setSpanCount(columns);
        }
    }

    private void addRecentSerie(Serie serie) {
        //just save the Serie in localPreferences, for future use
        DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
    }

    private void sendOrder(String query){
        String reportUrl = WebConfig.orderUrl.replace("{USER}", LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName())
                .replace("{TIPO}", Integer.toString(mMainCategory.getId()))
                .replace("{TITLE}", query);
        NetManager.getInstance().makeStringRequest(reportUrl, new StringRequestListener() {
            @Override
            public void onCompleted(String response) {
                Toast.makeText(mContext, "Thanks for requesting. We will add it as soon as possible!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError() {
                Toast.makeText(mContext, "Failed to send request! Please check your network connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMovieSelected(VideoStream movie) {
        if(movie instanceof Serie) {
            addRecentSerie((Serie) movie);
            Bundle extras = new Bundle();
            extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
            extras.putInt("mainCategoryId", mMainCategory.getId());
            extras.putString("serie", new Gson().toJson(movie));
            Intent launchIntent = new Intent(mContext, LoadingActivity.class);
            launchIntent.putExtras(extras);
            mContext.startActivity(launchIntent);
        }
        else {
                if(mMainCategory.getId() == 4 || mMainCategory.getId() == 7){
                    onPlaySelectedDirect((Movie)movie,mMainCategory.getId());
                }else{
                    Bundle extras = new Bundle();
                    extras.putString("movie", new Gson().toJson(movie));
                    extras.putInt("mainCategoryId", mMainCategory.getId());
                    Intent launchIntent = new Intent(mContext, OneSeasonDetailActivity.class);
                    launchIntent.putExtras(extras);
                    ActivityCompat.startActivityForResult((AppCompatActivity)mContext, launchIntent,100,
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