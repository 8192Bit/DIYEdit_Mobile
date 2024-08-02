package com.x8192bit.diyeditmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(getApplicationContext());
    }

    public void changeLanguage(Context context) {
        if (context == null) {
            return;
        }
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        SharedPreferences sp = context.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        String language = sp.getString("LanguageSelect", "system");
        if (!language.equals("system")) {
            String[] str = language.split("-");
            configuration.locale = new Locale(str[0], str[1]);
        }
        resources.updateConfiguration(configuration, null);
    }

    public void invokeRecreate() {
        recreate();
    }

}
