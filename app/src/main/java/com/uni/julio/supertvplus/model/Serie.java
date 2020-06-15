package com.uni.julio.supertvplus.model;

import java.util.ArrayList;
import java.util.List;

public class Serie extends Movie implements Cloneable {

    private List<Season> seasons;
    private String seasonCountText;


    public Serie() {
        seasons = new ArrayList<>();
        setPosition(-1);//at start set position to -1
    }
    public Serie clone()  {
        try{
            return (Serie) super.clone();
        }catch (CloneNotSupportedException e){
            return null;
        }

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

}
