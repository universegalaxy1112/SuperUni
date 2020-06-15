package com.uni.julio.supertvplus.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.uni.julio.supertvplus.LiveTvApplication;

import java.util.HashSet;
import java.util.Set;

public class DataManager {

    private static DataManager m_DataMInstance;
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    public static DataManager getInstance() {
        if(m_DataMInstance == null) {
            m_DataMInstance = new DataManager();
        }
        return m_DataMInstance;
    }

    private DataManager() {
        pref = LiveTvApplication.getAppContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveDataLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public void saveData(String key, Object value) {
        if(value instanceof String) {
            editor.putString(key, (String) value);
        }
        else if(value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        else if(value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        else if(value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        else if(value instanceof Set) {
//            editor.putInt(key, (Integer) value);
            editor.remove(key);
            editor.apply();
            editor.putStringSet(key, (Set<String>)value);
        }
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public Set<String> getStringSet(String key) {
        return pref.getStringSet(key, new HashSet<String>());
//        Set<String> returnSet = new HashSet<>();
//        returnSet.addAll(pref.getStringSet(key, new HashSet<String>()));
//        return returnSet;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }
    public long getLong(String key, long defaultValue) {
        return Long.valueOf(pref.getLong(key, defaultValue));
    }
}
