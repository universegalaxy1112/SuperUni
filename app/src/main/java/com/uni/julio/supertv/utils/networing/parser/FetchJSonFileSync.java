package com.uni.julio.supertv.utils.networing.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.networing.HttpRequest;
import com.uni.julio.supertv.utils.networing.NetManager;
import com.uni.julio.supertv.utils.networing.WebConfig;

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
//            param[0] is cat/modelType
            String subCatURL = getSubCategoriesUrl(mainCategory);
            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(subCatURL);

            //comment

                if(dataFromServer.contains("\"Settings\","))
                    dataFromServer = dataFromServer.replace("\"Settings\",","");

            ;//Log.d("liveTV","dataFromServer "+dataFromServer);
//                    retrieveDataFromServer(subCatURL);
            return ParserJSonFile.getParsedSubCategories(dataFromServer);
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
                    case ModelTypes.SERIES_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentSeries","");
                        break;
                    case ModelTypes.SERIES_KIDS_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentKidsSeries","");
                        break;
                }

                if(!TextUtils.isEmpty(recentMovies)) {
                    dataFromServer = "{\"Videos\": "+recentMovies + "}";
                }
                else {
                    return new ArrayList<>();
                }
            }
            else {
                String moviesForCatURL = getMoviesForCategoryUrl(mainCategory, movieCategory);
                if(movieCategory.contains("ettings")) {
                    dataFromServer = "{\"Videos\":[{\"Title\":\"Buscar\",\"SDPosterUrl\":\"lupita\",\"HDPosterUrl\":\"lupita\"}]}";
                }
                else {
                    dataFromServer = NetManager.getInstance().makeSyncStringRequest(moviesForCatURL, timeOut);
                }
//                    Log.d("liveTV", "dataFromServer for " + moviesForCatURL + " = " + dataFromServer);
            }
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
            String dataFromServer = HttpRequest.getInstance().performRequest(subCatURL);
//            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(subCatURL);
            ;//Log.d("liveTV","retrieveLiveTVCategories "+dataFromServer);
            return ParserJSonFile.getParsedLiveTVCategories(dataFromServer);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LiveProgram> retrieveProgramsForLiveTVCategory(LiveTVCategory liveTVCategory) {
        String theUser = DataManager.getInstance().getString("theUser","");
        User user = new Gson().fromJson(theUser, User.class);
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
        }
        return WebConfig.baseURL + tmpURL;
    }

    private String getMoviesForCategoryUrl(String mainCategory, String movieCategory) {

        String tmpURL = "";
        try{
            String mainCategoryEncoded = URLEncoder.encode(movieCategory, "utf-8");
            switch(mainCategory) {//main category
                case ModelTypes.MOVIE_CATEGORIES:
                    tmpURL = "/movie.php?cat="+mainCategoryEncoded;
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
        } catch (UnsupportedEncodingException e) { }
        String theUser = DataManager.getInstance().getString("theUser","");
        User user = new Gson().fromJson(theUser, User.class);
        String password = user.getPassword();
        return WebConfig.baseURL + tmpURL + "&s=" + password;
    }

    private String getMoviesForSerieUrl(Serie serie, int season) {
        String tmpURL = "/capitulos_temporada.php";
             tmpURL += "?cve="+ serie.getContentId() + "&temporada=Temporada%20"+ season;
         return WebConfig.baseURL + tmpURL;
    }
}