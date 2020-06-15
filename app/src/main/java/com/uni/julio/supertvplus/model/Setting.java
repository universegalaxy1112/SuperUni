package com.uni.julio.supertvplus.model;

//import org.simpleframework.xml.Root;

public class Setting extends VideoStream{

//    private String title;
    private String SDPosterUrl;
    private String HDPosterUrl;

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getSDPosterUrl() {
        return SDPosterUrl;
    }

    public void setSDPosterUrl(String SDPosterUrl) {
        this.SDPosterUrl = SDPosterUrl;
    }

    public String getHDPosterUrl() {
        return HDPosterUrl;
    }

    public void setHDPosterUrl(String HDPosterUrl) {
        this.HDPosterUrl = HDPosterUrl;
    }

}
