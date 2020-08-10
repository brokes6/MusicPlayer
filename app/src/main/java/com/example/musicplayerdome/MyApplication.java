package com.example.musicplayerdome;

import android.app.Application;

import com.example.musicplayerdome.db.DaoMaster;
import com.example.musicplayerdome.db.DaoSession;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.notification.NotificationConstructor;
import com.xuexiang.xui.BuildConfig;
import com.xuexiang.xui.XUI;

import org.greenrobot.greendao.database.Database;


/**
 * 应用初始化
 * 初始化 XUI框架
 * 初始化 SharedPreferencesUtil
 * 初始化 NotificationConstructor
 * 初始化 MusicManager
 * 需要去Android Manifest里使用
 * @author fuxinbo
 * @since 2020/04/09 10:07
 */

public class MyApplication extends Application {
    private static MyApplication mContext;
    private static DaoSession mDaoSession;
    public static final String DATA_BASE_NAME = "MusicPlay";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        XUI.init(this);
        XUI.debug(MyApplication.isDebug());
        MusicManager.initMusicManager(this);
        SharedPreferencesUtil.getInstance(this,"SPy");
        initDataBase();
        setNotification();
    }
    private void initDataBase() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, DATA_BASE_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
    private void setNotification(){
        MusicManager.getInstance().setNotificationConstructor(
                new NotificationConstructor.Builder()
                        .setCreateSystemNotification(true)
                        .setNotificationCanClearBySystemBtn(false)
                        .setTargetClass("com.example.musicplayerdome.song.view.SongActivity")
                        .bulid()
        );
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
    public static MyApplication getContext() {
        return mContext;
    }


}
