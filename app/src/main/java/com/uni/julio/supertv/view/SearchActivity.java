package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivitySearchBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.SearchViewModel;
import com.uni.julio.supertv.viewmodel.SearchViewModelContract;

public class SearchActivity extends BaseActivity implements SearchViewModelContract.View {
    private SearchViewModel searchViewModel;
    private ActivitySearchBinding activitySearchBinding;
    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return searchViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Bundle extras = getActivity().getIntent().getExtras();
            selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
            mainCategoryId = extras.getInt("mainCategoryId",0);
            movieCategoryId = extras.getInt("movieCategoryId",0);
            String query=extras.getString("query","");
            searchViewModel=new SearchViewModel(getBaseContext(), VideoStreamManager.getInstance().getMainCategory(mainCategoryId));
            activitySearchBinding= DataBindingUtil.setContentView(this, R.layout.activity_search);
            activitySearchBinding.setSearchFM(searchViewModel);
            Toolbar toolbar=activitySearchBinding.toolbar;
            toolbar.setTitle(VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getCatName()+"->"+query);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if(Device.treatAsBox){
                findViewById(R.id.appBarLayout).setVisibility(View.GONE);
            }
            boolean searchSerie=false;
            if((mainCategoryId == 1 || mainCategoryId == 2) && selectedType == ModelTypes.SelectedType.MAIN_CATEGORY) {
                searchSerie = true;
            }
            if(query.equals("")&& Device.treatAsBox){
                (activitySearchBinding.editPassword).setOnEditorActionListener(new EditText.OnEditorActionListener(){

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                searchViewModel.showMovieList(activitySearchBinding.searchRecycler,v.getText().toString(),true);
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
            else{
                activitySearchBinding.editPassword.setVisibility(View.GONE);
                searchViewModel.showMovieList(activitySearchBinding.searchRecycler,query,searchSerie);

            }
        }catch (Exception e){
            e.printStackTrace();
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
            finishActivity();
            return true;
        }

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu){

        return true;
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
            ActivityCompat.startActivityForResult(this, launchIntent,100,
                    null);
        }
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
        ActivityCompat.startActivityForResult(this, launchIntent,100
                ,null);
        // startActivityForResult(launchIntent,EVENT_REQUEST_CODE);
    }

    @Override
    public void onSerieAccepted(int selectedRow,Serie serie) {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putInt("movieCategoryId", serie.getMovieCategoryIdOwner());
        extras.putInt("serieId", serie.getPosition());
        extras.putString("serie", new Gson().toJson(serie));
        launchActivity(LoadingActivity.class, extras);
    }

    @Override
    public void closeKeyboard() {

    }
}
