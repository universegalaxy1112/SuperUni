package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.RequiresApi;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.LoadingMoviesViewModel;
import com.uni.julio.supertv.viewmodel.LoadingMoviesViewModelContract;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.List;
public class LoadingActivity extends BaseActivity implements LoadingMoviesViewModelContract.View{
    private int serieId;
    private LoadingMoviesViewModel loadingMoviesViewModel;

    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return loadingMoviesViewModel;
    }
    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
        mainCategoryId = extras.getInt("mainCategoryId",-1);
        movieCategoryId = extras.getInt("movieCategoryId",-1);
        serieId = extras.getInt("serieId",-1);
        loadingMoviesViewModel = new LoadingMoviesViewModel();

    }
    private boolean isInit = false;
    @Override
    public void onStart(){
        super.onStart();
        setContentView(R.layout.activity_loading);
        AVLoadingIndicatorView avi=findViewById(R.id.avi);
         if(!isInit){
            if(selectedType== ModelTypes.SelectedType.MAIN_CATEGORY){
                loadingMoviesViewModel.loadSubCategories(mainCategoryId);
            }
            else if(selectedType== ModelTypes.SelectedType.SERIES){
                loadingMoviesViewModel.loadSeasons(mainCategoryId,movieCategoryId,serieId);
            }
            isInit=true;
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
        if(Device.treatAsBox){
            launchActivity(MoviesTvActivity.class, extras);
        }else{
            launchActivity(MoviesActivity.class, extras);
        }

        getActivity().finish();
    }

    @Override
    public void onSubCategoriesForMainCategoryLoadedError() {
        showError();
    }
    public void showError() {
        if(Connectivity.isConnected()) {
            Dialogs.showOneButtonDialog(getActivity(), R.string.generic_error_title, R.string.generic_loading_message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    getActivity().finish();
                    finishActivity();
                }
            });
        }
        else {
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
    @Override
    public void onSeasonsForSerieLoaded() {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", ModelTypes.SelectedType.SEASONS);
        extras.putInt("mainCategoryId", mainCategoryId);
        extras.putInt("movieCategoryId", movieCategoryId);
        extras.putInt("serieId", serieId);
        launchActivity(MultiSeasonDetailActivity.class, extras);
         getActivity().finish();
    }

    @Override
    public void onSeasonsForSerieLoadedError() {
        showError();
    }

    @Override
    public void onProgramsForLiveTVCategoriesLoaded() {
        List<LiveTVCategory> liveTVCategoryList = VideoStreamManager.getInstance().getLiveTVCategoriesList();
        liveTVCategoryList = VideoStreamManager.getInstance().getLiveTVCategoriesList();
        launchActivity(LiveActivity.class);
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
