package com.uni.julio.supertvplus.model;

import android.text.TextUtils;

public class Movie extends VideoStream {

    private String Description;
    private boolean Watched;
    private int Length;
    private String Rating;
    private int StarRating;
    private String Categories;
    private String Director;
    private String Actors;
    private String ReleaseDate;
    private String StreamFormat;
    private int StreamBitrates;
    private String SDBifUrl;
    private String HDBifUrl;
    private String HDFondoUrl;

    private String SDPosterUrl;
    private String HDPosterUrl;
    private String StreamQualities;
//    private boolean HDBranded;
    private boolean isHD;
    private boolean fullHD;
    private String SubtitleUrl;
    private String searchActors;
    private transient int movieCategoryIdOwner = 0;
    public int getMovieCategoryIdOwner() {
        return movieCategoryIdOwner;
    }

    public void setMovieCategoryIdOwner(int movieCategoryIdOwner) {
        this.movieCategoryIdOwner = movieCategoryIdOwner;
    }


    public String getSearchActors() {
        return searchActors;
    }

    public void setSearchActors(String searchActors) {
        this.searchActors = searchActors;
    }
    public void setHDFondoUrl(String HDFondoUrl2) {
    this.HDFondoUrl = HDFondoUrl2;
}
    public String getHDFondoUrl() {
        return this.HDFondoUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public boolean isWatched() {
        return Watched;
    }

    public void setWatched(boolean watched) {
        Watched = watched;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        this.Length = length;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        this.Rating = rating;
    }

    public int getStarRating() {
        return StarRating;
    }

    public void setStarRating(int satrtRating) {
        this.StarRating = satrtRating;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        this.Categories = categories;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        this.Director = director;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        this.Actors = actors;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getStreamFormat() {
        return StreamFormat;
    }

    public void setStreamFormat(String streamFormat) {
        this.StreamFormat = streamFormat;
    }

    public int getStreamBitrates() {
        return StreamBitrates;
    }

    public void setStreamBitrates(int streamBitrates) {
        this.StreamBitrates = streamBitrates;
    }

    public String getSDBifUrl() {
        return SDBifUrl;
    }

    public void setSDBifUrl(String SDBifUrl) {
        this.SDBifUrl = SDBifUrl;
    }

    public String getHDBifUrl() {
        return HDBifUrl;
    }

    public void setHDBifUrl(String HDBifUrl) {
        this.HDBifUrl = HDBifUrl;
    }

    public String getSDPosterUrl() {
        return SDPosterUrl;
    }

    public void setSDPosterUrl(String SDPosterUrl) {
        this.SDPosterUrl = SDPosterUrl;
    }

    public String getHDPosterUrl() {
        return HDPosterUrl;
    }

    public void setHDPosterUrl(String HDPosterUrl) {
        this.HDPosterUrl = HDPosterUrl;
    }

    public String getStreamQualities() {
        return StreamQualities;
    }

    public void setStreamQualities(String streamQualities) {
        StreamQualities = streamQualities;
    }

//    public boolean isHDBranded() {
//        return HDBranded;
//    }
//
//    public void setHDBranded(boolean HDBranded) {
//        this.HDBranded = HDBranded;
//    }

    public boolean isHD() {
        return isHD;
    }

    public void setHD(boolean HD) {
        isHD = HD;
    }

    public boolean isFullHD() {
        return fullHD;
    }

    public void setFullHD(boolean fullHD) {
        this.fullHD = fullHD;
    }

    public String getSubtitleUrl() {
        return SubtitleUrl;
    }

    public void setSubtitleUrl(String subtitleUrl) {
        SubtitleUrl = subtitleUrl;
    }


    @Override
    public boolean contains(String searchString) {
//        Log.d("liveTV", "aaSearch for " + searchString);
        if(TextUtils.isEmpty(getActors())) {
            return getSearchTitle()!= null && getSearchTitle().toLowerCase().contains(searchString.toLowerCase());
        }
        else {
            return (getSearchTitle()!= null && getSearchTitle().toLowerCase().contains(searchString.toLowerCase()))
                    || (getSearchActors()!= null &&  getSearchActors().toLowerCase().contains(searchString.toLowerCase()));
        }
    }

}
