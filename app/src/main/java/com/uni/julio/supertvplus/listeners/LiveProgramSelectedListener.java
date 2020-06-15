package com.uni.julio.supertvplus.listeners;


import com.uni.julio.supertvplus.model.LiveProgram;

public interface LiveProgramSelectedListener {
    void onLiveProgramSelected(LiveProgram liveProgram, int programPosition);
}
