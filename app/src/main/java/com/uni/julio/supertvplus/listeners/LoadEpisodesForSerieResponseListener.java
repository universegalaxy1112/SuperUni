package com.uni.julio.supertvplus.listeners;


import com.uni.julio.supertvplus.model.Season;

public interface LoadEpisodesForSerieResponseListener extends BaseResponseListener {
    void onEpisodesForSerieCompleted(Season season);
}
