package com.uni.julio.supertv.listeners;

public interface DownloaderListener {
    void onDownloadComplete(String str);

    void onDownloadError(int i);
}
