package com.uni.julio.supertv.listeners;


public interface EpisodeLoadListener {
    void onLoaded();
    void onError();
    void showCustomProgress();
}