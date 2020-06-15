package com.uni.julio.supertvplus.model;

public class LiveProgram extends VideoStream {

//    private int contentId;        cve
//    private String title;         nombre
//    private String HDPosterUrl;   icono
//    private String StreamUrl;     stream

    private String epg_ahora;
    private String epg_despues;
    private String iconUrl;
    private String description = "";
    private String sub_title = "";

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getEpg_ahora() {
        return epg_ahora;
    }

    public void setEpg_ahora(String epg_ahora) {
        this.epg_ahora = epg_ahora;
    }

    public String getEpg_despues() {
        return epg_despues;
    }

    public void setEpg_despues(String epg_despues) {
        this.epg_despues = epg_despues;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getSub_title() {
        return sub_title;
    }
}
