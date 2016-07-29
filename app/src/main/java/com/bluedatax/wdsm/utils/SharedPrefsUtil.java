package com.bluedatax.wdsm.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xuyuanqiang on 7/23/16.
 */
public class SharedPrefsUtil {

    public static void putLong(Context context,String key,long value) {
        SharedPreferences.Editor edit = context.getSharedPreferences("count", Context.MODE_PRIVATE).edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static void putValue(Context context, String key, boolean value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("count", Context.MODE_PRIVATE)
                .edit();
        sp.putBoolean(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("count", Context.MODE_PRIVATE)
                .edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences("count",
                Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
}

    public static boolean getValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences("count",
                Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences("count",
                Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
    public static long getLong(Context context, String key, Long defValue) {
        SharedPreferences sp = context.getSharedPreferences("count",
                Context.MODE_PRIVATE);
        long value = sp.getLong(key, defValue);
        return value;
    }
}
