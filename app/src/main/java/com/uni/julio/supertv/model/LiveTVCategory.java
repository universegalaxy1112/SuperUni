package com.uni.julio.supertv.model;

import java.util.ArrayList;
import java.util.List;

public class LiveTVCategory extends BaseCategory {

//    private int id;           //cve
//    private String catName;   //nombre
    private int position;
    private int totalChannels;
    private List<LiveProgram> livePrograms;

    public LiveTVCategory() {
        livePrograms = new ArrayList<>();
    }

    public int getTotalChannels() {
        return totalChannels;
    }

    public void setTotalChannels(int totalChannels) {
        this.totalChannels = totalChannels;
    }

    public List<LiveProgram> getLivePrograms() {
        return livePrograms;
    }

    public void setLivePrograms(List<LiveProgram> livePrograms) {
        this.livePrograms = livePrograms;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

//    public Set<VideoStream> searchForMovies(String searchString) {
//        Set<VideoStream> searchList = new HashSet<>();
//        Set<VideoStream> tmpList;
//        for(MovieCategory movieCategory : movieCategories) {
//            tmpList = movieCategory.searchForMovies(searchString);
//            if(tmpList != null && tmpList.size() != 0) {
//                searchList.addAll(tmpList);
//            }
//        }
//        return searchList;
//    }

}
