package com.uni.julio.supertvplus.model;

public class VideoData {
    public String title;
    public String name_epi;
    public int img;
    public VideoData(String title,int img){
        this.title=title;
        this.img=img;
    }
    public VideoData(String title,int img,String name_epi){
        this.title=title;
        this.img=img;
        this.name_epi=name_epi;
    }
}
