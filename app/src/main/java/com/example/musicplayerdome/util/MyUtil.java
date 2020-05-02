package com.example.musicplayerdome.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class MyUtil {
    /**
     * 判断服务是否运行
     *
     * @param service_Name:服务的包名+类名
     */
    public static boolean isServiceRunning(Context context, String service_Name) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service_Name.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 设置文本信息
     *
     * @param content ：显示的文本；
     */
    public static void setText(TextView textView, String content) {
        if (textView == null) return;
        if (TextUtils.isEmpty(content)) return;
        textView.setText(content);
    }


    /**
     * 吐司
     *
     * @param context：上下文；
     * @param messageId:吐司内容Id；
     */
    public static void ShowToast(Context context, int messageId) {
        if (context == null) return;
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public static void setBgColor(Context context, View parent, int color) {
        if (parent == null || context == null) return;
        parent.setBackgroundColor(ContextCompat.getColor(context, color));
    }
    /**
     * 设置控件显示和隐藏
     */
    public static void setVisible(View view, int visible) {
        if (view == null) return;
        if (view.getVisibility() == visible) return;
        view.setVisibility(visible);
    }
    /**
     * 吐司
     *
     * @param context       ：上下文；
     * @param message:吐司内容；
     */
    public static void ShowToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
