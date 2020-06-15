package com.uni.julio.supertvplus.listeners;


import com.uni.julio.supertvplus.model.Serie;

public interface LoadSeasonsForSerieResponseListener extends BaseResponseListener {
    void onSeasonsLoaded(Serie serie, int seasons);
    void onSeasonsLoadedError();
}
