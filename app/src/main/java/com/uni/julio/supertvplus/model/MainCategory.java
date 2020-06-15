package com.uni.julio.supertvplus.model;

import java.util.ArrayList;
import java.util.List;

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

}
