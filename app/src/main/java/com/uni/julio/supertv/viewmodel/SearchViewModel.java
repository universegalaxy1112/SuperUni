package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.GridViewAdapter;
import com.uni.julio.supertv.adapter.MoviesRecyclerAdapter;
import com.uni.julio.supertv.databinding.ActivitySearchBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.DialogListener;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.networing.LiveTVServicesManual;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.utils.networing.WebConfig;
import com.uni.julio.supertv.view.SpeedTestActivity;

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
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private EditText mEditText;
    private List<? extends VideoStream> movies;
    private int columns = 3;//default for portrait
    private Pattern pattern;
    private GridViewAdapter moreVideoAdapter;
    public ObservableBoolean isLoading;
    private EditText editText;
    public SearchViewModel(Context context, MainCategory mainCategory) {
        ;//Log.d("liveTV","SearchViewModel from "+mainCategory.getModelType());
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
        mMainCategory = mainCategory;
        pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        isLoading = new ObservableBoolean(false);
    }

    public String removeSpecialChars(String s) {
        String nfdNormalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }



    @Override
    public void onViewResumed() {
//        if(mMoviesGridRV != null && mEditText != null) {
//            if (movies.size() > 0) {
//                mMoviesGridRV.requestFocus();
//            }
//            else {
//                mEditText.requestFocus();
//            }
//        }
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
        movies = new ArrayList();
        this.editText = activitySearchBinding.editPassword;
        moreVideoAdapter=new GridViewAdapter(mContext,moviesGridRV,movies,0,this);
        mLayoutManager=new GridLayoutManager(mContext,Integer.parseInt(mContext.getString(R.string.more_video)));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moviesGridRV.setLayoutManager(mLayoutManager);
        moviesGridRV.setAdapter(moreVideoAdapter);
        if (moviesGridRV.getItemDecorationCount() == 0) {
            moviesGridRV.addItemDecoration(new RecyclerViewItemDecoration(24,12,24,12));
        }
        activitySearchBinding.noResult.setVisibility(View.GONE);
        LiveTVServicesManual.searchVideo(mMainCategory,removeSpecialChars(query),10)
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

                        int moviecatId = mMainCategory.getMovieCategories().size() - 1;
                        for (VideoStream vs : movies) {
                            if (vs instanceof Serie) {
                                ((Serie) vs).setMovieCategoryIdOwner(moviecatId);
                            } else if(vs instanceof Movie){
                                ((Movie) vs).setMovieCategoryIdOwner(moviecatId);
                            }
                        }
                        SearchViewModel.this.mMainCategory.getMovieCategory(moviecatId).setMovieList(movies);
                        moreVideoAdapter.updateMovies(movies);
                        moreVideoAdapter.notifyDataSetChanged();
                       // hideKeyboard();
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

    @Override
    public void onMovieSelected(int selectedRow, int selectedMovie) {
        if(movies.get(selectedMovie) instanceof Serie) {
             addRecentSerie((Serie) movies.get(selectedMovie));
            viewCallback.onSerieAccepted(((Serie) movies.get(selectedMovie)).getMovieCategoryIdOwner(),(Serie) movies.get(selectedMovie));
        }
        else {
            int aa=((Movie) movies.get(selectedMovie)).getMovieCategoryIdOwner();
            viewCallback.onMovieAccepted(aa,(Movie) movies.get(selectedMovie));
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

}