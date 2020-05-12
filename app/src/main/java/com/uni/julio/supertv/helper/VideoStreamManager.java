package com.uni.julio.supertv.helper;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.networing.NetManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

public class VideoStreamManager {

    private static VideoStreamManager m_sInstance;
    private static List<MainCategory> mainCategoriesList;
    private static List<LiveTVCategory> liveTVCategoriesList;

    private static Set<String> localFavorites;
    private static Set<String> localSeen;
    private VideoStreamManager() {
        mainCategoriesList = new ArrayList<>();
        liveTVCategoriesList = new ArrayList<>();
        localFavorites = new HashSet<>();
        localFavorites.addAll(DataManager.getInstance().getStringSet("favoriteMoviesTotal"));
        localSeen = new HashSet<>();
        localSeen.addAll(DataManager.getInstance().getStringSet("seenMovies"));
    }

    public static VideoStreamManager getInstance() {
        if(m_sInstance == null) {
            m_sInstance = new VideoStreamManager();
        }
        return m_sInstance;
    }

    public void FillMainCategories() {
        mainCategoriesList.clear();
        mainCategoriesList.add(createMainCategory("Peliculas", R.drawable.movies, ModelTypes.MOVIE_CATEGORIES, 0));
        mainCategoriesList.add(createMainCategory("Series", R.drawable.series, ModelTypes.SERIES_CATEGORIES, 1));
        mainCategoriesList.add(createMainCategory("Infantiles", R.drawable.kids, ModelTypes.SERIES_KIDS_CATEGORIES, 2));
        mainCategoriesList.add(createMainCategory("Entretenimiento", R.drawable.entertainment, ModelTypes.ENTERTAINMENT_CATEGORIES, 3));
        mainCategoriesList.add(createMainCategory("Eventos", R.drawable.eventos, ModelTypes.EVENTS_CATEGORIES, 4));
        mainCategoriesList.add(createMainCategory("TV", R.drawable.tv, ModelTypes.LIVE_TV_CATEGORIES, 5));
        mainCategoriesList.add(createMainCategory("Karaoke", R.drawable.karaoke, ModelTypes.KARAOKE_CATEGORIES, 6));
        mainCategoriesList.add(createMainCategory("Adultos", R.drawable.adults, ModelTypes.ADULTS_CATEGORIES, 7));
        mainCategoriesList.add(createMainCategory("Top", R.drawable.top, ModelTypes.TOP_MOVIES, 8));
        mainCategoriesList.add(createMainCategory("Year", R.drawable.moviesyear, ModelTypes.MOVIES_YEAR, 9));
        if(Device.canTreatAsBox()) {
            mainCategoriesList.add(createMainCategory("Mi cuenta", R.drawable.setting, ModelTypes.SETTINGS, 10));
        }
    }

    private MainCategory createMainCategory(String name, int imageId, String modelType, int id) {
        MainCategory cat = new MainCategory();
        cat.setCatName(name);
        cat.setCatImageId(imageId);
        cat.setModelType(modelType);
        cat.setId(id);
        return cat;
    }

    public List<MainCategory> getMainCategoriesList() {
        return mainCategoriesList;
    }
    public MainCategory getMainCategory(int id) {
        try{
            return mainCategoriesList.get(id);
        }catch (Exception e){
            VideoStreamManager.getInstance().FillMainCategories();
            return mainCategoriesList.get(id);
        }
    }

/*    public List<VideoStream> searchForMovies(MainCategory mainCategory, String searchString,boolean searchForSeries ) {
        Set<VideoStream> searchList = new HashSet<>();
        Set<VideoStream> tmpList;

        tmpList = mainCategory.searchForMovies(searchString, searchForSeries);
        if(tmpList != null && tmpList.size() != 0) {
            searchList.addAll(tmpList);
        }

//        HashSet<Integer> seen = new HashSet<>();
//        searchList.removeIf(e->!seen.add(e.getContentId()));
        List<VideoStream> finalList = new ArrayList<>();
        finalList.addAll(searchList);
        return finalList;
    }*/


    public List<LiveProgram> getAllLivePrograms() {
        List<LiveProgram> allPrograms = new ArrayList<>();
        for(LiveTVCategory livePrograms : liveTVCategoriesList) {
            allPrograms.addAll(livePrograms.getLivePrograms());
        }
        return allPrograms;
    }

    public List<LiveTVCategory> getLiveTVCategoriesList() { return liveTVCategoriesList; }
    public LiveTVCategory getLiveTVCategory(int id) { return liveTVCategoriesList.get(id); }
    public void resetLiveTVCategory(int count) {
        liveTVCategoriesList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            liveTVCategoriesList.add(null);//hack to have a correct size
        }
    }
    public void setLiveTVCategory(LiveTVCategory liveTVCategory) {
        liveTVCategoriesList.set(liveTVCategory.getPosition(), liveTVCategory);
//        liveTVCategoriesList.add(liveTVCategory);
    }

    public Set<String> getSeenMovies() {
        return localSeen;
    }
    public boolean isLocalSeen(String contentId) {
        return localSeen.contains(contentId);
    }
    public void setLocalSeen(String contentId) {
        localSeen.add(contentId);
    }

    public Set<String> getFavoriteMovies() {
        return localFavorites;
    }
    public boolean isLocalFavorite(String contentId) {
        return localFavorites.contains(contentId);
    }
    public void setLocalFavorite(String contentId) {
        localFavorites.add(contentId);
    }
    public void removeLocalFavorite(String contentId) {
        localFavorites.remove(contentId);
    }

}
