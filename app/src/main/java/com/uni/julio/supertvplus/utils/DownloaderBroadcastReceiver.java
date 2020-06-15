package com.uni.julio.supertvplus.utils;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.uni.julio.supertvplus.listeners.DownloaderListener;

public class DownloaderBroadcastReceiver extends BroadcastReceiver {
    private long downloadReference;
    private DownloaderListener listener;
    private DownloadManager manager;

    public DownloaderBroadcastReceiver(DownloadManager manager2, long reference, DownloaderListener listener2) {
        this.downloadReference = reference;
        this.listener = listener2;
        this.manager = manager2;
    }

    public void onReceive(Context context, Intent intent) {
        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (reference == this.downloadReference) {
            Query query = new Query();
            query.setFilterById(new long[]{reference});
            Cursor cursor = this.manager.query(query);
            cursor.moveToFirst();
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String temp=cursor.getString(cursor.getColumnIndex("local_uri"));
            String savedFilePath = cursor.getString(cursor.getColumnIndex("local_uri")).replace("file://", "");
            switch (status) {
                case 1:
                    this.listener.onDownloadError(-1);
                    return;
                case 4:
                    this.listener.onDownloadError(-1);
                    return;
                case 8:
                    this.listener.onDownloadComplete(savedFilePath);
                    return;
                case 16:
                    this.listener.onDownloadError(-1);
                    return;
                default:
                    return;
            }
        }
    }
}
