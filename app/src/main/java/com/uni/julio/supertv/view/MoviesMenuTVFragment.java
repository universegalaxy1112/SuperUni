package com.uni.julio.supertv.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.app.HeadersFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.tooltip.Tooltip;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.CustomListRow;
import com.uni.julio.supertv.adapter.CustomListRowPresenter;
import com.uni.julio.supertv.adapter.MoviesPresenter;
import com.uni.julio.supertv.adapter.SortedArrayObjectAdapter;
import com.uni.julio.supertv.binding.BindingAdapters;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LoadMoviesForCategoryResponseListener;
import com.uni.julio.supertv.model.ListRowComparator;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoviesMenuTVFragment extends BrowseFragment implements LoadMoviesForCategoryResponseListener {

    private SortedArrayObjectAdapter mRowsAdapter;
    private ModelTypes.SelectedType selectedType;
    private int serieId;
    public int mainCategoryId;
    public  int movieCategoryId;
    public BackgroundManager mBackgroundManager;
    private Timer mBackgroundTimer;
    public URI mBackgroundURI;
    public final Handler mHandler = new Handler();
    private List<MovieCategory> mCategoriesList;
    private TextView title;
    private TextView release_date;
    private RatingBar ratingBar;
    private TextView length;
    private TextView description;
    private CardView hd;
    public MoviesMenuTVFragment() {
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        try{
            setupUIElements();
            setupEventListeners();
            prepareEntranceTransition();
            prepareBackgroundManager();
            this.mRowsAdapter = new SortedArrayObjectAdapter((Comparator) new ListRowComparator(), (Presenter) new CustomListRowPresenter());
            setAdapter(this.mRowsAdapter);
            loadData();
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

    private void setupEventListeners() {
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.wp_yellow));
        if(mainCategoryId != 4 && mainCategoryId != 7 && mainCategoryId != 8 && mainCategoryId != 9 )
        setOnSearchClickedListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                Bundle extras = new Bundle();
                extras.putSerializable("selectedType", MoviesMenuTVFragment.this.selectedType);
                extras.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                extras.putInt("movieCategoryId", MoviesMenuTVFragment.this.movieCategoryId);
                MoviesMenuTVFragment.this.launchActivity(SearchActivity.class, extras);
            }
        });
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
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }
    private void prepareBackgroundManager() {
        this.mBackgroundManager = BackgroundManager.getInstance(getActivity());
        this.mBackgroundManager.attach(getActivity().getWindow());
        this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.detail_background));
        DisplayMetrics mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupUIElements() {
        setTitle("Movies");
        setHeadersState(1);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.contact_us_link_color));
        //setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }
    public void loadData() {
            mCategoriesList = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories();
            setTitle(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getCatName());
            for (int i = 0; i < mCategoriesList.size(); i++) {
                if(i < Math.min(mCategoriesList.size(), 10))
                    load(i);
                else
                    Preload(i);
            }
    }
    public void Preload(int row) {

        if(mCategoriesList.get(row).isLoaded()) {
            loadRow(mCategoriesList.get(row));
        }
    }
    private void load(int row){
        if(mCategoriesList.get(row).hasErrorLoading()){
            // Displaying Error Message
        }
        if(!mCategoriesList.get(row).isLoaded() && !mCategoriesList.get(row).isLoading()) {
            mCategoriesList.get(row).setLoading(true);
            NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId),  mCategoriesList.get(row), this, 30);
        }else{
            loadRow(mCategoriesList.get(row));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        try{
            mCategoriesList = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories();
            if(mCategoriesList.size() > 0 && mCategoriesList.get(0).getCatName().equals("Favorite"))
                NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(0), this, 30);
            if(mCategoriesList.size() > 1 && mCategoriesList.get(1).getCatName().equals("Vistas Recientes"))
                NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(1), this, 30);
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
    private void loadRow(MovieCategory category) {
        try {
            HeaderItem header = new HeaderItem((long) category.getId(), category.getCatName());
            List<Movie> movieList = (List<Movie>) category.getMovieList();
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter( new MoviesPresenter(getActivity()));
            listRowAdapter.addAll(0, movieList);
            CustomListRow r = new CustomListRow(header, listRowAdapter);
            r.setId(category.getId());
            for(int i=0; i<mRowsAdapter.size();i++){
                if(((ListRow)mRowsAdapter.get(i)).getId() == category.getId()){
                    mRowsAdapter.removeItems(category.getId(),1);
                    this.mRowsAdapter.add(r);
                    return;
                }
            }
            this.mRowsAdapter.add(r);
            synchronized (mRowsAdapter){
                this.mRowsAdapter.notifyItemRangeChanged(mRowsAdapter.size(), 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onMoviesForCategoryCompleted(MovieCategory movieCategory) {
        movieCategory.setLoaded(true);
        if(!movieCategory.hasErrorLoading()) {
            movieCategory.setLoading(false);
            movieCategory.setErrorLoading(false);
            loadRow(movieCategory);
        }
    }
    @Override
    public void onMoviesForCategoryCompletedError(MovieCategory movieCategory) {
        movieCategory.setLoaded(false);
        movieCategory.setErrorLoading(true);
        //NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(0), this, 30);
    }
    public void addRecentSerie(Serie serie) {
        DataManager.getInstance().saveData("lastSerieSelected", new Gson().toJson(serie));
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
                    if(mainCategoryId == 4  || mainCategoryId == 7){
                        onPlaySelectedDirect(movie, mainCategoryId);
                    }else{
                        extras2.putString("movie", new Gson().toJson( movie));
                        extras2.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                        extras2.putInt("movieCategoryId",movie.getMovieCategoryIdOwner());
                        MoviesMenuTVFragment.this.startActivity(MoviesMenuTVFragment.this.getLaunchIntent(OneSeasonDetailActivity.class, extras2));
                        MoviesMenuTVFragment.this.getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }
            }
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
        ActivityCompat.startActivityForResult(getActivity(), launchIntent,100,null);
    }
    public Intent getLaunchIntent(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        return launchIntent;
    }
    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @SuppressLint("CutPasteId")
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                try {
                    if (!(((Movie) item).getPosition() == -1 )) {
                        int target = Math.min(mCategoriesList.size(),(int) row.getId() + 10);
                        if(row.getId() < (mCategoriesList.size() - 1) && (mCategoriesList.get((int)row.getId() + 1).hasErrorLoading() || (!mCategoriesList.get((int)row.getId() + 1).isLoading() && !mCategoriesList.get((int)row.getId() + 1).isLoaded()))){
                                for(int i = (int) row.getId() + 1; i < target; i++ ) {
                                    if(mCategoriesList.size() > i)
                                        load(i);
                                }
                        }
                        setPreviewDetails(item);
                        MoviesMenuTVFragment.this.mBackgroundURI = new URI(((Movie) item).getHDFondoUrl());
                    }
                    MoviesMenuTVFragment.this.startBackgroundTimer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void setPreviewDetails(final Object item){
        if(title != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    title.setText(((Movie)item).getTitle());
                    release_date.setText(((Movie)item).getReleaseDate());
                    description.setText(((Movie)item).getDescription());
                    BindingAdapters.visibleInt(ratingBar,((Movie)item).getStarRating());
                    BindingAdapters.setDuration(length,((Movie)item).getLength());
                    BindingAdapters.bindInvisibleVisibility(hd,!((Movie)item).isHDBranded());
                    BindingAdapters.setRating(ratingBar,(((Movie)item).getStarRating()));
                }
            });
        }else{
            View view=getActivity().findViewById(R.id.scale_frame);
            title=view.findViewById(R.id.title);
            release_date=view.findViewById(R.id.release_date);
            ratingBar= view.findViewById(R.id.ratingBar);
            length=view.findViewById(R.id.length);
            description=view.findViewById(R.id.description_detail);
            hd= view.findViewById(R.id.hd);
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
    public void updateBackground(final String uri) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            .load(uri)
                            .error(R.drawable.placeholder)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    mBackgroundManager.setDrawable(resource);
                                }
                            });
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void clearBackground() {
        this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.bg_general));
        this.mBackgroundTimer.cancel();
    }
}
