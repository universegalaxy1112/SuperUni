package com.uni.julio.supertv.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.app.HeadersFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.CursorObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowPresenter;
import androidx.loader.app.LoaderManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.MoviesPresenter;
import com.uni.julio.supertv.adapter.SortedArrayObjectAdapter;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LoadMoviesForCategoryResponseListener;
import com.uni.julio.supertv.model.ListRowComparator;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.viewmodel.MoviesMenuViewModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MoviesMenuTVFragment extends BrowseFragment implements LoadMoviesForCategoryResponseListener {

    private SortedArrayObjectAdapter mRowsAdapter;
    private MoviesMenuViewModel moviesModelViewModel;
    private ModelTypes.SelectedType selectedType;
    private int serieId;
    public int mainCategoryId;
    public  int movieCategoryId;
    private Map<Integer, CursorObjectAdapter> mVideoCursorAdapters;
    private LoaderManager mLoaderManager;
    private static final int CATEGORY_LOADER = 123; // Unique ID for Category Loader.
    public BackgroundManager mBackgroundManager;
    private Timer mBackgroundTimer;
    public URI mBackgroundURI;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private DisplayMetrics mMetrics;
    List<MovieCategory> mCategoriesList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.moviesModelViewModel = new MoviesMenuViewModel(getActivity().getApplicationContext());
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
       }
    public void launchActivity(Class classToLaunch) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void launchActivity(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUIElements();
        setupEventListeners();
        prepareEntranceTransition();
        prepareBackgroundManager();
        this.mRowsAdapter = new SortedArrayObjectAdapter((Comparator) new ListRowComparator(), (Presenter) new ListRowPresenter());
        setAdapter(this.mRowsAdapter);
        loadData();
        getHeadersFragment().setOnHeaderClickedListener(new HeadersFragment.OnHeaderClickedListener() {

           @Override
           public void onHeaderClicked(RowHeaderPresenter.ViewHolder viewHolder, Row row) {
               ListRow listRow = (ListRow)row;
               Bundle extras = new Bundle();
               if (serieId == -1) {
                   extras.putSerializable("selectedType", selectedType);
                   extras.putInt("mainCategoryId", mainCategoryId);
                   extras.putInt("movieCategoryId", (int) listRow.getId());
               }
               launchActivity(MoreVideoActivity.class, extras);
           }
       });
    }
    private void setupEventListeners() {
            setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.wp_yellow));
            setOnSearchClickedListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                    Bundle extras = new Bundle();
                    extras.putSerializable("selectedType", MoviesMenuTVFragment.this.selectedType);
                    extras.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                    extras.putInt("movieCategoryId", MoviesMenuTVFragment.this.movieCategoryId);
                    MoviesMenuTVFragment.this.launchActivity(SearchActivity.class, extras);
                }
            });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }
    private void prepareBackgroundManager() {
        this.mBackgroundManager = BackgroundManager.getInstance(getActivity());
        this.mBackgroundManager.attach(getActivity().getWindow());
        this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.detail_background));
        this.mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(this.mMetrics);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupUIElements() {

        setTitle("Movies");
        setHeadersState(1);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.contact_us_link_color));
        //setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }
    private void loadData() {
        if(VideoStreamManager.getInstance().getMainCategoriesList().size()<7){
            VideoStreamManager.getInstance().FillMainCategories();
        }
            mCategoriesList = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories();
            setTitle(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getCatName());
            for (int i = 0; i < mCategoriesList.size(); i++) {
                loadHeader(mCategoriesList.get(i));
                load(i);
            }
    }
    private void load(int row){
        if(mCategoriesList.get(row).hasErrorLoading()){

        }
        if(!mCategoriesList.get(row).isLoaded()) {
            NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId),  mCategoriesList.get(row), this, 60);
        }else{
            loadRow(mCategoriesList.get(row));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(0), this, 60);
        NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(1), this, 60);
    }
    private void loadRow(MovieCategory category) {
        HeaderItem header = new HeaderItem((long) category.getId(), category.getCatName());
        Movie showAsGrid = new Movie();
        showAsGrid.setTitle("Ver Todas");
        showAsGrid.setPosition(-1);
        List<Movie> movieList = (List<Movie>) category.getMovieList();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((Presenter) new MoviesPresenter(getActivity().getApplicationContext()));
        listRowAdapter.addAll(0, movieList);
        showAsGrid.setLength(category.getId());
        ListRow r = new ListRow(header, listRowAdapter);
        r.setId((long) category.getId());
        this.mRowsAdapter.removeItems(category.getId(),1);
        this.mRowsAdapter.add(r);
        synchronized(this.mRowsAdapter){
            this.mRowsAdapter.notify();
        }
    }
    private void loadHeader(MovieCategory category){
        HeaderItem header = new HeaderItem((long) category.getId(), category.getCatName());
        Movie showAsGrid = new Movie();
        showAsGrid.setTitle("Ver Todas");
        showAsGrid.setPosition(-1);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((Presenter) new MoviesPresenter(getActivity().getApplicationContext()));
        listRowAdapter.addAll(0, new ArrayList());
        showAsGrid.setLength(category.getId());
        ListRow r = new ListRow(header, listRowAdapter);
        r.setId((long) category.getId());
        this.mRowsAdapter.add(r);
        synchronized(this.mRowsAdapter){
            this.mRowsAdapter.notify();
        }
    }
    public void onMoviesForCategoryCompleted(MovieCategory movieCategory) {
        movieCategory.setLoaded(true);
        if(!movieCategory.hasErrorLoading()) {
            movieCategory.setLoading(false);
            movieCategory.setErrorLoading(false);
            if( movieCategory.getCatName().contains("ettings")) {
                movieCategory.setCatName("");
                if(movieCategory.getMovieList().size() > 1) {
                    movieCategory.getMovieList().remove(1);
                }
                movieCategory.getMovieList().get(0).setTitle("Buscar");
                ((Movie)movieCategory.getMovieList().get(0)).setHDPosterUrl("lupita");
            }
            loadRow(movieCategory);
            VideoStreamManager.getInstance().getMainCategory(mainCategoryId).addMovieCategory(movieCategory.getId(), movieCategory);
        }

    }
    @Override
    public void onMoviesForCategoryCompletedError(MovieCategory movieCategory) {
        movieCategory.setErrorLoading(false);
        //VideoStreamManager.getInstance().getMainCategory(mainCategoryId).addMovieCategory(movieCategory.getId(), movieCategory);
    }
    public void addRecentSerie(Serie serie) {
        DataManager.getInstance().saveData("lastSerieSelected", new Gson().toJson((Object) serie));
    }

    @Override
    public void onError() {

    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        private ItemViewClickedListener() {

        }

        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Serie) {
                Serie serie = (Serie) item;
                MoviesMenuTVFragment.this.addRecentSerie(serie);
                Bundle extras = new Bundle();
                extras.putSerializable("selectedType", ModelTypes.SelectedType.SERIES);
                extras.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                extras.putInt("movieCategoryId", serie.getMovieCategoryIdOwner());
                extras.putInt("serieId", serie.getPosition());
                extras.putString("serie", new Gson().toJson(serie));
                DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
                MoviesMenuTVFragment.this.launchActivity(LoadingActivity.class, extras);
            }else if(item instanceof Movie) {
                Movie movie = (Movie) item;
                Bundle extras2 = new Bundle();
                if (((Movie) item).getPosition() != -1) {
                    extras2.putString("movie", new Gson().toJson( movie));
                    extras2.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                    extras2.putInt("movieCategoryId",movie.getMovieCategoryIdOwner());
                    MoviesMenuTVFragment.this.startActivity(MoviesMenuTVFragment.this.getLaunchIntent(OneSeasonDetailActivity.class, extras2));
                    MoviesMenuTVFragment.this.getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    return;
                }

            }
        }

    }
    public Intent getLaunchIntent(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        return launchIntent;
    }
    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                try {
                    if (!(((Movie) item).getPosition() == -1 || ((Movie) item).getHDFondoUrl() == null)) {
                        MoviesMenuTVFragment.this.mBackgroundURI = new URI(((Movie) item).getHDFondoUrl());
                    }
                    MoviesMenuTVFragment.this.startBackgroundTimer();
                } catch (URISyntaxException e) {
                }
            }

        }
    }
    public void startBackgroundTimer() {
        if (this.mBackgroundTimer != null) {
            this.mBackgroundTimer.cancel();
        }
        this.mBackgroundTimer = new Timer();
        this.mBackgroundTimer.schedule(new UpdateBackgroundTask(), 300);
    }
    private class UpdateBackgroundTask extends TimerTask {
        private UpdateBackgroundTask() {
        }

        public void run() {
            MoviesMenuTVFragment.this.mHandler.post(new Runnable() {
                public void run() {
                    if (MoviesMenuTVFragment.this.mBackgroundURI != null) {
                        MoviesMenuTVFragment.this.updateBackground(MoviesMenuTVFragment.this.mBackgroundURI.toString());
                    }else {
                        MoviesMenuTVFragment.this.clearBackground();
                    }
                }
            });
        }
    }
    public void updateBackground(String uri) {

        Glide.with(this)
                .load(uri)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mBackgroundManager.setDrawable(resource);
                    }


                });
    }
    public void clearBackground() {
        this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.detail_background));
        this.mBackgroundTimer.cancel();
    }
}
