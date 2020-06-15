package com.heavykiwi.library.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

// *** Created by Kely George on 8/9/2016. *** //
public class LogUtils {
    public static void saveLog(String content) {
        saveLog("OBD2_log", content);
    }
    public static void saveLog(String filename, String content) {
        String file = filename;
        FileOutputStream overWrite = null;
        try {
            if (TextUtils.isEmpty(filename))
                file = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "obd2_log");
            if (!root.exists()) {
                root.mkdirs();
            }
            File fileToSave = new File(root, file + ".txt");// file path to save
            if (!fileToSave.exists())
            {
                try
                {
                    fileToSave.createNewFile();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if(fileToSave.exists()){
                overWrite = new FileOutputStream(fileToSave, true);
                //content = DateTimeUtils.calendarToString(Calendar.getInstance(), DateTimeUtils.FMT_FULL) + "\n" + content + "\n\n";
                overWrite.write(content.getBytes());
                overWrite.flush();
                overWrite.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
