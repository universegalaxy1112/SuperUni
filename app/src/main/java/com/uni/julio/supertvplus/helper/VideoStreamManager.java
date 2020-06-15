package com.uni.julio.supertvplus.helper;

import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.model.LiveProgram;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.utils.DataManager;
import com.uni.julio.supertvplus.utils.Device;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        mainCategoriesList.add(createMainCategory("TV", R.drawable.tv, ModelTypes.LIVE_TV_CATEGORIES, 5));
        mainCategoriesList.add(createMainCategory("Top", R.drawable.top, ModelTypes.TOP_MOVIES, 8));
        mainCategoriesList.add(createMainCategory("Peliculas", R.drawable.movies, ModelTypes.MOVIE_CATEGORIES, 0));
        mainCategoriesList.add(createMainCategory("Year", R.drawable.moviesyear, ModelTypes.MOVIES_YEAR, 9));
        mainCategoriesList.add(createMainCategory("Series", R.drawable.series, ModelTypes.SERIES_CATEGORIES, 1));
        mainCategoriesList.add(createMainCategory("Infantiles", R.drawable.kids, ModelTypes.SERIES_KIDS_CATEGORIES, 2));
        mainCategoriesList.add(createMainCategory("Eventos", R.drawable.eventos, ModelTypes.EVENTS_CATEGORIES, 4));
        mainCategoriesList.add(createMainCategory("Entretenimiento", R.drawable.entertainment, ModelTypes.ENTERTAINMENT_CATEGORIES, 3));
        mainCategoriesList.add(createMainCategory("Karaoke", R.drawable.karaoke, ModelTypes.KARAOKE_CATEGORIES, 6));
        if(LiveTvApplication.getUser() != null && LiveTvApplication.getUser().getAdultos() == 0)
        mainCategoriesList.add(createMainCategory("Adultos", R.drawable.adults, ModelTypes.ADULTS_CATEGORIES, 7));
        if(Device.canTreatAsBox()) {
            mainCategoriesList.add(createMainCategory("Mi cuenta", R.drawable.setting, ModelTypes.SETTINGS, 10));
        }
    }

    public MainCategory createMainCategory(String name, int imageId, String modelType, int id) {
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
        MainCategory mainCategory = null;
        try{

            for(MainCategory item : mainCategoriesList ){
                if(item.getId() == id){
                    mainCategory = item;
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        if(mainCategory == null) {
            VideoStreamManager.getInstance().FillMainCategories();
            for(MainCategory item : mainCategoriesList ){
                if(item.getId() == id) {
                    mainCategory = item;
                    break;
                }
            }
        }
        return mainCategory;
    }


    public List<LiveProgram> getAllLivePrograms() {
        List<LiveProgram> allPrograms = new ArrayList<>();
        for(LiveTVCategory livePrograms : liveTVCategoriesList) {
            if(livePrograms != null)
                allPrograms.addAll(Objects.requireNonNull(livePrograms).getLivePrograms());
        }

        return allPrograms;

    }

    public List<LiveTVCategory> getLiveTVCategoriesList() { return liveTVCategoriesList; }
    public LiveTVCategory getLiveTVCategory(int id) {
        if(liveTVCategoriesList.size() > id)
        return liveTVCategoriesList.get(id);
        else return null;

    }
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
