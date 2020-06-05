package com.example.musicplayerdome.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtil {
    private static DisplayUtil displayUtil = null;
    private static DisplayMetrics dm = null;

    public static DisplayUtil getInstance() {
        if (displayUtil == null) {
            displayUtil = new DisplayUtil();
        }
        return displayUtil;
    }

    /**
     * 获取DisplayMetrics对象
     *
     * @return DisplayMetrics
     */
    public DisplayMetrics getDisplayMetrics() {
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        return dm;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return 屏幕的宽度
     */
    public int getScreenWidth(Activity activity) {
        if (activity == null) {
            return 0;
        }
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return 屏幕的高度
     */
    public static int getScreenHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取设备操作系统版本
     *
     * @return
     */
    public String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public String getDevice() {
        return android.os.Build.MODEL;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context 上下文对象
     * @param pxValue 需要转换的值
     * @return 转换后的值
     */
    public int px2dip(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context  上下文对象
     * @param dipValue 需要转换的值
     * @return 转换后的值
     */
    public int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context 上下文对象
     * @param pxValue 需要转换的值
     * @return 转换后的值
     */
    public int px2sp(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context 上下文对象
     * @param spValue 需要转换的值
     * @return 转换后的值
     */
    public static int sp2px(Context context, float spValue) {
        if (context == null) {
            return 0;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale   （DisplayMetrics类中属性density）
     * @return
     */
    public int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale    （DisplayMetrics类中属性density）
     * @return
     */
    public int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public int px2sp(float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public int sp2px(float spValue, float fontScale) {
        return (int) (spValue * fontScale + 0.5f);
    }

    public int getScreenWidthctx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeightctx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;

    }

    public float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}

