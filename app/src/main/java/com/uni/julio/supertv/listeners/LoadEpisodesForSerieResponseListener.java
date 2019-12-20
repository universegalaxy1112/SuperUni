package com.uni.julio.supertv.listeners;


import com.uni.julio.supertv.model.Season;

public interface LoadEpisodesForSerieResponseListener extends BaseResponseListener {
    void onEpisodesForSerieCompleted(Season season);
}
