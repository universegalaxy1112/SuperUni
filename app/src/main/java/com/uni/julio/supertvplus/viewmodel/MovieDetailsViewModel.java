package com.uni.julio.supertvplus.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.databinding.ActivityOneseasonDetailBinding;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.StringRequestListener;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.utils.networing.WebConfig;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieDetailsViewModel implements MovieDetailsViewModelContract.ViewModel, StringRequestListener {

    private int mMainCategoryId;
    private MovieDetailsViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private Movie mMovie;
     public ObservableBoolean isFavorite;
    private ObservableBoolean isSeen;
    private boolean hidePlayFromStart = false;
    public ObservableBoolean isHD;
    public ObservableBoolean isSD;
    public ObservableBoolean isTrailer;
    private ActivityOneseasonDetailBinding movieDetailsBinding;
    private int likes = 0;
    private int dislikes = 0;
    public ObservableBoolean liked = new ObservableBoolean(false);
    public ObservableBoolean disliked = new ObservableBoolean(false);
    private boolean isRequested = false;
    private int reportType = -1;
    private Button po;
    public MovieDetailsViewModel(Context context, int mainCategoryId) {
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
        mMainCategoryId=mainCategoryId;
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        this.viewCallback = (MovieDetailsViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void report(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.reportTitle)
                .setView(buildView())
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reportUrl = WebConfig.reportUrl.replace("{USER}", LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName())
                                .replace("{TIPO}", String.valueOf(mMovie.getCategoryType() == -1 ? 1  : mMovie.getCategoryType()))
                                .replace("{CVE}", String.valueOf(mMovie.getContentId()))
                                .replace("{ACT}", String.valueOf(reportType));
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
        View view = inflater.inflate(R.layout.reportlayout, null);
        RadioGroup radioGroup = view.findViewById(R.id.RGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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
                        reportType = 0;
                        break;
                }
            po.requestFocus();

            }
        });
        return view;
    }
    @Override
    public void showMovieDetails(Movie movie, ActivityOneseasonDetailBinding movieDetailsBinding , int mainCategoryId) {
        mMainCategoryId=mainCategoryId;
        this.movieDetailsBinding=movieDetailsBinding;
        if(mainCategoryId == 4) { //eventos
            hidePlayFromStart = true;
        }
        if(hidePlayFromStart) {
            isSeen = new ObservableBoolean(false);
        }
        else {
            isSeen = new ObservableBoolean(videoStreamManager.isLocalSeen(String.valueOf(movie.getContentId())));
        }
        isHD=movie.getStreamUrl()==null||movie.getStreamUrl().equals("null")||movie.getStreamUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isSD=(movie.getSDUrl()==null||movie.getSDUrl().equals("null")||movie.getSDUrl().equals(""))?new ObservableBoolean(true):new ObservableBoolean(false);
        isTrailer=movie.getTrailerUrl()==null||movie.getTrailerUrl().equals("null")||movie.getTrailerUrl().equals("")?new ObservableBoolean(true):new ObservableBoolean(false);
        isFavorite = new ObservableBoolean(videoStreamManager.isLocalFavorite(String.valueOf(movie.getContentId())));
        isHD.notifyChange();
        isSD.notifyChange();
        isTrailer.notifyChange();
        mMovie = movie;
        movieDetailsBinding.setMovieDetailItem(movie);
        movieDetailsBinding.play.requestFocus();
        getLike();
    }
    private void getLike(){
        if(isRequested){
            Toast.makeText(mContext, R.string.processing, Toast.LENGTH_SHORT).show();
            return;
        }
        isRequested = true;
        String url = WebConfig.getLikeURL.replace("{MOVIEID}",String.valueOf(mMovie.getContentId()))
                .replace("{USERID}", LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName() == null ? "" : LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName());
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
                            .replace("{MOVIEID}",String.valueOf(mMovie.getContentId()))
                            .replace("{LIKE}","1")
                            .replace("{DISLIKE}","0")
                            .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName() == null ? "" : LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.like.setText(String.valueOf(++this.likes));
        }
        else{
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",String.valueOf(mMovie.getContentId()))
                            .replace("{LIKE}","-1")
                            .replace("{DISLIKE}","0")
                            .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.like.setText(String.valueOf(--this.likes));
        }
        liked.set(!liked.get());
        disliked.notifyChange();
    }
    public void dislike(View view){
        if(isRequested){
            Toast.makeText(mContext, R.string.processing, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!disliked.get()){
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",String.valueOf(mMovie.getContentId()))
                            .replace("{LIKE}","0")
                            .replace("{DISLIKE}","1")
                            .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.dislike.setText(String.valueOf(++this.dislikes));
        }
        else{
            NetManager.getInstance().makeStringRequest(
                    WebConfig.likeURL
                            .replace("{MOVIEID}",String.valueOf(mMovie.getContentId()))
                            .replace("{LIKE}","0")
                            .replace("{DISLIKE}","-1")
                            .replace("{USERID}",LiveTvApplication.getUser() == null ? "" : LiveTvApplication.getUser().getName()), this);
            movieDetailsBinding.dislike.setText(String.valueOf(--this.dislikes));
        }
        disliked.set(!disliked.get());
        disliked.notifyChange();

    }
     public void finishActivity(View view) {
        viewCallback.finishActivity();
     }

    public void playHD(View view){
        onPlay(0);
    }
    public void playSD(View view){
        onPlay(1);
    }
    public void playTrailor(View view) {
        if(!isTrailer.get())
            onPlay(2);
    }
    private void onPlay(int type) {
        if(!videoStreamManager.getSeenMovies().contains(String.valueOf(mMovie.getContentId()))) {
            videoStreamManager.setLocalSeen(String.valueOf(mMovie.getContentId()));
            if(!hidePlayFromStart) {
                isSeen.set(true);
            }
            addRecentMovies(mMovie);
            isSeen.notifyChange();
            DataManager.getInstance().saveData("seenMovies", videoStreamManager.getSeenMovies());
        }
        viewCallback.onPlaySelected(mMovie, type);
    }
    public void onClickFavorite(View view) {
        if(isFavorite.get()) {
            videoStreamManager.removeLocalFavorite(String.valueOf(mMovie.getContentId()));
            isFavorite.set(false);
            removeFavorite(mMovie);
        }
        else {
            videoStreamManager.setLocalFavorite(String.valueOf(mMovie.getContentId()));
            isFavorite.set(true);
            addFavorite(mMovie);
        }
        isFavorite.notifyChange();
        DataManager.getInstance().saveData("favoriteMoviesTotal", videoStreamManager.getFavoriteMovies());
    }
    private void addFavorite(Movie movie){
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.MOVIE_CATEGORIES)) {
            serieType = "favoriteMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.ENTERTAINMENT_CATEGORIES)){
            serieType = "favoriteEntertainment";
        }else{
            serieType = "favoriteMovies";
        }
       String favoriteMovies=DataManager.getInstance().getString(serieType,"");
       if(TextUtils.isEmpty(favoriteMovies)){
           List<Movie> movies=new ArrayList<>();
           movies.add(movie);
           DataManager.getInstance().saveData(serieType,new Gson().toJson(movies));
       }
       else{
           List<Movie> movieList=new Gson().fromJson(favoriteMovies,new TypeToken<List<Movie>>(){}.getType());
           movieList.add(0,movie);
           DataManager.getInstance().saveData(serieType,new Gson().toJson(movieList));
       }
    }
    private void removeFavorite(Movie movie){
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.MOVIE_CATEGORIES)) {
            serieType = "favoriteMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.ENTERTAINMENT_CATEGORIES)){
            serieType = "favoriteEntertainment";
        }
        String favoriteMovies=DataManager.getInstance().getString(serieType,"");
        if(TextUtils.isEmpty(favoriteMovies)){
            List<Movie> movies=new ArrayList<>();
            //movies.add(movie);
            DataManager.getInstance().saveData(serieType,new Gson().toJson(movies));
        }
        else{
            List<Movie> movieList=new Gson().fromJson(favoriteMovies,new TypeToken<List<Movie>>(){}.getType());
            movieList.remove(movie);
            DataManager.getInstance().saveData(serieType,new Gson().toJson(movieList));
        }
    }
    private void addRecentMovies(Movie movie) {
        String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.MOVIE_CATEGORIES)) {
            serieType = "recentMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.ENTERTAINMENT_CATEGORIES)){
            serieType = "recentEntertainment";
        }
        String recentMovies = DataManager.getInstance().getString(serieType,"");
        if (TextUtils.isEmpty(recentMovies)) {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            DataManager.getInstance().saveData(serieType, new Gson().toJson(movies));
        }
        else {
            List<Movie> movieList = new Gson().fromJson(recentMovies, new TypeToken<List<Movie>>() { }.getType());
            boolean needsToAdd = true;
            Iterator it = movieList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (movie.getContentId() == ((Movie) it.next()).getContentId()) {
                    needsToAdd = false;
                    break;
                }
            }
            if (needsToAdd) {
                if (movieList.size() == 10) {
                    movieList.remove(9);
                }
                movieList.add(0, movie);
                DataManager.getInstance().saveData(serieType, new Gson().toJson( movieList));
            }
        }
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
    public void onError() {
        isRequested = false;
    }
}