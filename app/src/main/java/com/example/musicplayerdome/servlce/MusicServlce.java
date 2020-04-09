package com.example.musicplayerdome.servlce;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.audio.MusicControllerImp;
import com.example.musicplayerdome.audio.MusicNotification;

/**
 * 音乐servlce
 * @author fuxinbo
 * @since 2020/04/09 10:07
 */

public class MusicServlce extends Service {
        private static final String TAG = "MyService";
        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return super.onStartCommand(intent, flags, startId);

        }

        @Override
        public IBinder onBind(Intent intent) {
            return mediaplayerBinder;
        }
        /**
         * 返回MusicController抽象类的实现类的实列
         */
        private MediaplayerBinder mediaplayerBinder = new MediaplayerBinder();
        public class MediaplayerBinder extends Binder {
            public MusicController getService() {
                return MusicControllerImp.getInstance(MusicServlce.this);
            }
        }

        @Override
        public boolean onUnbind(Intent intent) {
            return super.onUnbind(intent);
        }

        @Override
        public void onRebind(Intent intent) {
            super.onRebind(intent);
        }
        /**
         * 取消通知栏(里面会取消广播，注意查看)
         */
        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.e(TAG, "MusicServlce成功解绑");
            MusicNotification
                    .getMusicNotification(this, MusicControllerImp.getInstance(this))
                    .onCancelMusicNotifi();
        }
    }

