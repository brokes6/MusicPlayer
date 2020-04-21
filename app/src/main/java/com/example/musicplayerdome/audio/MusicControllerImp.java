package com.example.musicplayerdome.audio;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.util.AudioPlayerConstant;
import com.example.musicplayerdome.util.SP;
import com.example.musicplayerdome.util.SPManager;
import com.example.musicplayerdome.util.TimerFlag;
import com.example.musicplayerdome.util.XToastUtils;
import com.smp.soundtouchandroid.AudioSpeed;
import com.smp.soundtouchandroid.MediaCallBack;
import com.smp.soundtouchandroid.OnProgressChangedListener;
import com.smp.soundtouchandroid.PreparedListener;
import com.smp.soundtouchandroid.SoundStreamAudioPlayer;

import java.io.IOException;
import java.util.List;

import static com.example.musicplayerdome.util.AudioPlayerConstant.playerState;


public class MusicControllerImp implements MusicController {
    private SoundStreamAudioPlayer player;
    private float tempo = 1.0f;//这个是速度，1.0表示正常设置新的速度控制值，
    private float pitchSemi = 1.0f;//这个是音调，1.0表示正常，
    private float rate = 1.0f;//这个参数是变速又变声的，这个参数大于0，否则会报错
    Thread playThread;
    Context context;
    private final AudioFocusManager focusManager;
    private final MediaSessionManager sessionManager;
    private MusicNotification notification;
    private static final String TAG = "MusicControllerImp";
    //以下属于广播需要
    private final static String ACTIONS = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClickS";
    private final static String INTENT_BUTTONID_TAG = "ButtonId";
    //广播需要结束
    private MusicControllerImp(Context context) {
        this.context = context;
        this.sessionManager = new MediaSessionManager(context, this);
        this.focusManager = new AudioFocusManager(context, this);
        notification = MusicNotification.getMusicNotification(context, this);
    }

    private static MusicController musicController;

    public static MusicController getInstance(Context context) {
        if (musicController == null) {
            synchronized (MusicController.class) {
                if (musicController == null) {
                    musicController = new MusicControllerImp(context);
                }
            }
        }
        return musicController;
    }


    @Override
    public void play() {
        try {
            if (player != null && player.isPause()) {
                player.start();
                Log.e(TAG, "play: 播放？");

                Intent intent = new Intent();
                intent.setAction(ACTIONS);
                intent.putExtra(INTENT_BUTTONID_TAG, 2);
                context.sendBroadcast(intent);

                sessionManager.updatePlaybackState();
            } else if (player != null && status == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARE) {
                Log.d(TAG, "正在加载中..." );
                onState(status);
            } else {
                start();
            }
        } catch (Exception e) {
            if (serviceMediaCallBack != null) {
                serviceMediaCallBack.onError();
            }
            e.printStackTrace();
        }
    }

    Audio audio;

    public void setAudio(Audio audio) {
        this.audio = audio;
        setCurrentPosition(getCurrentPosition(audio));
    }

    private int getCurrentPosition(Audio audio) {
        if (audio == null) return 0;
        if (list == null || list.isEmpty()) return 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && audio.getId() == list.get(i).getId()) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void play(Audio audio) {
        try {
            if (isSame(audio)) {
                if (player.isPlaying()) {
                    pause();
                } else if (player.isPause()) {
                    player.start();
                    sessionManager.updatePlaybackState();
                } else if (status == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARE) {
                    Log.d("debug", "正在加载中。。。");
                    onState(status);
                } else {
                    setAudio(audio);
                    start();
                }
            } else {
                setAudio(audio);
                start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void next() {
        Log.e(TAG, "play: 下一首？");
        initPosition(AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_NEXT_AUDIO);

        Intent intent = new Intent();
        intent.setAction(ACTIONS);
        intent.putExtra(INTENT_BUTTONID_TAG, 4);
        intent.putExtra("name",audio.getName());
        context.sendBroadcast(intent);
    }
    //输入音乐资源下标，来进行播放
    @Override
    public void Choice(int i) {
        ChoicePosition(AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_SELECT_AUDIO,i);

        Intent intent = new Intent();
        intent.setAction(ACTIONS);
        intent.putExtra(INTENT_BUTTONID_TAG, 5);
        intent.putExtra("name",audio.getName());
        context.sendBroadcast(intent);
    }

    @Override
    public void pre() {
        initPosition(AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_PRE_AUDIO);

        Intent intent = new Intent();
        intent.setAction(ACTIONS);
        intent.putExtra(INTENT_BUTTONID_TAG, 1);
        intent.putExtra("name",audio.getName());
        context.sendBroadcast(intent);
    }
    //更具传入的下标来创建Audio
    private void ChoicePosition(int action,int num){
        Log.e(TAG, "正在改变音乐播放地址：下标为："+num);
        if (list == null || list.isEmpty()) return;
        int temp = this.currentPosition;
        List<Audio> audioList = list;
        //判断audioList不能为空且长度大于0，传入的下标不能比audioList的长度大
        if (audioList != null && audioList.size() > 0 && num<audioList.size()) {
            if (action == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_SELECT_AUDIO) {
                Audio audio = audioList.get(num);
                if (audio != null) {
                    //不能观看
                    if (audio.isLock()) {
                        XToastUtils.warning("没有歌曲拉");
                        setCurrentPosition(temp);
                    } else {
                        status = 0;
                        setAudio(audio);
                        if (mediaCallBack != null) {
                            mediaCallBack.onChange(action);
                        }
                        start();
                    }
                }
            }
        }
    }
    //下一首
    private int currentPosition;
    private void initPosition(int action) {
        if (list == null || list.isEmpty()) return;
        int temp = this.currentPosition;
        List<Audio> audioList = list;
        if (audioList != null && audioList.size() > 0) {
            if (action == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_NEXT_AUDIO) {
                currentPosition = currentPosition + 1;
                if (currentPosition > audioList.size() - 1) {
                    currentPosition = 0;
                }
            } else {
                currentPosition = currentPosition - 1;
                if (currentPosition < 0) {
                    currentPosition = audioList.size() - 1;
                }
            }
            Audio audio = audioList.get(currentPosition);
            if (audio != null) {
                //不能观看
                if (audio.isLock()) {
                    XToastUtils.warning("没有歌曲拉");
                    setCurrentPosition(temp);
                } else {
                    status = 0;
                    setAudio(audio);
                    if (mediaCallBack != null) {
                        mediaCallBack.onChange(action);
                    }
                    start();
                }
            }
        }
    }

    private void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }
    //对audioList的前身List进行增删，获取
    List<Audio> list;
    @Override
    public List<Audio> getPlayList() {
        return list;
    }

    @Override
    public void setPlayList(List<Audio> list) {
        this.list = list;
    }

    @Override
    public void cleanPlayList() {
        if (list==null)return;
        list.clear();
    }



    //继续播放
    @Override
    public void resume() {
        if (status != AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY) {
            focusManager.requestAudioFocus();
            sessionManager.updatePlaybackState();
            player.start();
        }
    }

    @Override
    public boolean isSame(Audio audio) {
        if (audio == null) return false;
        if (this.audio == null) return false;
        return player != null && this.audio != null && audio.getId() == this.audio.getId();
    }

    @Override
    public void stop() {
        try {
            if (player != null) {
                player.stop();
            }
            player = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        Log.e(TAG, "pause: 暂停？？？");
        if (player != null) {
            if (!player.isPaused()) {
                player.pause();

                Intent intent = new Intent();
                intent.setAction(ACTIONS);
                intent.putExtra(INTENT_BUTTONID_TAG, 3);
                context.sendBroadcast(intent);

                sessionManager.updatePlaybackState();
            }
        }
    }

    @Override
    public void setPath(String path) {
    }

    private void start() {
        try {
            if (player != null) {
                player.stop();
                player.seekTo(0, true);
                player.release();
                player = null;
            }
            player = new SoundStreamAudioPlayer(0, audio.getFileUrl(), tempo, pitchSemi, serviceMediaCallBack, new PreparedListener() {
                @Override
                public void onPrepared(long duration) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            player.setOnProgressChangedListener(new OnProgressChangedListener() {
                                @Override
                                public void onProgressChanged(int track, double currentPercentage, long position) {
                                    if (player == null) return;
                                    if (progressListener == null) return;
                                    progressListener.onProgressChanged(track, currentPercentage, position);
                                }

                                @Override
                                public void onTrackEnd(int track) {
                                    Log.d("debug", "onTrackEnd=" + track);
                                    if (progressListener != null) {
                                        progressListener.onTrackEnd(track);
                                    }
                                }

                                @Override
                                public void onExceptionThrown(String string) {
                                    if (serviceMediaCallBack != null) {
                                        serviceMediaCallBack.onError();
                                    }
                                    if (progressListener != null) {
                                        progressListener.onExceptionThrown(string);
                                    }
                                    Log.d("debug", "onExceptionThrown=" + string);
                                }
                            });
                            if (player != null) {
                                player.setRate(rate);
                                player.setTempo(tempo);
                            }
                            if (playThread != null) {
                                playThread.interrupt();
                                playThread = null;
                            }
                            if (progressListener != null) {
                                progressListener.onProgressChanged(0, 0f, 0);
                            }
                            playThread = new Thread(player);
                            playThread.start();
                            player.start();
                            sessionManager.updateMetaData(audio);
                            focusManager.requestAudioFocus();
                            sessionManager.updatePlaybackState();
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int status;

    @Override
    public long getCurrentId() {
        return audio == null ? 0 : audio.getId();
    }

    @Override
    public Audio getAudio() {
        return audio;
    }


    @Override
    public int getState() {
        return status;
    }

    @Override
    public boolean isPlaying() {
        return player == null ? false : player.isPlaying();
    }

    @Override
    public boolean isPause() {
        return player == null ? false : player.isPause();
    }

    OnProgressChangedListener progressListener;

    @Override
    public void setOnProgressChangedListener(OnProgressChangedListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public OnProgressChangedListener getProgressListener() {
        return progressListener;
    }

    PreparedListener preparedListener;

    @Override
    public void setOnPreparedListener(PreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    MediaCallBack mediaCallBack;

    @Override
    public void setMediaCallBack(MediaCallBack listener) {
        this.mediaCallBack = listener;
    }

    @Override
    public MediaCallBack getMediaCallBack() {
        return mediaCallBack;
    }

    @Override
    public void removeCallBack() {
        mediaCallBack = null;
        progressListener = null;
    }

    MediaCallBack serviceMediaCallBack = new MediaCallBack() {
        @Override
        public void onChange(int state) {
            if (mediaCallBack != null) {
                mediaCallBack.onChange(state);
            }
        }

        @Override
        public void onPrepare() {
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARE;
            if (notification != null) {
                notification.upDataNotifacation(true, getAudio().getName(), getAudio().getFaceUrl(), false);
            }
            onChange(status);
            onState(status);
        }

        @Override
        public void onPrepared(long duration) {
            Log.d(TAG, "onPrepared: ");
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARED;
            onChange(status);
            onState(status, duration);
        }

        @Override
        public void onPlay() {
            if (notification != null) {
                notification.upDataNotifacation(false, getAudio().getName(), getAudio().getFaceUrl(), true);
            }
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY;
            onChange(status);
            onState(status);
        }

        @Override
        public void onStop() {
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_STOP;
            if (notification != null) {
                notification.upDataNotifacation(false, getAudio().getName(), getAudio().getFaceUrl(), false);
            }
            onChange(status);
            onState(status);
        }

        @Override
        public void onPause() {
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE;
            if (notification != null) {
                notification.upDataNotifacation(false, getAudio().getName(), getAudio().getFaceUrl(), false);
            }

            onChange(status);
            onState(status);
        }

        @Override
        public void onComplete() {
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_COMPLETE;
            if (notification != null) {
                notification.upDataNotifacation(false, getAudio().getName(), getAudio().getFaceUrl(), false);
            }
            onChange(status);
            onState(status);
            //如果定时模式为只播放当前，则暂停
            int timerState = SPManager.getTimerState(context);
            Log.d(TAG, " timerState=" + timerState);
            if (timerState == TimerFlag.CURRENT) {
            } else {//否则播放下一首
                next();
            }
        }

        @Override
        public void onError() {
            status = AudioPlayerConstant.ACITION_AUDIO_PLAYER_ERROR;
            if (notification != null) {
                notification.upDataNotifacation(false, getAudio().getName(), getAudio().getFaceUrl(), false);
            }

            onChange(status);
            onState(status);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AudioPlayerConstant.ACITION_AUDIO_DELAY_CLOSE:
                    if (isPlaying()) {
                        //定时状态设置为关闭
                        SPManager.write(context, SP.TIMER_STATE, TimerFlag.CLOSE);
                        pause();
                    }
                    break;
            }
        }
    };

    private void onState(final int status, final Long... duration) {
        if (mediaCallBack == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (status) {
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_COMPLETE:
                        mediaCallBack.onComplete();
                        break;
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE:
                        mediaCallBack.onPause();
                        break;
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY:
                        mediaCallBack.onPlay();
                        break;
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_STOP:
                        mediaCallBack.onStop();
                        break;
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARE:
                        mediaCallBack.onPrepare();
                        break;
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARED:
                        if (duration != null && duration.length > 0) {
                            mediaCallBack.onPrepared(duration[0]);
                        }
                        break;
                    case AudioPlayerConstant.ACITION_AUDIO_PLAYER_ERROR:
                        mediaCallBack.onError();
                        break;

                }
            }
        });

    }

    @Override
    public long getPlayedDuration() {
        if (player == null) return 0;
        return player.getPlayedDuration();
    }

    @Override
    public long getDuration() {
        return player == null ? 0 : player.getDuration();
    }

    @Override
    public void setRateChange(float rate) {
        this.rate = rate;
        if (player != null) {
            player.setRateChange(rate);
        }
    }

    @Override
    public void setPitchSemi(float pitchSemi) {
        this.pitchSemi = pitchSemi;
        if (player != null) {
            player.setPitchSemi(pitchSemi);
        }
    }

    @Override
    public void setTempoChange(float tempo) {
        this.tempo = tempo;
        if (player != null) {
            player.setTempoChange(tempo);
        }
    }

    @Override
    public void setTempo(float tempo) {
        this.tempo = tempo;
        if (player != null) {
            player.setTempo(tempo);
        }
    }

    @Override
    public float getTemp() {
        return tempo == 0 ? AudioSpeed.SPEED_NORMAL : tempo;
    }

    @Override
    public void seekTo(long progress) {
        if (player != null) {
            player.seekTo(progress);
            sessionManager.updatePlaybackState();
        }
    }

    @Override
    public void setChannels(int i) {
        if (player != null) {
            player.setChannels(i);
        }
    }

    /**
     * 取消定时功能
     */
    @Override
    public void cancelDelay() {
        if (handler == null) return;
        if (handler.hasMessages(AudioPlayerConstant.ACITION_AUDIO_DELAY_CLOSE)) {
            handler.removeMessages(AudioPlayerConstant.ACITION_AUDIO_DELAY_CLOSE);
        }
    }

    /**
     * 定时关闭功能
     *
     * @param delayTime :延时分钟
     */
    public void delayClose(int delayTime) {
        //取消之前的定时功能
        cancelDelay();
        //将分钟转换成秒
        delayTime = delayTime * 60 * 1000;
        if (handler != null) {
            handler.sendEmptyMessageDelayed(AudioPlayerConstant.ACITION_AUDIO_DELAY_CLOSE, delayTime);
        }
    }

    @Override
    public void release() {
        removeCallBack();
        if (player != null) {
            if (playThread != null) {
                playThread.interrupt();
                playThread = null;
            }
            Log.d(TAG, "release: 音乐正在播放，准备停止");
            cancelDelay();
//            player.release();
            sessionManager.release();
            status = 0;
//            audio = null;
            musicController = null;
        }
    }
}
