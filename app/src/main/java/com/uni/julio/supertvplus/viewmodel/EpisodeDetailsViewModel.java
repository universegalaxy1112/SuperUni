package com.uni.julio.supertvplus.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.adapter.MultiSeasonAdapter;
import com.uni.julio.supertvplus.databinding.ActivityMultiSeasonDetailBinding;
import com.uni.julio.supertvplus.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.EpisodeLoadListener;
import com.uni.julio.supertvplus.listeners.LoadEpisodesForSerieResponseListener;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.listeners.SeasonSelectListener;
import com.uni.julio.supertvplus.listeners.StringRequestListener;
import com.uni.julio.supertvplus.model.Episode;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.Season;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.utils.networing.WebConfig;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class EpisodeDetailsViewModel implements EpisodeDetailsViewModelContract.ViewModel, LoadEpisodesForSerieResponseListener, SeasonSelectListener,MovieSelectedListener, StringRequestListener {
    private int mMainCategoryId ;
    private Movie mMovie;
    private Season season;
    private int seasonPosition = 0;
    private EpisodeDetailsViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private List<? extends VideoStream>  movieList;
    public ObservableBoolean isFavorite;
    private ObservableBoolean isSeen;
    public ObservableBoolean isHD;
    public ObservableBoolean isSD;
    public ObservableBoolean isTrailer;
    private boolean hidePlayFromStart = false;
    private ActivityMultiSeasonDetailBinding movieDetailsBinding;
    private MultiSeasonAdapter moviesRecyclerAdapter;
    private TVRecyclerView rowsRecycler;
    private Serie serie;
    private EpisodeLoadListener episodeLoadListener;
    private int likes = 0;
    private int dislikes = 0;
    public ObservableBoolean liked = new ObservableBoolean(false);
    public ObservableBoolean disliked = new ObservableBoolean(false);
    private boolean isRequested = false;
    private int reportType = -1;
    private Button po;
    public EpisodeDetailsViewModel(Context context, int mainCategoryId) {
        videoStreamManager = VideoStreamManager.getInstance();
        this.mContext = context;
        mMainCategoryId=mainCategoryId;
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        //set the callback to the fragment (using the BaseFragment class)
        this.viewCallback = (EpisodeDetailsViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void showMovieDetails(Serie serie, ActivityMultiSeasonDetailBinding movieDetailsBinding , int mainCategoryId, EpisodeLoadListener episodeLoadListener) {
        this.mMainCategoryId=mainCategoryId;
        this.movieDetailsBinding=movieDetailsBinding;
        this.episodeLoadListener=episodeLoadListener;
        this.serie=serie;
        this.movieList = new ArrayList<>();
        ImageView fondoImage=movieDetailsBinding.fondoUrl;
        try{
            if(!TextUtils.isEmpty(serie.getHDFondoUrl()) && !serie.getHDFondoUrl().equals(" ") && !serie.getHDFondoUrl().equals("")) {
                Picasso.get().load(serie.getHDFondoUrl()).placeholder(R.drawable.placeholder).into(fondoImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess(){

                    }
                });
            }

        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        rowsRecycler = movieDetailsBinding.getRoot().findViewById(R.id.recycler_view);
        GridLayoutManager rowslayoutmanger = new GridLayoutManager(mContext, Integer.parseInt(mContext.getString(R.string.episode)));
        rowslayoutmanger.setOrientation(LinearLayoutManager.VERTICAL);
        rowsRecycler.setLayoutManager(rowslayoutmanger);
        moviesRecyclerAdapter = new MultiSeasonAdapter(mContext, rowsRecycler,movieList,  this);
        rowsRecycler.setAdapter(moviesRecyclerAdapter);
        if (rowsRecycler.getItemDecorationCount() == 0) {
            rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(6,16,6,16));
        }
        if(this.serie.getSeasons().size()>0){
            addSeasonButtons();
            showSeasonList(0);
        }
    }
    private void addSeasonButtons(){
        TabLayout tabLayout = movieDetailsBinding.detailTab;
        for(int i=0;i<serie.getSeasons().size();i++){
            TabItem tabItem=new TabItem(mContext);
            tabLayout.addView(tabItem);
            tabLayout.getTabAt(i).setText(serie.getSeason(i).getName());
            tabItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            tabItem.setPadding(2,0,2,0);
         }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showSeasonList(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public void showSeasonList(int seasonposition){
        seasonPosition = seasonposition;
        season=serie.getSeason(seasonposition);
        List<Episode> episodeList=(List<Episode>)season.getEpisodeList();
        boolean needsRedraw=true;
        if(episodeList == null||episodeList.size()==0){
            if(!season.isLoading()){
                needsRedraw=false;
                this.episodeLoadListener.showCustomProgress();
                NetManager.getInstance().retrieveEpisodesForSerie(serie,season,this);
            }
        }
        if(needsRedraw){
            movieList=season.getEpisodeList();
            moviesRecyclerAdapter.updateMovies(movieList);
            showEpisode((Movie)movieList.get(0));
        }
    }
    public void finishActivity(View view) {
        viewCallback.finishActivity();
    }

    public void report(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.reportTitle)
                .setView(buildView())
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reportUrl = WebConfig.reportUrl.replace("{USER}", LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName())
                                .replace("{TIPO}", Integer.toString(serie.getCategoryType() != -1 ? serie.getCategoryType() : 2))
                                .replace("{CVE}", Integer.toString(mMovie.getContentId()))
                                .replace("{ACT}", Integer.toString(reportType));
                        if(reportType == -1) {
                            Toast.makeText(mContext, "Select an option.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        NetManager.getInstance().makeStringRequest(reportUrl, new StringRequestListener() {
                            @Override
                            public void onCompleted(String response) {
                                reportType = -1;
                                Toast.makeText(mContext, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError() {
                                reportType = -1;
                                Toast.makeText(mContext, "Failed To report! Please check your network connection.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog=builder.create();
        dialog.show();
        Button ne=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        po=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ne.setBackground(mContext.getResources().getDrawable(R.drawable.dialog_btn_background));
        po.setBackground(mContext.getResources().getDrawable(R.drawable.dialog_btn_background));
        ne.setPadding(16,4,16,4);
        po.setPadding(16,4,16,4);
    }

    private View buildView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.reportlayout, null);
        RadioGroup radioGroup = view.findViewById(R.id.RGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case (R.id.report_audio):
                        reportType = 0;
                        break;
                    case (R.id.report_video):
                        reportType = 1;
                        break;
                    case R.id.report_subtitle:
                        reportType = 2;
                        break;
                    case R.id.report_content:
                        reportType = 3;
                        break;
                    default:
                        reportType = -1;
                        break;
                }
                if(po != null) po.requestFocus();

            }
        });
        return view;
    }

    private void setProperty(){
        if(mMainCategoryId == 4)  //eventos
            hidePlayFromStart = true;
        if(hidePlayFromStart)
            isSeen = new ObservableBoolean(false);
        else
            isSeen = new ObservableBoolean(videoStreamManager.isLocalSeen(String.valueOf(mMovie.getContentId())));
        isFavorite = new ObservableBoolean(videoStreamManager.isLocalFavorite(String.valueOf(mMovie.getContentId())));
        isHD=mMovie.getStreamUrl()==null||mMovie.getStreamUrl().equals("null")||mMovie.getStreamUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isSD=mMovie.getSDUrl()==null||mMovie.getSDUrl().equals("null")||mMovie.getSDUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isTrailer=mMovie.getTrailerUrl()==null||mMovie.getTrailerUrl().equals("null")||mMovie.getTrailerUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isHD.notifyChange();
        isSD.notifyChange();
        isTrailer.notifyChange();
        movieDetailsBinding.play.requestFocus();
    }
    public void onClickFavorite(View view) {
        if(isFavorite.get()) {
            videoStreamManager.removeLocalFavorite(String.valueOf(mMovie.getContentId()));
            isFavorite.set(false);
        }
        else {
            videoStreamManager.setLocalFavorite(String.valueOf(mMovie.getContentId()));
            isFavorite.set(true);
        }
        isFavorite.notifyChange();
        DataManager.getInstance().saveData("favoriteMoviesTotal", videoStreamManager.getFavoriteMovies());
        addFavorite();
     }


    private void addFavorite(){
        String serieType;
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.SERIES_CATEGORIES)) {
            serieType = "favoriteSerie";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.SERIES_KIDS_CATEGORIES)) {
            serieType = "favoriteKids";
        }else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.KARAOKE_CATEGORIES)){
            serieType = "favoriteKara";
        }else{
            serieType = "favoriteSerie";
        }
        String favoriteSeries=DataManager.getInstance().getString(serieType,"");
        Serie newserie;
        newserie=serie.clone();
        newserie.setSeasons(new ArrayList<Season>());
        if(TextUtils.isEmpty(favoriteSeries)){
            List<Serie> series=new ArrayList<>();
            if(checkNeedAdd()){
                series.add(newserie);
                DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
            }
        }
        else{
             List<Serie> series=new Gson().fromJson(favoriteSeries,new TypeToken<List<Serie>>(){}.getType());
            if(checkNeedAdd()){
                for(Serie serie1:series){
                    if(serie1.getContentId()==serie.getContentId()) return ;
                }
                series.add(0,newserie);
            }else{
                series.remove(newserie);
            }
            DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
        }

    }

    private boolean checkNeedAdd(){
        List<Season> seasons;
        seasons=this.serie.getSeasons();
        for(Season season:seasons){
            List<? extends VideoStream>  movieList=season.getEpisodeList();
            for(VideoStream movie:movieList){
               if(videoStreamManager.isLocalFavorite(String.valueOf(movie.getContentId()))) return true;
            }
        }
     return false;
    }
    public void playTrailer(View view) {
        if(!isTrailer.get())
            onPlay(2);
    }
    public void playHD(View view){
        onPlay(0);
    }
    public void playSD(View view){
        onPlay(1);
    }
    private void onPlay(int type) {
        if(!videoStreamManager.getSeenMovies().contains(String.valueOf(mMovie.getContentId()))) {
            videoStreamManager.setLocalSeen(String.valueOf(mMovie.getContentId()));
            if(!hidePlayFromStart) {
                isSeen.set(true);
            }
        }
        isSeen.notifyChange();
        addRecentSerie();
        DataManager.getInstance().saveData("seenMovies", videoStreamManager.getSeenMovies());
        viewCallback.onPlaySelected(mMovie, type, seasonPosition);
    }

    private void addRecentSerie() {
        try {
            String lastSelectedSerie = DataManager.getInstance().getString("lastSerieSelected", null);
            if (lastSelectedSerie != null) {
                Serie serie = new Gson().fromJson(lastSelectedSerie, Serie.class);

                String recentSeries;

                String serieType;
                if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES) {
                    serieType = "recentSeries";
                } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_KIDS_CATEGORIES) {
                    serieType = "recentKids";
                }else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.KARAOKE_CATEGORIES){
                    serieType = "recentKara";
                }else{
                    serieType = "recentSeries";
                }
                recentSeries = DataManager.getInstance().getString(serieType, "");

                if (TextUtils.isEmpty(recentSeries)) {
                    List<Serie> series = new ArrayList<>();
                    series.add(serie);

                    DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
                } else {
                    List<Serie> serieList = new Gson().fromJson(recentSeries, new TypeToken<List<Serie>>() {
                    }.getType());
                    boolean needsToAdd = true;
                    for (Serie ser : serieList) {
                        if (serie.getContentId() == ser.getContentId()) {
                            needsToAdd = false;
                            break;
                        }
                    }
                    if (needsToAdd) {
                        if (serieList.size() == 10) {
                            serieList.remove(9);
                        }
                        serieList.add(0, serie);
                        DataManager.getInstance().saveData(serieType, new Gson().toJson(serieList));
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showEpisode(Movie movie){
        mMovie = movie;
        setProperty();
        movieDetailsBinding.setMovieDetailItem(mMovie);
        movieDetailsBinding.setMovieDetailsVM(this);
        movieDetailsBinding.play.requestFocus();
        getLike();
    }
    @Override
    public void onEpisodesForSerieCompleted(Season mseason) {
        this.season=mseason;
        movieList=season.getEpisodeList();
        moviesRecyclerAdapter.updateMovies(movieList);
        showEpisode((Movie)movieList.get(0));
        this.episodeLoadListener.onLoaded();

    }
    private void getLike(){
        if(isRequested){
            Toast.makeText(mContext, R.string.processing, Toast.LENGTH_SHORT).show();
            return;
        }
        isRequested = true;
        String url = WebConfig.getLikeURL.replace("{MOVIEID}",Integer.toString(serie.getContentId()))
                .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName());
        NetManager.getInstance().makeStringRequest(url, this);
    }
    public void like(View view){
        if(isRequested)
        {
            Toast.makeText(mContext, R.string.processing, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!liked.get()){
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",Integer.toString(serie.getContentId()))
                            .replace("{LIKE}","1")
                            .replace("{DISLIKE}","0")
                            .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.like.setText(String.valueOf(++this.likes));
        }
        else{
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",Integer.toString(serie.getContentId()))
                            .replace("{LIKE}","-1")
                            .replace("{DISLIKE}","0")
                            .replace("{USERID}", LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.like.setText(String.valueOf(--this.likes));
        }
        liked.set(!liked.get());
        disliked.notifyChange();    }
    public void dislike(View view){
        if(isRequested){
            Toast.makeText(mContext, R.string.processing, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!disliked.get()){
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",Integer.toString(serie.getContentId()))
                            .replace("{LIKE}","0")
                            .replace("{DISLIKE}","1")
                            .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.dislike.setText(String.valueOf(++this.dislikes));
        }
        else{
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",Integer.toString(serie.getContentId()))
                            .replace("{LIKE}","0")
                            .replace("{DISLIKE}","-1")
                            .replace("{USERID}", LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.dislike.setText(String.valueOf(--this.dislikes));
        }
        disliked.set(!disliked.get());
        disliked.notifyChange();
    }

    @Override
    public void onError() {
        this.episodeLoadListener.onError();
    }


    @Override
    public void onCompleted(String response) {
        isRequested = false;
        try{
            if(!TextUtils.isEmpty(response)){
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.getBoolean("status");
                if(status && !jsonObject.isNull("likes")){
                    this.likes = jsonObject.getInt("likes");
                    this.dislikes = jsonObject.getInt("dislikes");
                    this.liked.set(jsonObject.getBoolean("liked"));
                    this.disliked.set(jsonObject.getBoolean("disliked"));
                    liked.notifyChange();
                    disliked.notifyChange();
                    movieDetailsBinding.like.setText(String.valueOf(this.likes));
                    movieDetailsBinding.dislike.setText(String.valueOf(this.dislikes));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMovieSelected(VideoStream movie) {
        showEpisode((Movie)movie);
    }
}