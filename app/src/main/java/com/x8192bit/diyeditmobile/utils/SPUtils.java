package com.x8192bit.diyeditmobile.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SPUtils {

    private final static String sharedPreferencesName = "com.x8192bit.diyeditmobile_preferences";

    public static Locale getLocale(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
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
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        return sp.getBoolean("showFullPath", false);
    }

    public static String[] getHistoryItems(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        String history = sp.getString("history", null);
        if (history != null) {
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
        } else {
            return null;
        }
    }

    public static String[] getHistoryPaths(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        String history = sp.getString("history", null);
        if (history != null) {
            String[] paths = history.split(",");
            return paths;
        } else {
            return null;
        }
    }

    public static void appendHistory(Context context, String realPath) throws NumberFormatException {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        try {
            int historyCount = Integer.parseInt(sp.getString("maxHistoryCount", "10"));
            SharedPreferences.Editor ed = sp.edit();
            String history = sp.getString("history", null);
            StringBuilder sb = new StringBuilder();
            if (history != null) {
                ArrayList<String> historyArray = new ArrayList<>(Arrays.asList(history.split(",")));
                boolean isDuplicated = false;
                for (int i = 0; i < historyArray.size(); i++) {
                    if (historyArray.get(i).equals(realPath)) {
                        historyArray.remove(i);
                        break;
                    }
                }
                historyArray.add(0, realPath);
                if (historyArray.size() > historyCount) {
                    do {
                        historyArray.remove(historyCount - 1);
                    } while (historyArray.size() == historyCount);
                }
                for (int i = 0; i < historyArray.size(); i++) {
                    sb.append(historyArray.get(i)).append(",");
                }
            } else {
                sb.append(realPath);
            }
            ed.putString("history", sb.toString());
            ed.apply();
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public static void cleanHistory(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        sp.edit().putString("history", null).apply();
    }
}
