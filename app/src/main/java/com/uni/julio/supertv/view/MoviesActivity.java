package com.uni.julio.supertv.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityMoviesBinding;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MoviesMenuViewModel;
import com.uni.julio.supertv.viewmodel.MoviesMenuViewModelContract;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class MoviesActivity extends BaseActivity implements MoviesMenuViewModelContract.View {
    private TVRecyclerView recyclerView;
     private MoviesMenuViewModel moviesMenuViewModel;
    ActivityMoviesBinding activityMoviesBinding;
    private int serieId;
    SearchView searchView;
    private boolean isPip = false;
    private int EVENT_REQUEST_CODE = 100;
    private WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return moviesMenuViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            moviesMenuViewModel=new MoviesMenuViewModel(getBaseContext());
            activityMoviesBinding= DataBindingUtil.setContentView(this,R.layout.activity_movies);
            activityMoviesBinding.setMoviesMenuFragmentVM(moviesMenuViewModel);
            Bundle extras = getActivity().getIntent().getExtras();
            selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
            mainCategoryId = extras.getInt("mainCategoryId",-1);
            movieCategoryId = extras.getInt("movieCategoryId",-1);
            serieId = extras.getInt("serieId",-1);
            Toolbar toolbar = activityMoviesBinding.toolbar;
            toolbar.setTitle(VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getCatName());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
            }
            waveSwipeRefreshLayout =  findViewById(R.id.main_swipe);
            waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
                @Override public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(selectedType== ModelTypes.SelectedType.MAIN_CATEGORY){
                                moviesMenuViewModel.showMovieLists(activityMoviesBinding.moviecategoryrecycler,mainCategoryId);
                            }
                            waveSwipeRefreshLayout.setRefreshing(false);
                        }
                    },3000);
                }
            });
            if(selectedType== ModelTypes.SelectedType.MAIN_CATEGORY){
                moviesMenuViewModel.showMovieLists(activityMoviesBinding.moviecategoryrecycler,mainCategoryId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

     }
    private long mLastKeyDownTime = 0;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        MenuItem myActionMenuItem=menu.findItem(R.id.toolbar_search);
        searchView=(SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    private void search(String query){
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", selectedType);
        extras.putInt("mainCategoryId",mainCategoryId);
        extras.putInt("movieCategoryId",movieCategoryId);
        extras.putString("query",query);
        launchActivity(SearchActivity.class, extras);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        long current = System. currentTimeMillis();
        boolean res;
        if (current - mLastKeyDownTime < 300 ) {
            res = true;
        } else {
            res = super.onKeyDown(keyCode, event);
            mLastKeyDownTime = current;
        }
        return res;
     }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieAccepted(int selectedRow,Movie movie) {
        if(mainCategoryId == 4 || mainCategoryId == 7){
            onPlaySelectedDirect(movie,mainCategoryId);
        }else{
            Bundle extras = new Bundle();
            extras.putString("movie", new Gson().toJson(movie));
            extras.putInt("mainCategoryId", mainCategoryId);
            extras.putInt("movieCategoryId", selectedRow);
            Intent launchIntent = getLaunchIntent(OneSeasonDetailActivity.class, extras);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, activityMoviesBinding.moviecategoryrecycler, getResources().getString(R.string.transition_image));
            ActivityCompat.startActivityForResult(this, launchIntent,100,
                    options.toBundle());
        }
    }
    @Override
    public void onShowAsGridSelected(Integer position) {
        Bundle extras = new Bundle();
        if (serieId == -1) {
            extras.putSerializable("selectedType", selectedType);
            extras.putInt("mainCategoryId", mainCategoryId);
            extras.putInt("movieCategoryId", position);
        }
        launchActivity(MoreVideoActivity.class, extras);
    }
    public void onPlaySelectedDirect(Movie movie, int mainCategoryId) {
        int movieId = movie.getContentId();
        String[] uris = {movie.getStreamUrl()};
        String[] extensions = {movie.getStreamUrl().substring(movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
                .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, 0L)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("type", 0)
                .putExtra("subsURL", movie.getSubtitleUrl())
                .putExtra("title", movie.getTitle())
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        ActivityCompat.startActivityForResult(this, launchIntent,EVENT_REQUEST_CODE
                ,null);
       // startActivityForResult(launchIntent,EVENT_REQUEST_CODE);
    }

    @Override
    public void onSerieAccepted(int selectedRow, Serie serie) {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putInt("movieCategoryId", selectedRow);
        extras.putInt("serieId", serie.getPosition());
        extras.putString("serie", new Gson().toJson(serie));
        launchActivity(LoadingActivity.class, extras);
    }

    @Override
    public void onSearchSelected(boolean isAccepted) {

    }
}
