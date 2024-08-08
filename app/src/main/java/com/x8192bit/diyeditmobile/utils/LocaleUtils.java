package com.x8192bit.diyeditmobile.utils;

import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleUtils {

    public static Locale getLocaleFromString(String locale) {
        if (locale.equals("system")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList localeList = LocaleList.getDefault();
                return localeList.get(0);
            } else {
                return Locale.getDefault();
            }
        } else {
            String[] str = locale.split("-");
            return new Locale(str[0], str[1].replace("r", ""));
        }
    }

}
