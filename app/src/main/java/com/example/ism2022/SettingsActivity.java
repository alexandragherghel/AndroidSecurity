package com.example.ism2022;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);

        addPreferencesFromResource(R.layout.activity_settings);

        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);

        boolean cbDarkTheme = spf.getBoolean("cbDarkTheme", false);
        String etDeveloper = spf.getString("etDeveloper", null);

        if (cbDarkTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
}