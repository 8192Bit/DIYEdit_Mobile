package com.x8192Bit.DIYEdit_Mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.x8192Bit.DIYEdit_Mobile.Utils.CharUtils;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish(); // back button
            //process here
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
            assert maxHistoryCount != null;
            assert cleanAllHistory != null;
            maxHistoryCount.setOnPreferenceChangeListener(this);
            cleanAllHistory.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (key.equals("cleanAllHistory")) {
                SharedPreferences sp = this.getContext().getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
                sp.edit().putString("history", null).commit();
                Toast t = new Toast(getContext());
                t.setText("Cleaned.");
                t.show();
            }
            if (key.equals("openAboutDialog")) {

            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            if (key.equals("maxHistoryCount")) {
                if (CharUtils.isNumeric((String) newValue)) {
                    return true;  // 保存
                } else {
                    Toast t = new Toast(getContext());
                    t.setText("it must be a number");
                    t.show();
                    return false;
                }
            }
            return false;  //不保存
        }
    }
}