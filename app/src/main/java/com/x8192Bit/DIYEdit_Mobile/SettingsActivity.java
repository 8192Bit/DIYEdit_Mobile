package com.x8192Bit.DIYEdit_Mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.x8192Bit.DIYEdit_Mobile.utils.CharUtils;

import x8192Bit.DIYEdit_Mobile.R;


public class SettingsActivity extends AppCompatActivity {

    /*
    private boolean isNightMode, isChange;
    private String

    private void initView() {
        //switch初始化
        isNightMode = (boolean) getApplicationContext().getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE).getString("ThemeSelectKey", "system");
        switchNight.setChecked(isNightMode);
        //添加switch切换监听
        switchNight.setOnCheckedChangeListener((compoundButton, b) -> {
            //模式改变时才发送事件
            if (isNightMode == b) return;
            isChange = !isChange;
            SharedPreferencesUtils.put("isNightMode", b);
            EventBus.getDefault().post(new ChangeModeBean());
        });
    }*/


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
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

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
            Preference openAboutDialog = findPreference("openAboutDialog");
            Preference ThemeSelect = findPreference("ThemeSelect");
            assert maxHistoryCount != null;
            assert cleanAllHistory != null;
            assert openAboutDialog != null;
            maxHistoryCount.setOnPreferenceChangeListener(this);
            assert ThemeSelect != null;
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
                if (newValue.equals("day")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //((AppCompatActivity) requireActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        //邪术
                    }
                } else if (newValue.equals("night")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //((AppCompatActivity) requireActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        //邪术
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        //((AppCompatActivity) requireActivity()).getDelegate().set(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    } else {
                        //邪术
                    }
                }
                return true;
            }
            return false;
        }
    }
}