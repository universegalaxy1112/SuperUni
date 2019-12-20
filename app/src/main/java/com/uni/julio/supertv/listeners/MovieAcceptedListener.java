package com.uni.julio.supertv.listeners;


import com.uni.julio.supertv.model.VideoStream;

public interface MovieAcceptedListener {
    void onMovieAccepted(int selectedRow, VideoStream videoStream);
//    void onMovieAccepted(int[] positions, View view);
}
