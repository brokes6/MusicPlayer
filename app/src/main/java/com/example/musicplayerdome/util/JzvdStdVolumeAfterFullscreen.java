package com.example.musicplayerdome.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaSystem;
import cn.jzvd.JzvdStd;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN;

public class JzvdStdVolumeAfterFullscreen extends JzvdStd {
    public JzvdStdVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        titleTextView.setVisibility(View.GONE);
    }
    @Override
    public void onPrepared() {
        super.onPrepared();
        if (screen == SCREEN_FULLSCREEN) {
            mediaInterface.setVolume(1f, 1f);
        } else {
            mediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void gotoScreenFullscreen() {
        super.gotoScreenFullscreen();
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (mediaInterface != null)
            mediaInterface.setVolume(1f, 1f);
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        if (mediaInterface != null)
            mediaInterface.setVolume(0f, 0f);
    }
}
