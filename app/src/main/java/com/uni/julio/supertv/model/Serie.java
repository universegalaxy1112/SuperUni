package com.uni.julio.supertv.model;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Serie extends Movie implements Cloneable {

    private List<Season> seasons;
    private String seasonCountText;

    private transient int movieCategoryIdOwner = -1;
     public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public Serie() {
        seasons = new ArrayList<>();
        setPosition(-1);//at start set position to -1
    }

    public void setSeasons(List<Season> seasons) { this.seasons = seasons; }

    public List<Season> getSeasons() { return seasons; }
    public int getSeasonCount() { return seasons.size(); }
    public void addSeason(int position, Season season) {
//        if(seasons.get(position) != null) {
//            seasons.set(position, season);
//        }
//        else {
            season.setPosition(position);
            seasons.add(season);
//        }
    }
    public void setSeason(int position, Season season) {
        seasons.set(position, season);
    }
    public Season getSeason(int season) { return seasons.get(season); }
    public String getSeasonCountText() { return seasonCountText; }

    public void setSeasonCountText(String seasonCountText) { this.seasonCountText = seasonCountText; }
    //    private int position = -1;

    public int getMovieCategoryIdOwner() {
        return movieCategoryIdOwner;
    }

    public void setMovieCategoryIdOwner(int movieCategoryIdOwner) {
        this.movieCategoryIdOwner = movieCategoryIdOwner;
    }

//    public int getPosition() { return position; }
//    public void setPosition(int position) { this.position = position; }

    public Set<VideoStream> searchForMovies(String searchString) {
        Set<VideoStream> searchList = new HashSet<>();
        Set<VideoStream> tmpList;

        for(Season season : seasons) {
            tmpList = season.searchForMovies(searchString);
            if(tmpList != null && tmpList.size() != 0) {
                searchList.addAll(tmpList);
            }
        }
        return searchList;
    }
}
