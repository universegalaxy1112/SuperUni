package com.uni.julio.supertvplus.listeners;


public interface EpisodeLoadListener {
    void onLoaded();
    void onError();
    void showCustomProgress();
}