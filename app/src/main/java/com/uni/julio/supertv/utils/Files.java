package com.uni.julio.supertv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.uni.julio.supertv.LiveTvApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Files {
    public static File GetFile(String path) {
        try {
            return new File(path);
        }catch (Exception e){}
        return null;
    }

    public static File GetFile(String path, String child) {
        try {
            return new File(path, child);
        }catch (Exception e){}
        return null;
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

    public static String ReadFile(String filename)
    {
        String data = null;
        try
        {
            File file = new File(filename);
            byte fileContent[] = new byte[(int)file.length()];

            FileInputStream fin = new FileInputStream(file);
            fin.read(fileContent);
            fin.close();
            data = new String(fileContent);
        }
        catch (Exception e){data = null;}
        return data;
    }

    public static boolean WriteFile(String filename, String data)
    {
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;
        try
        {
            try
            {
//                DBG("SUtils", "WriteFile(), File = " + filename);
                File file = new File(filename);
                if(!file.exists())
                {
                    File parent = new File(file.getParent());
                    parent.mkdirs();
                    parent = null;

                    file.createNewFile();
                }
                file = null;
            } catch (Exception ex) {}

            fOut = new FileOutputStream(filename);
            osw = new OutputStreamWriter(fOut);
            osw.append(data);
            osw.flush();
            osw.close();
            fOut.close();
        }
        catch (Exception e)
        {
//            DBG_EXCEPTION(e);
            return false;
        }
        return true;
    }

//    public static Bitmap getBitmap(String imagePath)
//    {
//        try {
//            File f = new File(imagePath);
//            return BitmapFactory.decodeStream(new FileInputStream(f));
//        }
//        catch (FileNotFoundException e)
//        {
//            return null;
//        }
//    }
    public static Bitmap getBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        final Bitmap b = BitmapFactory.decodeFile(path, options);
        return b;
    }
}
