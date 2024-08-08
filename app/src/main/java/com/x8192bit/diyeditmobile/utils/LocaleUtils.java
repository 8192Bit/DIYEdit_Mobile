package com.x8192bit.diyeditmobile.utils;

import java.util.Locale;

public class LocaleUtils {

    public static Locale getLocaleFromString(String locale) {
        if (!locale.equals("system")) {
            String[] str = locale.split("-");
            return new Locale(str[0], str[1]);
        } else {
            return Locale.getDefault();
        }
    }

}
