package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferencescreen);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        int count = getPreferenceScreen().getPreferenceCount();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                p.setSummary(value);
            }
        }
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        if (preference instanceof EditTextPreference) {
            preference.setSummary(stringValue);
        } else if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int preferenceValue = listPreference.findIndexOfValue(stringValue);

            if (preferenceValue >= 0) {
                CharSequence summaryResId = listPreference.getEntries()[preferenceValue];
                listPreference.setSummary(summaryResId);
            }
        }
    }
}
