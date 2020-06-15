package com.uni.julio.supertvplus.view;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.utils.Connectivity;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;
import com.uni.julio.supertvplus.viewmodel.LoadingMoviesViewModel;
import com.uni.julio.supertvplus.viewmodel.LoadingMoviesViewModelContract;
import com.wang.avi.AVLoadingIndicatorView;
public class LoadingActivity extends BaseActivity implements LoadingMoviesViewModelContract.View{
    private Serie serie;
    private LoadingMoviesViewModel loadingMoviesViewModel;

    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return loadingMoviesViewModel;
    }


    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
            mainCategoryId = extras.getInt("mainCategoryId",-1);
            serie = new Gson().fromJson(extras.getString("serie"), Serie.class);
        }
        loadingMoviesViewModel = new LoadingMoviesViewModel();
    }
    private boolean isInit = false;
    @Override
    public void onStart(){
        super.onStart();
        setContentView(R.layout.activity_loading);
        AVLoadingIndicatorView avi=findViewById(R.id.avi);
         if(!isInit){
            cancelCalls();
            if(selectedType== ModelTypes.SelectedType.MAIN_CATEGORY){
                loadingMoviesViewModel.loadSubCategories(mainCategoryId);
            }
            else if(selectedType== ModelTypes.SelectedType.SERIES){
                loadingMoviesViewModel.loadSeasons(mainCategoryId, serie);
            }
            isInit=true;
        }
    }

    private void cancelCalls() {
        NetManager.getInstance().cancelAll();
        for(int i = 0; i < VideoStreamManager.getInstance().getMainCategoriesList().size();i++) {
            int categoryId = VideoStreamManager.getInstance().getMainCategoriesList().get(i).getId();
            if(VideoStreamManager.getInstance().getMainCategory(categoryId) != null && i != mainCategoryId)
                for(int j = 0; j < VideoStreamManager.getInstance().getMainCategory(categoryId).getMovieCategories().size();j++) {
                    VideoStreamManager.getInstance().getMainCategory(categoryId).getMovieCategory(j).setLoading(false);
                }
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
    public void onSubCategoriesForMainCategoryLoaded() {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.MAIN_CATEGORY);
        extras.putInt("mainCategoryId", mainCategoryId);
        if(LiveTvApplication.appContext instanceof LoadingActivity){
            if(Device.treatAsBox)
                launchActivity(MoviesTvActivity.class, extras);
            else
                launchActivity(MoviesActivity.class, extras);
            getActivity().finish();
        }
    }

    @Override
    public void onSubCategoriesForMainCategoryLoadedError() {
        showError();
    }
    public void showError() {
        try{
            if(LiveTvApplication.appContext instanceof LoadingActivity)
            if(Connectivity.isConnected()) {
                Dialogs.showOneButtonDialog(getActivity(), R.string.generic_error_title, R.string.generic_loading_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                });
            }
            else {
                noInternetConnection(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                });
            }
        }catch (IllegalStateException e){
            finishActivity();
        }

    }
    @Override
    public void onSeasonsForSerieLoaded() {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.SEASONS);
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putString("serie", new Gson().toJson(serie));
        launchActivity(MultiSeasonDetailActivity.class, extras);
        getActivity().finish();
    }

    @Override
    public void onSeasonsForSerieLoadedError() {
        showError();
    }

    @Override
    public void onProgramsForLiveTVCategoriesLoaded() {
        launchActivity(LiveTvNewActivity.class);
        getActivity().finish();

    }

    @Override
    public void onProgramsForLiveTVCategoriesLoadedError() {
        showError();
    }

    @Override
    public void onError() {

    }
}
