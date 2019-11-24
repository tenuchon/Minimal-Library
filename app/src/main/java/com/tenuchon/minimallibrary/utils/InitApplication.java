package com.tenuchon.minimallibrary.utils;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


public class InitApplication extends Application {
    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static final String SORT_MODE = "SORT_MODE";

    public static final int SORT_MODE_TITLE = 1;
    public static final int SORT_MODE_AUTHOR = 2;
    public static final int SORT_MODE_DATE = 3;

    private boolean isNightModeEnabled = false;
    private int sortMode;

    private static InitApplication singleton = null;

    public static InitApplication getInstance() {

        if (singleton == null) {
            singleton = new InitApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isNightModeEnabled = prefs.getBoolean(NIGHT_MODE, false);
        this.sortMode = prefs.getInt(SORT_MODE, SORT_MODE_DATE);
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public int getSortMode() {
        return sortMode;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SORT_MODE, sortMode);
        editor.apply();
    }
}

