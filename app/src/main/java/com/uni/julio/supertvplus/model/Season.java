package com.uni.julio.supertvplus.model;

import java.util.ArrayList;
import java.util.List;

public class Season {

//    private int seasons;
    private List<? extends VideoStream> episodeList;

    public Season() {
        episodeList = new ArrayList<>();
    }
    private boolean isLoading = false;
    private int position = -1;
    private String name;

    public List<? extends VideoStream> getEpisodeList() { return episodeList; }
    public VideoStream getEpisode(int position) { return episodeList.get(position); }
    public void setEpisodeList(List<? extends VideoStream> list) { episodeList = list; }

    public boolean isLoading() { return isLoading; }
    public void setLoading(boolean loading) { isLoading = loading; }

    public int getPosition() { return position; }
    public void setPosition(int position) {
        this.position = position;
        setName("Temporada " + (position + 1));
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }




}
