package com.example.musicplayerdome.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    public PreferenceHelper() {
    }

    public static void write(Context context, String fileName, String k, int v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, boolean v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(k, v);
        editor.commit();
    }

    public static void write(Context context, String fileName, String k, String v) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(k, v);
        editor.commit();
    }

    public static int readInt(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        return preference.getInt(k, 0);
    }

    public static int readInt(Context context, String fileName, String k, int defv) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        return preference.getInt(k, defv);
    }

    public static boolean readBoolean(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        return preference.getBoolean(k, false);
    }

    public static boolean readBoolean(Context context, String fileName, String k, boolean defBool) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        return preference.getBoolean(k, defBool);
    }

    public static String readString(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        return preference.getString(k, (String)null);
    }

    public static String readString(Context context, String fileName, String k, String defV) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        return preference.getString(k, defV);
    }

    public static void remove(Context context, String fileName, String k) {
        SharedPreferences preference = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.remove(k);
        editor.commit();
    }

    public static void clean(Context cxt, String fileName) {
        SharedPreferences preference = cxt.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.commit();
    }
}