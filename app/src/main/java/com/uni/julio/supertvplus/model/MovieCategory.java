package com.uni.julio.supertvplus.model;

import java.util.ArrayList;
import java.util.List;

public class MovieCategory extends BaseCategory {


    private List<? extends VideoStream> movieList;
    private boolean isLoading = false;
    private boolean isLoaded = false;
    private boolean hasErrorLoading = false;
    private boolean categoryDisplayed = false;

    public MovieCategory() {
        movieList = new ArrayList<>();
    }

    public List<? extends VideoStream> getMovieList() { return movieList; }
    public VideoStream getMovie(int position) { return movieList.get(position); }
    public void setMovieList(List<? extends VideoStream> list) { movieList = list; }

    public boolean isLoading() { return isLoading; }
    public void setLoading(boolean loading) { isLoading = loading; }

    public boolean isLoaded() { return isLoaded; }
    public void setLoaded(boolean loaded) { isLoaded = loaded; }

    public boolean hasErrorLoading() { return hasErrorLoading; }
    public void setErrorLoading(boolean errorLoading) { hasErrorLoading = errorLoading; }
    public boolean isCategoryDisplayed() {
        return categoryDisplayed;
    }
    public void setCategoryDisplayed(boolean categoryDisplayed) {
        this.categoryDisplayed = categoryDisplayed;
    }
}
