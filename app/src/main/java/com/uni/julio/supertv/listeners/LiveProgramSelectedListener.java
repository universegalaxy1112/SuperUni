package com.uni.julio.supertv.listeners;


import com.uni.julio.supertv.model.LiveProgram;

public interface LiveProgramSelectedListener {
    void onLiveProgramSelected(LiveProgram liveProgram, int programPosition);
}
