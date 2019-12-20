package com.uni.julio.supertv.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieCategory extends BaseCategory {


    private List<? extends VideoStream> movieList;
    private boolean isLoading = false;
    private boolean isLoaded = false;
    private boolean hasErrorLoading = false;

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


    public Set<VideoStream> searchForMovies(String searchString, boolean searchSerie) {
        Set<VideoStream> searchList = new HashSet<>();
        Set<VideoStream> tmpList;

        for(VideoStream videoStream : movieList) {
            if(videoStream instanceof Serie) {
                if(searchSerie) {

                    if(((Serie) videoStream).getSearchTitle()!= null && ((Serie) videoStream).getSearchTitle().toLowerCase().contains(searchString)) {
                        searchList.add(videoStream);
                    }
//                    tmpList = ((Serie) videoStream).searchForMovies(searchString);
//                    if (tmpList != null && tmpList.size() != 0) {
//                        searchList.addAll(tmpList);
//                    }
                }
                else {
                    tmpList = ((Serie) videoStream).searchForMovies(searchString);
                    if (tmpList != null && tmpList.size() != 0) {
                        searchList.addAll(tmpList);
                    }
                }
            }
            else {
                Movie mov = (Movie)videoStream;
                if(mov.contains(searchString)) {
                    searchList.add(videoStream);
                }
            }
        }
        return searchList;
    }


}
