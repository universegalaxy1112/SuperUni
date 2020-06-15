package com.uni.julio.supertvplus.utils.networing;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;

import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.listeners.DownloaderListener;
import com.uni.julio.supertvplus.utils.DownloaderBroadcastReceiver;

import java.io.File;

public class Downloader {
    private static Downloader sDownloader = null;
    private Context context = LiveTvApplication.getAppContext();
    /* access modifiers changed from: private */
    public DownloadManager mDownloadManager = ((DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE));
    /* access modifiers changed from: private */
    public long myDownloadReference;
    private DownloaderBroadcastReceiver receiverDownloadComplete;

    private Downloader() {
    }

    public static Downloader getInstance() {
        if (sDownloader == null) {
            sDownloader = new Downloader();
        }
        return sDownloader;
    }

    public void performDownload(String url, final ProgressDialog progress, DownloaderListener listener) {
        if (hasUnknownSourcesOn()) {
            Resources res = this.context.getResources();
            Request request = new Request(Uri.parse(url));
            try {
                File f = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/update.apk");
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.setDescription(res.getString(R.string.downloading)).setTitle(res.getString(R.string.app_name)).setVisibleInDownloadsUi(true).setAllowedOverRoaming(false).setAllowedNetworkTypes(3).setDestinationInExternalFilesDir(this.context, Environment.DIRECTORY_DOWNLOADS, "update.apk");
             this.myDownloadReference = this.mDownloadManager.enqueue(request);
            IntentFilter iFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
            this.receiverDownloadComplete = new DownloaderBroadcastReceiver(this.mDownloadManager, this.myDownloadReference, listener);
            this.context.registerReceiver(this.receiverDownloadComplete, iFilter);
            new Thread(new Runnable() {
                public void run() {
                    boolean downloading = true;
                    while (downloading) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Query q = new Query();
                        q.setFilterById(new long[]{Downloader.this.myDownloadReference});
                        Cursor cursor = mDownloadManager.query(q);
                        cursor.moveToFirst();
                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex("bytes_so_far"));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex("total_size"));
                        if (cursor.getInt(cursor.getColumnIndex("status")) == 8) {
                            downloading = false;
                        }
                        progress.setProgress((int) ((((long) bytes_downloaded) * 100) / ((long) bytes_total)));
                        cursor.close();
                    }
                }
            }).start();
            return;
        }
        listener.onDownloadError(1);
    }

    private boolean hasUnknownSourcesOn() {
        if (VERSION.SDK_INT < 17) {
            if (Secure.getInt(this.context.getContentResolver(), "install_non_market_apps", 0) == 1) {
                return true;
            }
            return false;
        } else if (Global.getInt(this.context.getContentResolver(), "install_non_market_apps", 0) != 1) {
            return false;
        } else {
            return true;
        }
    }
}
