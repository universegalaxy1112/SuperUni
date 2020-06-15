package com.uni.julio.supertvplus.utils.networing.parser;

import android.text.TextUtils;
import android.util.Log;

import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.model.Episode;
import com.uni.julio.supertvplus.model.LiveProgram;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.Setting;
import com.uni.julio.supertvplus.model.VideoStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class ParserJSonFile {
    final static String LOG_TAG = "ParserJsonFile";

//    private static Set<String> seenMovies;
//    private static Set<String> favoriteMovies;


    public static List<MovieCategory> getParsedSubCategories(String data, int mainCategoryId) throws JSONException {

        List<MovieCategory> dataArray = new ArrayList<>();

        JSONArray videoArray = new JSONArray(data);
        MovieCategory movieCat;
        for (int i = 0; i < videoArray.length(); i++) {
            if(!videoArray.getString(i).toLowerCase().contains("4_k")) {
                movieCat = new MovieCategory();
                movieCat.setCatName(videoArray.getString(i));
                if ((mainCategoryId == 7 || mainCategoryId == 8 || mainCategoryId == 4 || mainCategoryId == 9)) {
                    movieCat.setId(i);
                } else {
                    movieCat.setId(i + 1);
                }

                dataArray.add(movieCat);
            }
        }
        return dataArray;
    }

    public static List<LiveTVCategory> getParsedLiveTVCategories(String data) throws JSONException {

        if(!TextUtils.isEmpty(data)) {
            List<LiveTVCategory> dataArray = new ArrayList<>();

            JSONArray videoArray = new JSONArray(data);
            LiveTVCategory liveTvCat;
            JSONObject tmpObj;
            for (int i = 0; i < videoArray.length(); i++) {
                liveTvCat = new LiveTVCategory();

                tmpObj = videoArray.getJSONObject(i);
//            movieCat.setId(i);
                liveTvCat.setId(Integer.valueOf(tmpObj.getString("cve")));
                liveTvCat.setCatName(tmpObj.getString("nombre"));
                liveTvCat.setTotalChannels(Integer.valueOf(tmpObj.getString("total_canales")));
                liveTvCat.setPosition(i);
                dataArray.add(liveTvCat);
            }
            return dataArray;
        }
        return null;
    }

    public static List<LiveProgram> getParsedProgramsForLiveTVCategory(LiveTVCategory liveTVCategory, String data) throws JSONException {

//        seenMovies = DataManager.getInstance().getStringSet("seenMovies");
//        favoriteMovies = DataManager.getInstance().getStringSet("favoriteMovies");

        List<LiveProgram> dataArray = new ArrayList<>();

        JSONArray videoArray = new JSONArray(data);
        LiveProgram liveProgram;
        for (int i = 0; i < videoArray.length(); i++) {
            liveProgram = new LiveProgram();
            fillObject(liveProgram,videoArray.getJSONObject(i));
            liveProgram.setPosition(i);
            dataArray.add(liveProgram);
        }
        return dataArray;
    }

    public static List<? extends VideoStream> getParsedMoviesForSerie(Serie serie, String data) throws JSONException {

//        seenMovies = DataManager.getInstance().getStringSet("seenMovies");
//        favoriteMovies = DataManager.getInstance().getStringSet("favoriteMovies");

        String JSON_ARRAY_VAR = "";
        List<VideoStream> dataArray = new ArrayList<>();

        JSONObject videosJson = new JSONObject(data);
        JSONArray videoArray = null;
        JSON_ARRAY_VAR = "Capitulos";
        videoArray = videosJson.getJSONArray(JSON_ARRAY_VAR);

        dataArray = new ArrayList<>();

        VideoStream movie;
        for (int i = 0; i < videoArray.length(); i++) {

            movie = new Episode();
            movie.setPosition(i);
            fillObject(movie,videoArray.getJSONObject(i));
            dataArray.add(movie);
        }
        return dataArray;
    }

    public static List<? extends VideoStream> getParsedMovies(String mainCategory, String movieCategory, String data) throws JSONException,NullPointerException {

        String JSON_ARRAY_VAR = "";
        List<VideoStream> dataArray = new ArrayList<>();
            JSONObject videosJson = new JSONObject(data);
            JSONArray videoArray = null;
            JSON_ARRAY_VAR = "Videos";
            videoArray = videosJson.getJSONArray(JSON_ARRAY_VAR);

            dataArray = new ArrayList<>();
            VideoStream movie = null;
            for (int i = 0; i < videoArray.length(); i++) {

                switch (mainCategory) {
                    case ModelTypes.MOVIE_CATEGORIES:
                    case ModelTypes.ENTERTAINMENT_CATEGORIES:
                    case ModelTypes.EVENTS_CATEGORIES:
                    case ModelTypes.ADULTS_CATEGORIES:
                        movie = new Movie();
                        break;
                    case ModelTypes.SERIES_CATEGORIES:
                    case ModelTypes.SERIES_KIDS_CATEGORIES:
                    case ModelTypes.KARAOKE_CATEGORIES:
                        movie = new Serie();
                        break;
                    default:
                        movie = new Movie();
                        break;
                    //case ModelTypes.LIVE_TV_CATEGORIES:
                }
                if(movieCategory.contains("Movies"))
                    movie = new Movie();
                else if(movieCategory.contains("Series"))
                    movie = new Serie();
                movie.setPosition(i);
                fillObject(movie, videoArray.getJSONObject(i));
                dataArray.add(movie);
            }


        return dataArray;

    }


    private static Pattern pattern;
    static {
        pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    }

    public static String removeSpecialChars(String s) {
        String nfdNormalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static void fillObject(VideoStream obj, JSONObject json_obj) {
        try {

            if(json_obj.has("ContentId"))
                obj.setContentId(Integer.parseInt(json_obj.getString("ContentId")));
            if(json_obj.has("ContentType"))
                obj.setContentType(json_obj.getString("ContentType"));
            if(json_obj.has("Title")) {
                obj.setTitle(json_obj.getString("Title"));
                obj.setSearchTitle(removeSpecialChars(json_obj.getString("Title")));
            }
            if(json_obj.has("StreamUrl"))
                obj.setStreamUrl(json_obj.getString("StreamUrl"));
            if(json_obj.has("StreamUrl2"))
                obj.setSDUrl(json_obj.getString("StreamUrl2"));
            if(json_obj.has("Trailerurl"))
                obj.setTrailerUrl(json_obj.getString("Trailerurl"));
            if(json_obj.has("tipo"))
                obj.setCategoryType(json_obj.getInt("tipo"));

            if(VideoStreamManager.getInstance().getSeenMovies().contains(String.valueOf(obj.getContentId())))
                obj.setSeen(true);
            if(VideoStreamManager.getInstance().getFavoriteMovies().contains(String.valueOf(obj.getContentId())))
                obj.setFavorite(true);

            //Series must be first because (for Serie) obj is also an instance of Movie (because it's extending it)

            if(obj instanceof  LiveProgram) {
                LiveProgram liveProgram = (LiveProgram) obj;

                if (json_obj.has("cve"))
                    liveProgram.setContentId(Integer.parseInt(json_obj.getString("cve")));
                if (json_obj.has("nombre"))
                    liveProgram.setTitle((json_obj.getString("nombre")));
                if (json_obj.has("icono"))
                    liveProgram.setIconUrl(json_obj.getString("icono"));
                if (json_obj.has("stream"))
                    liveProgram.setStreamUrl(json_obj.getString("stream"));
                if (json_obj.has("epg_ahora"))
                    liveProgram.setEpg_ahora(json_obj.getString("epg_ahora"));
                if (json_obj.has("epg_despues"))
                    liveProgram.setEpg_despues(json_obj.getString("epg_despues"));
                if (json_obj.has("description") && !json_obj.getString("description").equals(""))
                    liveProgram.setDescription(json_obj.getString("description"));
                if (json_obj.has("title") && !json_obj.getString("title").equals(""))
                    liveProgram.setSub_title(json_obj.getString("title"));
            }
            else  if(obj instanceof  Serie) {
                Serie movie = (Serie) obj;

                if(json_obj.has("temporadas"))
                    movie.setSeasonCountText(json_obj.getString("temporadas"));
                else
                    if(json_obj.has("seasonCountText"))
                        movie.setSeasonCountText(json_obj.getString("seasonCountText"));

                if (json_obj.has("Description"))
                    movie.setDescription(json_obj.getString("Description"));
                if (json_obj.has("Watched"))
                    movie.setWatched(json_obj.getBoolean("Watched"));
                if (json_obj.has("Length")) {
                    if(TextUtils.isEmpty(json_obj.getString("Length"))||json_obj.getString("Length").equals("null")) {
                        movie.setLength(0);
                    }
                    else {
                        movie.setLength(Integer.parseInt(json_obj.getString("Length")));
                    }
                }
                if (json_obj.has("Rating"))
                    movie.setRating((json_obj.getString("Rating")));
                if (json_obj.has("StarRating"))
                    movie.setStarRating(json_obj.getInt("StarRating"));
                if (json_obj.has("Categories"))
                    movie.setCategories(json_obj.getString("Categories"));
                if (json_obj.has("Director"))
                    movie.setDirector(json_obj.getString("Director"));
                if (json_obj.has("Actors")) {
                    movie.setActors(json_obj.getString("Actors"));
                    movie.setSearchActors(removeSpecialChars(json_obj.getString("Actors")));
                }
                if (json_obj.has("ReleaseDate"))
                    movie.setReleaseDate(json_obj.getString("ReleaseDate"));
                if (json_obj.has("StreamFormat"))
                    movie.setStreamFormat(json_obj.getString("StreamFormat"));
                if (json_obj.has("StreamBitrates"))
                    movie.setStreamBitrates(Integer.parseInt(json_obj.getString("StreamBitrates")));
                if (json_obj.has("SDBifUrl"))
                    movie.setSDBifUrl(json_obj.getString("SDBifUrl"));
                if (json_obj.has("HDBifUrl"))
                    movie.setHDBifUrl(json_obj.getString("HDBifUrl"));
                if (json_obj.has("SDPosterUrl"))
                    movie.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                if (json_obj.has("HDPosterUrl"))
                    movie.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                if (json_obj.has("StreamQualities"))
                    movie.setStreamQualities(json_obj.getString("StreamQualities"));

                if (json_obj.has("HDBranded"))
                    movie.setHDBranded((json_obj.getBoolean("HDBranded")));
                if (json_obj.has("isHD"))
                    movie.setHD(json_obj.getBoolean("isHD"));
                if (json_obj.has("fullHD"))
                    movie.setFullHD(json_obj.getBoolean("fullHD"));
                if (json_obj.has("HDFondoUrl")) {
                    movie.setHDFondoUrl(json_obj.getString("HDFondoUrl"));
                }

                if(json_obj.has("Title")) {
                    String title;
                    if(movie.getSeasonCountText() != null)
                        title = json_obj.getString("Title").replace(movie.getSeasonCountText() , "");
                    else
                        title = json_obj.getString("Title");
                    obj.setTitle(title);
                    obj.setSearchTitle(title);
                }
            }
            else if(obj instanceof  Episode) {
                Episode movie = (Episode) obj;

//                if(json_obj.has("temporadas"))
//                    movie.setSeasons(json_obj.getString("temporadas"));
                if(json_obj.has("SubtitleUrl"))
                    movie.setSubtitleUrl(json_obj.getString("SubtitleUrl"));
                if(json_obj.has("Description"))
                    movie.setDescription(json_obj.getString("Description"));
                if(json_obj.has("Watched"))
                    movie.setWatched(json_obj.getBoolean("Watched"));
                if(json_obj.has("Length")) {
                    if(TextUtils.isEmpty(json_obj.getString("Length"))) {
                        movie.setLength(0);
                    }
                    else {
                        movie.setLength(Integer.parseInt(json_obj.getString("Length")));
                    }
                }
                if(json_obj.has("Rating"))
                    movie.setRating((json_obj.getString("Rating")));
                if(json_obj.has("StarRating"))
                    movie.setStarRating(json_obj.getInt("StarRating"));
                if(json_obj.has("Categories"))
                    movie.setCategories(json_obj.getString("Categories"));
                if (json_obj.has("Director"))
                    movie.setDirector(json_obj.getString("Director"));
                if(json_obj.has("Actors")) {
                    movie.setActors(json_obj.getString("Actors"));
                    movie.setSearchActors(removeSpecialChars(json_obj.getString("Actors")));
                }
                if(json_obj.has("ReleaseDate"))
                    movie.setReleaseDate(json_obj.getString("ReleaseDate"));
                if(json_obj.has("StreamFormat"))
                    movie.setStreamFormat(json_obj.getString("StreamFormat"));
                if(json_obj.has("StreamBitrates"))
                    movie.setStreamBitrates(Integer.parseInt(json_obj.getString("StreamBitrates")));
                if(json_obj.has("SDBifUrl"))
                    movie.setSDBifUrl(json_obj.getString("SDBifUrl"));
                if(json_obj.has("HDBifUrl"))
                    movie.setHDBifUrl(json_obj.getString("HDBifUrl"));
                if(json_obj.has("SDPosterUrl"))
                    movie.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                if(json_obj.has("HDPosterUrl"))
                    movie.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                if(json_obj.has("StreamQualities"))
                    movie.setStreamQualities(json_obj.getString("StreamQualities"));

                if(json_obj.has("HDBranded"))
                    movie.setHDBranded((json_obj.getBoolean("HDBranded")));
                if(json_obj.has("isHD"))
                    movie.setHD(json_obj.getBoolean("isHD"));
                if(json_obj.has("fullHD"))
                    movie.setFullHD(json_obj.getBoolean("fullHD"));
                if (json_obj.has("HDFondoUrl")) {
                    movie.setHDFondoUrl(json_obj.getString("HDFondoUrl"));
                }
            } else if(obj instanceof  Movie) {
                Movie movie = (Movie) obj;

                if(json_obj.has("SubtitleUrl"))
                    movie.setSubtitleUrl(json_obj.getString("SubtitleUrl"));
                if (json_obj.has("Description"))
                    movie.setDescription(json_obj.getString("Description"));
                if (json_obj.has("Watched"))
                    movie.setWatched(json_obj.getBoolean("Watched"));
                if (json_obj.has("Length")) {
                    if(TextUtils.isEmpty(json_obj.getString("Length"))) {
                        movie.setLength(0);
                    }
                    else {
                        movie.setLength(Integer.parseInt(json_obj.getString("Length")));
                    }
                }
                if (json_obj.has("Rating"))
                    movie.setRating((json_obj.getString("Rating")));
                if (json_obj.has("StarRating"))
                    movie.setStarRating(json_obj.getInt("StarRating"));
                if (json_obj.has("Categories"))
                    movie.setCategories(json_obj.getString("Categories"));
                if (json_obj.has("Director"))
                    movie.setDirector(json_obj.getString("Director"));
                if (json_obj.has("Actors")) {
                    movie.setActors(json_obj.getString("Actors"));
                    movie.setSearchActors(removeSpecialChars(json_obj.getString("Actors")));
                }
                if (json_obj.has("ReleaseDate"))
                    movie.setReleaseDate(json_obj.getString("ReleaseDate"));
                if (json_obj.has("StreamFormat"))
                    movie.setStreamFormat(json_obj.getString("StreamFormat"));
                if (json_obj.has("StreamBitrates"))
                    movie.setStreamBitrates(Integer.parseInt(json_obj.getString("StreamBitrates")));
                if (json_obj.has("SDBifUrl"))
                    movie.setSDBifUrl(json_obj.getString("SDBifUrl"));
                if (json_obj.has("HDBifUrl"))
                    movie.setHDBifUrl(json_obj.getString("HDBifUrl"));
                if (json_obj.has("SDPosterUrl"))
                    movie.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                if (json_obj.has("HDPosterUrl"))
                    movie.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                if (json_obj.has("StreamQualities"))
                    movie.setStreamQualities(json_obj.getString("StreamQualities"));

                if (json_obj.has("HDBranded"))
                    movie.setHDBranded((json_obj.getBoolean("HDBranded")));
                if (json_obj.has("isHD"))
                    movie.setHD(json_obj.getBoolean("isHD"));
                if (json_obj.has("fullHD"))
                    movie.setFullHD(json_obj.getBoolean("fullHD"));
                if (json_obj.has("HDFondoUrl")) {
                    movie.setHDFondoUrl(json_obj.getString("HDFondoUrl"));
                }
            } else if (obj instanceof Setting){
                Setting setting = (Setting) obj;
                if(json_obj.has("SDPosterUrl"))
                    setting.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                if(json_obj.has("HDPosterUrl"))
                    setting.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
            }

        } catch (JSONException e) {

            Log.i(LOG_TAG, e.getMessage());
        }

    }

}
