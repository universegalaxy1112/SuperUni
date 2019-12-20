package com.uni.julio.supertv.model;

public class LiveProgram extends VideoStream {

//    private int contentId;        cve
//    private String title;         nombre
//    private String HDPosterUrl;   icono
//    private String StreamUrl;     stream

    private String epg_ahora;
    private String epg_despues;
    private String iconUrl;

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
}
