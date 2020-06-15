package com.uni.julio.supertvplus.utils.networing.parser;

import android.text.TextUtils;

import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.model.LiveProgram;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.User;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.networing.NetManager;
import com.uni.julio.supertvplus.utils.networing.WebConfig;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FetchJSonFileSync {

    final String LOG_TAG = this.getClass().getSimpleName();
    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;

//    private final String liveTVURL = "https://superteve.com/yXhj9M0pf9NZ9u4qh";

    public FetchJSonFileSync() {
        super();
    }


    public List<MovieCategory> retrieveSubCategories(MainCategory mainCategory) {
        try {
            String subCatURL = getSubCategoriesUrl(mainCategory);
            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(subCatURL);
                if(dataFromServer != null && dataFromServer.contains("\"Settings\",")){
                    dataFromServer = dataFromServer.replace("\"Settings\",","");
                }
            return ParserJSonFile.getParsedSubCategories(dataFromServer, mainCategory.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<? extends VideoStream> retrieveMovies(String mainCategory, String movieCategory, int timeOut) {
        try {
            String dataFromServer = "";

            if(movieCategory.toLowerCase().contains("vistas") && movieCategory.toLowerCase().contains("recientes")) {
                String recentMovies = "";
                switch(mainCategory) {//main category
                    case ModelTypes.MOVIE_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentMovies","");
                        break;
                    case ModelTypes.ENTERTAINMENT_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentEntertainment","");
                        break;
                    case ModelTypes.SERIES_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentSeries","");
                        break;
                    case ModelTypes.SERIES_KIDS_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentKids","");
                        break;
                    case ModelTypes.KARAOKE_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentKara","");
                        break;
                }

                if(!TextUtils.isEmpty(recentMovies)) {
                    dataFromServer = "{\"Videos\": "+recentMovies + "}";
                }
                else {
                    return new ArrayList<>();
                }
            }else if(movieCategory.toLowerCase().contains("favorite")) {
                String favoriteMovies = "";
                switch(mainCategory) {//main category
                    case ModelTypes.MOVIE_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteMovies","");
                        break;
                    case ModelTypes.ENTERTAINMENT_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteEntertainment","");
                        break;
                    case ModelTypes.SERIES_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteSerie","");
                        break;
                    case ModelTypes.SERIES_KIDS_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteKids","");
                        break;
                    case ModelTypes.KARAOKE_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteKara","");
                        break;
                }

                if(!TextUtils.isEmpty(favoriteMovies)) {
                    dataFromServer = "{\"Videos\": "+favoriteMovies + "}";
                }
                else {
                    return new ArrayList<>();
                }
            }
            else {
                String moviesForCatURL = getMoviesForCategoryUrl(mainCategory, movieCategory);
                    dataFromServer = NetManager.getInstance().makeSyncStringRequest(moviesForCatURL, timeOut);
            }
            if(dataFromServer != null)
            return ParserJSonFile.getParsedMovies(mainCategory, movieCategory, dataFromServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<? extends VideoStream> retrieveMoviesForSerie(Serie serie, int season) {
        try {
            String moviesForSerieURL = getMoviesForSerieUrl(serie, season);
            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(moviesForSerieURL);
            return ParserJSonFile.getParsedMoviesForSerie(serie, dataFromServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LiveTVCategory> retrieveLiveTVCategories(MainCategory mainCategory) {
        try {
            String subCatURL = getLiveTVCategoriesUrl(mainCategory);
            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(subCatURL);
            //Log.d("liveTV","retrieveLiveTVCategories "+dataFromServer);
            return ParserJSonFile.getParsedLiveTVCategories(dataFromServer);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LiveProgram> retrieveProgramsForLiveTVCategory(LiveTVCategory liveTVCategory) {
        User user = LiveTvApplication.getUser();
        String password = user.getPassword();
         try {
            String moviesForCatURL = getProgramsForLiveTVCategoryUrl(liveTVCategory);
            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(moviesForCatURL+"&s=" + password);
            return ParserJSonFile.getParsedProgramsForLiveTVCategory(liveTVCategory, dataFromServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLiveTVCategoriesUrl(MainCategory mainCategory) {
        return WebConfig.liveTVCategoriesURL;
//        return "https://superteve.com/android/API/live_categorias.php";
    }

    private String getProgramsForLiveTVCategoryUrl(LiveTVCategory liveTVCategory) {
        return WebConfig.liveTVChannelsURL.replace("{CAT_ID}",liveTVCategory.getId()+"");
//        return "https://superteve.com/android/API/live_canales.php?cve=" + liveTVCategory.getId();
    }

    private String getSubCategoriesUrl(MainCategory mainCategory) {
        String tmpURL = "/categorias.php";
        try {
            tmpURL += "?t=" + URLEncoder.encode(mainCategory.getModelType(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return mainCategory.getModelType().equals(ModelTypes.MOVIES_YEAR) ? WebConfig.getCategoriesForYear : WebConfig.baseURL + tmpURL;
    }
    public List<? extends VideoStream> retrieveSearchMovies(MainCategory mainCategory, String pattern, int timeOut) {
        int type = -1;
        try {
            String modelType = mainCategory.getModelType();
            switch (modelType) {
                case ModelTypes.MOVIE_CATEGORIES:
                    type = 1;
                    break;
                case ModelTypes.SERIES_CATEGORIES:
                    type = 2;
                    break;
                case ModelTypes.SERIES_KIDS_CATEGORIES:
                    type = 3;
                    break;
                case ModelTypes.EVENTS_CATEGORIES:
                    type = 4;
                    break;
                case ModelTypes.ADULTS_CATEGORIES:
                    type = 5;
                    break;
            }
            return ParserJSonFile.getParsedMovies(mainCategory.getModelType(), "", NetManager.getInstance().makeSearchStringRequest(WebConfig.videoSearchURL.replace("{TYPE}", "" + type).replace("{PATTERN}", pattern), timeOut));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMoviesForCategoryUrl(String mainCategory, String movieCategory) {

        String tmpURL = "";
        try{
            String mainCategoryEncoded = URLEncoder.encode(movieCategory, "utf-8");
            switch(mainCategory) {//main category
                case ModelTypes.MOVIE_CATEGORIES:
                    tmpURL = "/movies.php?CATEGORY="+mainCategoryEncoded;
                    break;
                case ModelTypes.SERIES_CATEGORIES:
                    tmpURL = "/series.php?cat="+mainCategoryEncoded+"&tipo=2";
                    break;
                case ModelTypes.SERIES_KIDS_CATEGORIES:
                    tmpURL = "/series.php?cat="+mainCategoryEncoded+"&tipo=3";
                    break;
                case ModelTypes.EVENTS_CATEGORIES:
                    tmpURL = "/eventos.php?cat="+mainCategoryEncoded;
                    break;
                case ModelTypes.ADULTS_CATEGORIES:
                    tmpURL = "/adultos.php?cat="+mainCategoryEncoded;
                    break;
                case ModelTypes.LIVE_TV_CATEGORIES:
                    break;
                case ModelTypes.KARAOKE_CATEGORIES:
                    tmpURL = "/karaoke.php?cat="+mainCategoryEncoded;
                    break;
                case ModelTypes.MUSIC_CATEGORIES:
                    break;
                case ModelTypes.ENTERTAINMENT_CATEGORIES:
                    tmpURL = "/entertainment.php?cat="+mainCategoryEncoded;
                    break;
            }
            if(ModelTypes.TOP_MOVIES.equals(mainCategory) && movieCategory.contains("Movies"))
                tmpURL = "/getTopMovies.php?";
            else if(ModelTypes.TOP_MOVIES.equals(mainCategory) && movieCategory.contains("Series"))
                tmpURL = "/getTopSeries.php?";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        User user = LiveTvApplication.getUser();
        String password = user.getPassword();
        if(ModelTypes.TOP_MOVIES.equals(mainCategory))
            return WebConfig.baseURL + tmpURL + "s=" + password;
        else if(ModelTypes.MOVIES_YEAR.equals(mainCategory))
            return WebConfig.getMoviesYear.replace("{YEAR}", movieCategory);
        else
            return WebConfig.baseURL + tmpURL + "&s=" + password;

    }

    private String getMoviesForSerieUrl(Serie serie, int season) {
        String tmpURL = "/capitulos_temporada.php";
             tmpURL += "?cve="+ serie.getContentId() + "&temporada=Temporada%20"+ season;
         return WebConfig.baseURL + tmpURL;
    }
}