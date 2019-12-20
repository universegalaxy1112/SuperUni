package com.uni.julio.supertv.viewmodel;

import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.GridViewAdapter;
import com.uni.julio.supertv.adapter.MoviesRecyclerAdapter;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Connectivity;
import com.uni.julio.supertv.utils.DataManager;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public class SearchViewModel implements SearchViewModelContract.ViewModel, MovieSelectedListener {

    private final MainCategory mMainCategory;
    public ObservableBoolean isConnected;
    private SearchViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private EditText mEditText;
    private List<VideoStream> movies;
    private MoviesRecyclerAdapter rowsRecyclerAdapter;
    private int selectedId = -1;//-1 = search
    private int columns = 3;//default for portrait
    private TVRecyclerView mMoviesGridRV;
    private Pattern pattern;

    public SearchViewModel(Context context, MainCategory mainCategory) {
        ;//Log.d("liveTV","SearchViewModel from "+mainCategory.getModelType());
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
        mMainCategory = mainCategory;
        pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
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
    public void showMovieList(TVRecyclerView moviesGridRV, String query,final boolean searchSerie) {
        columns = 3;
         movies = videoStreamManager.searchForMovies(mMainCategory,query,searchSerie);
        GridViewAdapter moreVideoAdapter=new GridViewAdapter(mContext,moviesGridRV,movies,0,this);
        mLayoutManager=new GridLayoutManager(mContext,Integer.parseInt(mContext.getString(R.string.more_video)));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moviesGridRV.setLayoutManager(mLayoutManager);
        moviesGridRV.setAdapter(moreVideoAdapter);
        if (moviesGridRV.getItemDecorationCount() == 0) {
            moviesGridRV.addItemDecoration(new RecyclerViewItemDecoration(48,16,0,16));
        }
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

}