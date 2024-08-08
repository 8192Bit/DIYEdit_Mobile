package com.x8192bit.diyeditmobile.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class SPUtils {

    private final static String sharedPreferencesName = "com.x8192Bit.DIYEdit_Mobile_preferences";

    public static Locale getLocale(Context context) {
        SharedPreferences sp = context.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        String locale = sp.getString("LanguageSelect", "system");
        return LocaleUtils.getLocaleFromString(locale);
    }

    public static boolean isTimeSorted(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        return sp.getBoolean("isTimeSorted", false);
    }

    public static boolean isNormalOrder(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        return sp.getBoolean("isNormalOrder", false);
    }

    public static boolean isShowingHiddenFiles(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        return sp.getBoolean("showHiddenFiles", false);
    }

    public static void setTimeSorted(Context context, boolean isTimeSorted) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        sp.edit().putBoolean("isTimeSorted", isTimeSorted).apply();
    }

    public static void setNormalOrder(Context context, boolean isNormalOrder) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        sp.edit().putBoolean("isNormalOrder", isNormalOrder).apply();
    }

    public static void setShowingHiddenFiles(Context context, boolean isShowingHiddenFiles) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        sp.edit().putBoolean("showHiddenFiles", isShowingHiddenFiles).apply();
    }

    public static String getTheme(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        return sp.getString("ThemeSelect", "system");
    }

    private static Boolean isFullFilePath(Context context) {
        SharedPreferences sp = context.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        return sp.getBoolean("showFullPath", false);
    }

    public static String[] getHistoryItems(Context context) {
        SharedPreferences sp = context.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        String history = sp.getString("history", null);
        String[] paths = history.split(",");
        if (isFullFilePath(context)) {
            return paths;
        } else {
            String[] items = new String[paths.length];
            for (int i = 0; i < items.length; i++) {
                String[] slashSplit = paths[i].split("/");
                items[i] = slashSplit[slashSplit.length - 1];
            }
            return items;
        }
    }

    public static String[] getHistoryPaths(Context context) {
        SharedPreferences sp = context.getSharedPreferences("com.x8192Bit.DIYEdit_Mobile_preferences", MODE_PRIVATE);
        String history = sp.getString("history", null);
        String[] paths = history.split(",");
        return paths;
    }
}
