package com.uni.julio.supertvplus.listeners;

public interface DownloaderListener {
    void onDownloadComplete(String str);

    void onDownloadError(int i);
}
