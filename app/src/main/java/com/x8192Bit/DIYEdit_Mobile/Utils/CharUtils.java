package com.x8192Bit.DIYEdit_Mobile.Utils;

public class CharUtils {
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String doubleFirstChar(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(0, str.charAt(0));
        return sb.toString();
    }
}
