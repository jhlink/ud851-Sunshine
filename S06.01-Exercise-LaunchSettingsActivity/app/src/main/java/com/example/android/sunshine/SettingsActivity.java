package com.example.android.sunshine;

import android.drm.DrmStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pref);

        ActionBar menuBar = this.getSupportActionBar();

        if (menuBar != null) {
            menuBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
