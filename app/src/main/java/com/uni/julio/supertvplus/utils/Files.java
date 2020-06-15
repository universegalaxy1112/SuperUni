package com.uni.julio.supertvplus.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import androidx.core.content.FileProvider;

 import com.uni.julio.supertvplus.LiveTvApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Files extends FileProvider {
    public static File GetFile(String path) {
        try {
            return new File(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static File GetFile(String path, String child) {
        try {
            return new File(path, child);
        } catch (Exception e) {
            return null;
        }
    }

    public static String GetExternalsFilesDir() {
        return LiveTvApplication.getAppContext().getExternalFilesDir(null).getAbsolutePath();
    }

    public static String GetFilesDir() {
        return LiveTvApplication.getAppContext().getFilesDir().getAbsolutePath();
    }

    public static String GetCacheDir() {
        return LiveTvApplication.getAppContext().getCacheDir().getAbsolutePath();
    }

    public static String ReadFile(String filename) {
        try {
            File file = new File(filename);
            byte[] fileContent = new byte[((int) file.length())];
            FileInputStream fin = new FileInputStream(file);
            fin.read(fileContent);
            fin.close();
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean WriteFile(String filename, String data) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
        }
        try {
            FileOutputStream fOut = new FileOutputStream(filename);
            try {
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                try {
                    osw.append(data);
                    osw.flush();
                    osw.close();
                    fOut.close();
                    OutputStreamWriter outputStreamWriter = osw;
                    FileOutputStream fileOutputStream = fOut;
                    return true;
                } catch (Exception e2) {
                    OutputStreamWriter outputStreamWriter2 = osw;
                    FileOutputStream fileOutputStream2 = fOut;
                    return false;
                }
            } catch (Exception e3) {
                FileOutputStream fileOutputStream3 = fOut;
                return false;
            }
        } catch (Exception e4) {
            return false;
        }
    }

    public static Bitmap getBitmap(String path) {
        Options options = new Options();
        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(path, options);
    }
}
