package com.uni.julio.supertv.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityMorevideoBinding;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MoviesGridViewModelContract;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MoviesGridViewModel;
public class MoreVideoActivity extends BaseActivity implements MoviesGridViewModelContract.View{
    private MoviesGridViewModel moviesGridViewModel;
    private ActivityMorevideoBinding activityMorevideoBinding;
     @Override
    protected Lifecycle.ViewModel getViewModel() {
        return moviesGridViewModel;
    }



    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Bundle extras=getIntent().getExtras();
        selectedType=(ModelTypes.SelectedType)extras.get("selectedType");
        mainCategoryId=extras.getInt("mainCategoryId",-1);
        movieCategoryId=extras.getInt("movieCategoryId",-1);
        moviesGridViewModel=new MoviesGridViewModel(getBaseContext(),selectedType);
        activityMorevideoBinding= DataBindingUtil.setContentView(this,R.layout.activity_morevideo);
        activityMorevideoBinding.setMoviesGridFragmentVM(moviesGridViewModel);
        Toolbar toolbar = activityMorevideoBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(Device.treatAsBox){
             (activityMorevideoBinding.appBarLayout).setVisibility(View.GONE);
        }
        String title= VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getMovieCategories().get(movieCategoryId).getCatName();
        getSupportActionBar().setTitle(title);
        moviesGridViewModel.showMovieList(activityMorevideoBinding.moreVideoRecycler,mainCategoryId,movieCategoryId);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if(keycode== KeyEvent.KEYCODE_BACK){
            finishActivity();
        }
        return false;
    }

    @Override
    public void onMovieAccepted(int selectedRow,Movie movie) {
        Bundle extras = new Bundle();
        extras.putString("movie", new Gson().toJson(movie));
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putInt("movieCategoryId", selectedRow);
        Intent launchIntent = getLaunchIntent(OneSeasonDetailActivity.class, extras);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onSerieAccepted(int selectedRow,Serie serie) {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putInt("movieCategoryId", selectedRow);
        extras.putInt("serieId", serie.getPosition());
        extras.putString("serie", new Gson().toJson(serie));
        launchActivity(LoadingActivity.class, extras);
    }
}
