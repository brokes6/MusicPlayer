package com.example.musicplayerdome.abstractclass;


import com.example.musicplayerdome.bean.Audio;
import com.smp.soundtouchandroid.MediaCallBack;
import com.smp.soundtouchandroid.OnProgressChangedListener;
import com.smp.soundtouchandroid.PreparedListener;

import java.util.List;


/**
 * 音乐控制器
 */
public interface MusicController {
    /**
     * 播放
     */
    void play();

    /**
     * 播放
     */
    void play(Audio audio);

    /**
     * 停止
     */
    void stop();

    /**
     * 暂停
     */
    void pause();

    boolean isPlaying();

    boolean isPause();

    void next();

    void Choice(int i);

    void pre();

    /**
     * 设置音频路径
     */
    void setPath(String path);

    /**
     * 这个参数是变速又变声的，这个参数大于0，否则会报错
     */
    void setRateChange(float rate);

    /**
     * 这个是音调，1.0表示正常
     */
    void setPitchSemi(float pitchSemi);

    /**
     * 这个是速度，1.0表示正常设置新的速度控制值，
     */
    void setTempoChange(float tempo);

    /**
     * 这个是速度，1.0表示正常设置新的速度控制值，
     */
    void setTempo(float tempo);

    float getTemp();

    /**
     * 设置左右声道
     *
     * @param i :1:左声道，2：右声道
     */
    void setChannels(int i);

    /**
     * 获取当前播放进度的时长，单位:微秒，us
     */
    long getPlayedDuration();

    /**
     * 获取总时长，单位:微秒，us
     */
    long getDuration();

    void seekTo(long progress);

    void setOnProgressChangedListener(OnProgressChangedListener progressListener);

    void setOnPreparedListener(PreparedListener listener);

    void setMediaCallBack(MediaCallBack listener);

    MediaCallBack getMediaCallBack();

    long getCurrentId();

    Audio getAudio();

    int getState();

    void removeCallBack();

    /**
     * 取消定时功能
     */
    void cancelDelay();

    /**
     * 定时关闭功能
     *
     * @param timerState :延时分钟
     */
    void delayClose(int timerState);

    void release();

    void resume();

    List<Audio> getPlayList();

    void setPlayList(List<Audio> list);

    void cleanPlayList();

    int getCurrentPosition();

    OnProgressChangedListener getProgressListener();

    boolean isSame(Audio audio);
}
