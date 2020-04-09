package com.example.musicplayerdome.util;

/**
 * Created by ding on 2016/10/6.
 * <p>
 * 音频播放相关的常量
 */
public class AudioPlayerConstant {
    //发送到Activity的Action
    public static final String ACITION_AUDIO_PLAYER_SERVICE_TO_ACT = "action_audio_player_service_to_act";
    //发送到Service的Action
    public static final String ACITION_AUDIO_PLAYER_ACT_TO_SERVICE = "action_audio_player_act_to_service";
    //播放1
    public static final int ACITION_AUDIO_PLAYER_PLAY = 1;
    //暂停2
    public static final int ACITION_AUDIO_PLAYER_PAUSE = ACITION_AUDIO_PLAYER_PLAY + 1;
    //停止3
    public static final int ACITION_AUDIO_PLAYER_STOP = ACITION_AUDIO_PLAYER_PAUSE + 1;
    //再次播放4
    public static final int ACITION_AUDIO_PLAYER_REPLAY = ACITION_AUDIO_PLAYER_STOP + 1;
    //判断播放状态5
    public static final int ACITION_AUDIO_PLAYER_JUDGE = ACITION_AUDIO_PLAYER_REPLAY + 1;
    //准备状态6
    public static final int ACITION_AUDIO_PLAYER_PREPARE = ACITION_AUDIO_PLAYER_JUDGE + 1;
    //准备完成7
    public static final int ACITION_AUDIO_PLAYER_PREPARED = ACITION_AUDIO_PLAYER_PREPARE + 1;
    //MediaPlayer当前播放进度8
    public static final int ACITION_AUDIO_PLAYER_UPDATE_MEDIA_CURRENT_PROGRESS = ACITION_AUDIO_PLAYER_PREPARED + 1;
    //MediaPlayer缓冲进度9
    public static final int ACITION_AUDIO_PLAYER_UPDATE_MEDIA_BUFFERING_PROGRESS = ACITION_AUDIO_PLAYER_UPDATE_MEDIA_CURRENT_PROGRESS + 1;
    //保存进度
    public static final int ACITION_AUDIO_PLAYER_SAVE_CURENT_DATA = ACITION_AUDIO_PLAYER_UPDATE_MEDIA_BUFFERING_PROGRESS + 1;
    //播放下一首
    public static final int ACITION_AUDIO_PLAYER_PLAY_NEXT_AUDIO = ACITION_AUDIO_PLAYER_SAVE_CURENT_DATA + 1;
    //播放上一首
    public static final int ACITION_AUDIO_PLAYER_PLAY_PRE_AUDIO = ACITION_AUDIO_PLAYER_PLAY_NEXT_AUDIO + 1;
    //播放选择的某一首
    public static final int ACITION_AUDIO_PLAYER_PLAY_SELECT_AUDIO = ACITION_AUDIO_PLAYER_PLAY_PRE_AUDIO + 1;
    //滑动seekBar
    public static final int ACITION_AUDIO_PLAYER_SEEK = ACITION_AUDIO_PLAYER_PLAY_SELECT_AUDIO + 1;
    //获取时长
    public static final int ACITION_AUDIO_PLAYER_GET_MEDIADURATION = ACITION_AUDIO_PLAYER_SEEK + 1;
    //获取总时长
    public static final int ACITION_AUDIO_PLAYER_GET_MEDIADURATION_ALL = ACITION_AUDIO_PLAYER_GET_MEDIADURATION + 1;
    //播放完成
    public static final int ACITION_AUDIO_PLAYER_PLAY_COMPLETE = ACITION_AUDIO_PLAYER_GET_MEDIADURATION_ALL + 1;
    //获取初始数据
    public static final int ACITION_AUDIO_PLAYER_GET_INIT_DATA = ACITION_AUDIO_PLAYER_PLAY_COMPLETE + 1;
    //销毁播放服务
    public static final int ACITION_AUDIO_PLAYER_DESTROY = ACITION_AUDIO_PLAYER_GET_INIT_DATA + 1;
    //初始化成功
    public static final int ACITION_AUDIO_PLAYER_INIT_SUCCESS = ACITION_AUDIO_PLAYER_DESTROY + 1;
    //重新获取播放列表
    public static final int ACITION_AUDIO_PLAYER_REGET_PLAYER_LIST = ACITION_AUDIO_PLAYER_INIT_SUCCESS + 1;
    //获取所有数据
    public static final int ACITION_AUDIO_PLAYER_GET_ALL_DATA = ACITION_AUDIO_PLAYER_REGET_PLAYER_LIST + 1;
    public static final int ACITION_AUDIO_PLAYER_ERROE = ACITION_AUDIO_PLAYER_GET_ALL_DATA + 1;
    //定时关闭
    public static final int ACITION_AUDIO_DELAY_CLOSE = ACITION_AUDIO_PLAYER_ERROE + 1;
    public static final int ACITION_AUDIO_PLAYER_ERROR = ACITION_AUDIO_DELAY_CLOSE + 1;
    //是否需要继续播放
    public static boolean needContinuePlay = false;
    //当前位置
    public static int currentPosition;
    //当前audio的类型，为音频还是听法
    public static int type;
    //播放状态
    public static int playerState;
    //各种进度
    public static int current_progress, bufferingProgress, totalSeconds, media_duration;
    //当前时间和总时间
    public static String currentTimeStr, totalTimeStr;
    //书名
    public static String BookName = "";
}
