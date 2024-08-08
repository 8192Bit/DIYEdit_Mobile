package com.x8192bit.diyeditmobile;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.x8192bit.diyeditmobile.utils.SPUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeLanguage(getBaseContext());
        super.onCreate(savedInstanceState);
    }

    public void changeLanguage(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = SPUtils.getLocale(context);
        resources.updateConfiguration(configuration, null);
    }

    public void changeLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, null);
    }

    public void invokeRecreate() {
        recreate();
    }

}
