package com.example.musicplayerdome.util;

import android.content.Context;

/**
 * SharePreferences的取值管理
 */
public class SPManager {
    /**
     * 获取定时状态
     */
    public static int getTimerState(Context context) {
        return PreferenceHelper.readInt(context, SP.FILE_NAME, SP.TIMER_STATE, TimerFlag.CLOSE);
    }

    public static void write(Context context, String key, int value) {
        PreferenceHelper.write(context, SP.FILE_NAME, key, value);
    }
}
