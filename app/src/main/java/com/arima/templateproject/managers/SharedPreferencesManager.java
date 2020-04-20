package com.arima.templateproject.managers;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public static final String SHARED_PREFERENCES = "preferences";

    public static final String SP_USER = "user";

    public SharedPreferencesManager(ContextWrapper context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void savePreference(String key, Object value) {
        String objectString = gson.toJson(value);
        editor.putString(key, objectString);
        editor.commit();
    }

    public void savePreference(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreference(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public Object getObjectPreference(String key, Class clazz) {
        String jsonObject = sharedPreferences.getString(key, "");
        if(jsonObject.isEmpty()) {
            return null;
        }
        return gson.fromJson(jsonObject, clazz);
    }

    public String getStringPreference(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getIntPreference(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void removePreference(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clearSharedPreferences() {
        editor.clear();
        editor.commit();
    }

}
