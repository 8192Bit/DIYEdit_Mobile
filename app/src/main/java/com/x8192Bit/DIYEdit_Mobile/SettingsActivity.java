package com.x8192Bit.DIYEdit_Mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.x8192Bit.DIYEdit_Mobile.utils.CharUtils;

import x8192Bit.DIYEdit_Mobile.R;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference maxHistoryCount = findPreference("maxHistoryCount");
            Preference cleanAllHistory = findPreference("cleanAllHistory");
            Preference openAboutDialog = findPreference("openAboutDialog");
            Preference ThemeSelect = findPreference("ThemeSelect");
            assert maxHistoryCount != null;
            assert cleanAllHistory != null;
            assert openAboutDialog != null;
            assert ThemeSelect != null;
            maxHistoryCount.setOnPreferenceChangeListener(this);
            ThemeSelect.setOnPreferenceChangeListener(this);
            cleanAllHistory.setOnPreferenceClickListener(this);
            openAboutDialog.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (key.equals("cleanAllHistory")) {
                SharedPreferences sp = this.requireContext().getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
                sp.edit().putString("history", null).apply();
                Toast.makeText(requireContext(), R.string.cleanedKey, Toast.LENGTH_SHORT).show();
            }
            if (key.equals("openAboutDialog")) {
                Intent i = new Intent(this.requireContext(), AboutActivity.class);
                startActivity(i);
            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            if (key.equals("maxHistoryCount")) {
                if (CharUtils.isNumeric((String) newValue)) {
                    try {
                        if (Integer.parseInt((String) newValue) > 0) {
                            return true;
                        } else {
                            Toast.makeText(requireContext(), R.string.historyTooExtremeKey, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), R.string.historyTooExtremeKey, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.numberRequiredKey, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (key.equals("ThemeSelect")) {
                MainActivity.setUIMode((String) newValue);
                return true;
            }
            return false;
        }
    }
}