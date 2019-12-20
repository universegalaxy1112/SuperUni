package com.uni.julio.supertv.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainCategory extends BaseCategory {

    //catName from BaseCategory
    //position from BaseCategory
    private String modelType;
    private int catImageId;
    private List<MovieCategory> movieCategories;

    public MainCategory() {
        movieCategories = new ArrayList<>();
    }

    public String getModelType() { return modelType; }
    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public int getCatImageId() { return catImageId; }
    public void setCatImageId(int catImageId) { this.catImageId = catImageId; }

    public void setMovieCategories(List<MovieCategory> movieCategories) { this.movieCategories = movieCategories; }
    public void addMovieCategory(int position, MovieCategory movieCategory) { movieCategories.set(position, movieCategory); }
    public List<MovieCategory> getMovieCategories() { return movieCategories; }
    public MovieCategory getMovieCategory(int position) { return movieCategories.get(position); }
    public Set<VideoStream> searchForMovies(String searchString, boolean searchSerie) {
        Set<VideoStream> searchList = new HashSet<>();
        Set<VideoStream> tmpList;
        for(MovieCategory movieCategory : movieCategories) {
            tmpList = movieCategory.searchForMovies(searchString, searchSerie);
            if(tmpList != null && tmpList.size() != 0) {
                searchList.addAll(tmpList);
            }
        }
        return searchList;
    }

}
