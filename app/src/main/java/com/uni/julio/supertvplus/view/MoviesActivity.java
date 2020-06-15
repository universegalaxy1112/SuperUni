package com.uni.julio.supertvplus.view;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.databinding.ActivityMoviesBinding;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;
import com.uni.julio.supertvplus.viewmodel.MoviesMenuViewModel;
import com.uni.julio.supertvplus.viewmodel.MoviesMenuViewModelContract;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class MoviesActivity extends BaseActivity implements MoviesMenuViewModelContract.View {
     private MoviesMenuViewModel moviesMenuViewModel;
    ActivityMoviesBinding activityMoviesBinding;
    private int serieId;
    SearchView searchView;
    private boolean isPip = false;
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
            moviesMenuViewModel=new MoviesMenuViewModel(this);
            activityMoviesBinding= DataBindingUtil.setContentView(this,R.layout.activity_movies);
            activityMoviesBinding.setMoviesMenuFragmentVM(moviesMenuViewModel);
            Bundle extras = getActivity().getIntent().getExtras();
            selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
            mainCategoryId = extras.getInt("mainCategoryId",-1);
            movieCategoryId = extras.getInt("movieCategoryId",-1);
            serieId = extras.getInt("serieId",-1);
            Toolbar toolbar = activityMoviesBinding.toolbar;
            if(VideoStreamManager.getInstance().getMainCategory(mainCategoryId) != null)
                toolbar.setTitle(VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getCatName());
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
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
            Dialogs.showOneButtonDialog(getActivity(), R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    getActivity().finish();
                }
            });
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
    public void onShowAsGridSelected(Integer position) {
        Bundle extras = new Bundle();
        if (serieId == -1) {
            extras.putSerializable("selectedType", selectedType);
            extras.putInt("mainCategoryId", mainCategoryId);
            extras.putInt("movieCategoryId", position);
        }
        launchActivity(MoreVideoActivity.class, extras);
    }


}
