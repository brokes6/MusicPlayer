package com.example.musicplayerdome;

import android.app.Application;

import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.xuexiang.xui.BuildConfig;
import com.xuexiang.xui.XUI;


/**
 * 应用初始化
 * 初始化XUI框架
 * 需要去Android Manifest里使用
 * @author fuxinbo
 * @since 2020/04/09 10:07
 */

public class MyApplication extends Application {
    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        XUI.init(this);
        XUI.debug(MyApplication.isDebug());
        SharedPreferencesUtil.getInstance(this,"SPy");
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
    public static MyApplication getContext() {
        return mContext;
    }


}
