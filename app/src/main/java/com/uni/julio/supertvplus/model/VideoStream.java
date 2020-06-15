package com.uni.julio.supertvplus.model;

//base class for other models
public abstract class VideoStream  {//implements Cloneable {
    private int ContentId = 0;
    private String ContentType = "";
    private String Title = "";
    private String StreamUrl = "";
    private String StreamUrl2 = "";
    private String Trailerurl = "";
    private int position;
    private boolean HDBranded;
    private boolean seen;
    private boolean favorite;
    private int tipo = -1;

    private String searchTitle;

    public String getSearchTitle() {
        return searchTitle;
    }
    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public int getContentId() {
        return ContentId;
    }
    public void setContentId(int contentId) {
        this.ContentId = contentId;
    }

    public String getContentType() {
        return ContentType;
    }
    public void setContentType(String contentType) {
        this.ContentType = contentType;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        this.Title = title;
    }

    public String getStreamUrl() {
        return StreamUrl;
    }
    public String getSDUrl() {
        return StreamUrl2;
    }
    public String getTrailerUrl() {
        return Trailerurl;
    }


    public void setStreamUrl(String streamUrl) {
        StreamUrl = streamUrl;
    }
    public void setSDUrl(String sdUrl) {
        StreamUrl2 = sdUrl;
    }
    public void setTrailerUrl(String trailerUrl) {
        Trailerurl = trailerUrl;
    }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public boolean contains(String searchString) {
//Log.d("liveTV", "Search for "+searchString+ " should add? "+getTitle().toLowerCase().contains(searchString.toLowerCase()));
        return getTitle().toLowerCase().contains(searchString.toLowerCase());
    }

    public boolean isHDBranded() {
        return HDBranded;
    }
    public void setHDBranded(boolean HDBranded) {
        this.HDBranded = HDBranded;
    }

    public boolean isSeen() {
        return seen;
    }
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoStream that = (VideoStream) o;
        return this.getContentId() == that.getContentId();
    }
    public int getCategoryType() {
        return tipo;
    }

    public void setCategoryType(int categoryType) {
        this.tipo = categoryType;
    }


    @Override
    public int hashCode() {
        return this.ContentId;
    }
}
