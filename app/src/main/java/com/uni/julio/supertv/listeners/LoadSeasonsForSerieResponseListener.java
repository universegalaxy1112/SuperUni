package com.uni.julio.supertv.listeners;


import com.uni.julio.supertv.model.Serie;

public interface LoadSeasonsForSerieResponseListener extends BaseResponseListener {
    void onSeasonsLoaded(Serie serie, int seasons);
    void onSeasonsLoadedError();
}
