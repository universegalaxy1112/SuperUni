package com.uni.julio.supertvplus.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.app.HeadersSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowPresenter;


import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.adapter.CustomListRow;
import com.uni.julio.supertvplus.adapter.CustomListRowPresenter;
import com.uni.julio.supertvplus.adapter.MoviesPresenter;
import com.uni.julio.supertvplus.adapter.SortedArrayObjectAdapter;
import com.uni.julio.supertvplus.binding.BindingAdapters;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.LoadMoviesForCategoryResponseListener;
import com.uni.julio.supertvplus.model.ListRowComparator;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.view.exoplayer.VideoPlayFragment;
import java.util.List;
import java.util.Objects;


public class MoviesMenuTVFragment extends BrowseSupportFragment implements LoadMoviesForCategoryResponseListener {

    private SortedArrayObjectAdapter mRowsAdapter;
    private ModelTypes.SelectedType selectedType;
    private int serieId;
    public int mainCategoryId;
    private int movieCategoryId;
    private BackgroundManager mBackgroundManager;
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
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        this.selectedType = (ModelTypes.SelectedType) Objects.requireNonNull(extras).get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void launchActivity(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        startActivityForResult(launchIntent, 100);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try{
            setupUIElements();
            setupEventListeners();
            prepareEntranceTransition();
            prepareBackgroundManager();
            this.mRowsAdapter = new SortedArrayObjectAdapter( new ListRowComparator(),  new CustomListRowPresenter());
            setAdapter(this.mRowsAdapter);
        }catch (Exception e){
            e.printStackTrace();
            Dialogs.showOneButtonDialog(getActivity(), R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        }
    }


    private void setupEventListeners() {
        setSearchAffordanceColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.wp_yellow));
        if(mainCategoryId != 4 && mainCategoryId != 7 && mainCategoryId != 8 && mainCategoryId != 9 )
        setOnSearchClickedListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                Bundle extras = new Bundle();
                extras.putSerializable("selectedType", MoviesMenuTVFragment.this.selectedType);
                extras.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                extras.putInt("movieCategoryId", MoviesMenuTVFragment.this.movieCategoryId);
                launchActivity(SearchTvActivity.class, extras);
            }
        });

        getHeadersSupportFragment().setOnHeaderClickedListener(new HeadersSupportFragment.OnHeaderClickedListener() {
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
        try{
            this.mBackgroundManager = BackgroundManager.getInstance(Objects.requireNonNull(getActivity()));
            this.mBackgroundManager.attach(getActivity().getWindow());
            this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.detail_background));
            DisplayMetrics mMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupUIElements() {
        setTitle("Movies");
        setHeadersState(1);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.contact_us_link_color));
    }
    private void loadData(int start) {
            mCategoriesList = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories();
            setTitle(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getCatName());
            int loadCount = mCategoriesList.size();
            if(mainCategoryId == 9 ) loadCount = 10;
            for (int i = start; i < mCategoriesList.size(); i++) {
                mCategoriesList.get(i).setCategoryDisplayed(false);
                if(i < loadCount)
                    load(i);
            }
    }

    private void load(int row){
        if(mCategoriesList.get(row).hasErrorLoading()){
            // Displaying Error Message
        }
        if(!mCategoriesList.get(row).isLoaded() && !mCategoriesList.get(row).isLoading()) {
            mCategoriesList.get(row).setLoading(true);
            NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId),  mCategoriesList.get(row), this, 30);
        }else if(!mCategoriesList.get(row).isLoading() && ! mCategoriesList.get(row).isCategoryDisplayed()){
            loadRow(mCategoriesList.get(row));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        try{
            mCategoriesList = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories();
            int loadStart = 0;
            if(mCategoriesList.size() > 0 && mCategoriesList.get(0).getCatName().equals("Favorite"))
            {
                NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(0), this, 30);
                loadStart = 1;
            }
            if(mCategoriesList.size() > 1 && mCategoriesList.get(1).getCatName().equals("Vistas Recientes"))
            {
                NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), mCategoriesList.get(1), this, 30);
                loadStart = 2;
            }
            loadData(loadStart);

        }catch (Exception e){
            e.printStackTrace();
            Dialogs.showOneButtonDialog(getActivity(), R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        }
    }
    private void loadRow(final MovieCategory category) {
        try {
            if(getActivity() != null)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    category.setCategoryDisplayed(true);
                    HeaderItem header = new HeaderItem( category.getId(), category.getCatName());
                    List<? extends VideoStream> movieList = category.getMovieList();
                    ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter( new MoviesPresenter(getActivity()));
                    listRowAdapter.addAll(0, movieList);
                    CustomListRow r = new CustomListRow(header, listRowAdapter);
                    r.setId(category.getId());
                    for(int i = 0; i <  mRowsAdapter.size(); i++) {
                        if(((ListRow)mRowsAdapter.get(i)).getId() == category.getId()) {
                            if (mRowsAdapter.get(i) != null) {
                                mRowsAdapter.remove(mRowsAdapter.get(i));
                                break;
                            }
                        }
                    }
                    MoviesMenuTVFragment.this.mRowsAdapter.add(r);
                }
            });

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
        movieCategory.setLoading(false);
        movieCategory.setErrorLoading(true);
    }
    private void addRecentSerie(Serie serie) {
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
                extras.putString("serie", new Gson().toJson(serie));
                DataManager.getInstance().saveData("lastSerieSelected",new Gson().toJson(serie));
                launchActivity(LoadingActivity.class, extras);
            }else if(item instanceof Movie) {
                Movie movie = (Movie) item;
                Bundle extras2 = new Bundle();
                if (((Movie) item).getPosition() != -1) {
                    if(mainCategoryId == 4  || mainCategoryId == 7){
                        onPlaySelectedDirect(movie, mainCategoryId);
                    }else{
                        extras2.putString("movie", new Gson().toJson( movie));
                        extras2.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                        launchActivity(OneSeasonDetailActivity.class, extras2);
                        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }
            }
        }
    }


    private void onPlaySelectedDirect(Movie movie, int mainCategoryId) {
        int movieId = movie.getContentId();
        String[] uris = {movie.getStreamUrl()};
        String[] extensions = {movie.getStreamUrl().substring(movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        Intent launchIntent = new Intent(LiveTvApplication.appContext, VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra(VideoPlayFragment.MOVIE_ID_EXTRA, movieId)
                .putExtra(VideoPlayFragment.SECONDS_TO_START_EXTRA, 0L)
                .putExtra("mainCategoryId", mainCategoryId)
                .putExtra("type", 0)
                .putExtra("subsURL", movie.getSubtitleUrl())
                .putExtra("title", movie.getTitle())
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
        ActivityCompat.startActivityForResult(Objects.requireNonNull(getActivity()), launchIntent,100,null);
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @SuppressLint("CutPasteId")
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            try{
                if (item instanceof Movie && !(((Movie) item).getPosition() == -1 )) {
                    setPreviewDetails(item);
                    MoviesMenuTVFragment.this.updateBackground(((Movie) item).getHDFondoUrl());
                }
                int target = Math.min(mCategoriesList.size(),(int) row.getId() + 10);
                for(int i = (int) row.getId() + 1; i < target; i++ ) {
                    if(!mCategoriesList.get(i).isCategoryDisplayed() && !mCategoriesList.get(i).isLoading())
                        load(i);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private void setPreviewDetails(final Object item){
        if(getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(title != null){
                    title.setText(((Movie)item).getTitle());
                    release_date.setText(((Movie)item).getReleaseDate());
                    description.setText(((Movie)item).getDescription());
                    BindingAdapters.visibleInt(ratingBar,((Movie)item).getStarRating());
                    BindingAdapters.setDuration(length,((Movie)item).getLength());
                    BindingAdapters.bindInvisibleVisibility(hd,!((Movie)item).isHDBranded());
                    BindingAdapters.setRating(ratingBar,(((Movie)item).getStarRating()));
                }else{
                    View view= Objects.requireNonNull(getActivity()).findViewById(R.id.scale_frame);
                    title=view.findViewById(R.id.title);
                    release_date=view.findViewById(R.id.release_date);
                    ratingBar= view.findViewById(R.id.ratingBar);
                    length=view.findViewById(R.id.length);
                    description=view.findViewById(R.id.description_detail);
                    hd= view.findViewById(R.id.hd);
                }
            }
        });

    }

    private void updateBackground(final String uri) {
        try {
            if(getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Target target = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Objects.requireNonNull(mBackgroundManager).setBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {}
                        };
                        Picasso.get().load(uri).placeholder(R.drawable.placeholder).into(target);
                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
