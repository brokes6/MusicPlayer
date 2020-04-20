package com.example.musicplayerdome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.DialogClickCallBack;
import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.audio.RatateImage;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.ActivityMusicBinding;
import com.example.musicplayerdome.dialog.AudioTimerDialog;
import com.example.musicplayerdome.dialog.MusicList;
import com.example.musicplayerdome.object.BaseActivity;
import com.example.musicplayerdome.resources.MusicImg;
import com.example.musicplayerdome.resources.MusicURL;
import com.example.musicplayerdome.servlce.MusicServlce;
import com.example.musicplayerdome.util.AudioFlag;
import com.example.musicplayerdome.util.AudioPlayerConstant;
import com.example.musicplayerdome.util.BitMapUtil;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SPManager;
import com.example.musicplayerdome.util.TimerFlag;
import com.example.musicplayerdome.util.XToastUtils;
import com.smp.soundtouchandroid.AudioSpeed;
import com.smp.soundtouchandroid.MediaCallBack;
import com.smp.soundtouchandroid.OnProgressChangedListener;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

import static com.blankj.utilcode.util.NetworkUtils.getWifiEnabled;
import static com.example.musicplayerdome.util.AudioPlayerConstant.playerState;

public class MusicActivity extends BaseActivity implements View.OnClickListener, CustomAdapt {
    private static final int MUSIC_LIST_ITEM = 77;
    ActivityMusicBinding binding;
    private static final String TAG = "MusicActivity";
    int lp;
    int sid;
    //旋转图片动画控件
    private RatateImage ratateImage;
    AudioManager mAudioManager;
    MusicList myDialog;
    //用于判断是否绑定成功
    private boolean connect;
    //音频播放类
    private Audio audio;
    //音乐列表
    private List<Audio> audioList = new ArrayList<>();
    private float speed = AudioSpeed.SPEED_NORMAL;
    //显示UI
    private final int MSG_SHOW_UI = 1;
    //
    private final int MSG_Img = 77;
    //准备播放
    private final int MSG_AUDIO_PREPARE = MSG_SHOW_UI + 1;
    //自动播放
    private final int MSG_AUTO_PLAY = MSG_AUDIO_PREPARE + 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //更新数据（音乐名称和别的）
                case MSG_SHOW_UI:
                    update();
                    break;
                case MSG_AUTO_PLAY:
                    autoPlay(needPlay);
                    break;
                case MSG_Img:

                    break;
            }
        }
    };
    /**
     * servlce绑定监听
     */
    private MusicController musicController;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service!=null) {
                initSharedPreferences(true);
                Log.d(TAG, "onServiceConnected: 服务以启动");
                MusicServlce.MediaplayerBinder binder =(MusicServlce.MediaplayerBinder)service;
                musicController = binder.getService();
                if (musicController.getPlayList() == null || musicController.getPlayList().isEmpty()) {
                    if (audioList != null) {
                        musicController.setPlayList(audioList);
                    }
                }
                if (musicController != null) {
                    musicController.stop();
                    musicController.Choice(sid-1);
//                    musicController.play(audio);
                }
                //这里我为了监听播放状态而改变界面ui，所以添加监听接口
                musicController.setMediaCallBack(mediaCallBack);
                //实时更新进度条监听
                musicController.setOnProgressChangedListener(progressChangedListener);
                //这个方法目前也不知道什么用
//                onPrepare();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connect = false;
            Log.d(TAG, "onServiceDisconnected: 解绑成功");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_music);
        initView();
        setMusicList();
        initResources();
    }

    /**
     * 初始化资源
     */
    private void initView(){
        goPlay();
        binding.play.setOnClickListener(this);
        binding.musicList.setOnClickListener(this);
        binding.button.setOnClickListener(this);
        binding.audioTiming.setOnClickListener(this);
        binding.actAudioPlayerButtonPrebuttonId.setOnClickListener(this);
        binding.actAudioPlayerButtonNextId.setOnClickListener(this);
        binding.actAudioPlayerAudioProgressId.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        binding.actAudioVolumeControl.setOnSeekBarChangeListener(new SeekBarChangeVolumeControl());
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        lp = (int)(display.getHeight()*0.5);
    }

    /**
     * 用来第一次传入选择歌曲id（仅限于第一次）
     * 都加入了wifi判断
     */
    private void goPlay(){
        Bundle bundle = getIntent().getExtras();
        sid = bundle.getInt("sid");
        Log.e(TAG, "goPlayTo: 接收到的id为："+sid);
        if (musicController==null){
            Log.e(TAG, "goPlay: 开始注册绑定服务");
//            stratSerlvce();
            Wifipaly=WifiMusic();
            if (Wifipaly==false){
                setWifiplay();
                return;
            }else{
                stratSerlvce();
            }
            return;
        }
        musicController.Choice(sid-1);

    }

    /**
     * 初始化音量控制器
     */
    private void initAudioManager(){
        //音量控制,初始化定义
        if (mAudioManager==null){
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
        //设置音量控制器的进度为当前音量
        binding.actAudioVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    /**
     * 初始化音乐封面和调用初始化音量控制器
     */
    private void initResources(){
        handler.sendEmptyMessage(MSG_SHOW_UI);
        initAudioManager();
        //将图片封面加载为RatateImage（旋转动画）
        ratateImage = new RatateImage(this, binding.playAlbumIs);
    }

    /**
     * 没发现什么用（暂时没用）
     */
    private void onPrepare() {
        if (musicController != null) {
            speed = musicController.getTemp();
        }
        judgeState();
        changePlayUI();
        handler.sendEmptyMessage(MSG_AUTO_PLAY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                Wifipaly=WifiMusic();
                if (Wifipaly==false){
                    setWifiplay();
                    return;
                }else{
                    stratSerlvce();
                }
                break;
            case R.id.music_list:
                myDialog = new MusicList(this,lp,sid);
                myDialog.setDialogClickCallBack(new DialogClickListener());
                myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                myDialog.show();
                break;
            case R.id.button:
                Stop();
                break;
            case R.id.act_audio_player_button_nextId:
                if (musicController != null) {
                    musicController.next();
                }
                if (ratateImage != null) {
                    ratateImage.initSpin();
                }
                break;
            case R.id.act_audio_player_button_prebuttonId:
                if (musicController != null) {
                    musicController.pre();
                }
                if (ratateImage != null) {
                    ratateImage.initSpin();
                }
                break;
            case R.id.audio_timing:
                showTimingPop();
                break;
        }
    }
    /**
     * 初始化的总方法，包括（初始化服务，进行播放）
     */
    private void stratSerlvce(){
        Log.d(TAG, "开始绑定");
        play();
        play(audio);
    }
    /**
     * 绑定服务，进行注册服务和绑定服务
     * 采用双服务
     */
    boolean needPlay;
    private void initMusicService(boolean needPlay) {
        this.needPlay = needPlay;
        Intent intent = new Intent(this, MusicServlce.class);
        if (!MyUtil.isServiceRunning(this, MusicServlce.class.getName())) {
            Log.d(TAG, "服务未开启,准备启动服务~");
            startService(intent);
        }
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }
    /**
     * 用来进行立马解绑的
     */
    private void Stop() {
        connect = getSharedPreferences();
        //一开始是为false，当绑定服务绑定成功时，就会成为true
        //所以这就是为什么要在注销时，要进行判断connect的值
        Log.d(TAG, "开始解绑:"+connect+"---"+serviceConnection);
        if (connect ==true) {
            if (musicController != null) {
                musicController.release();
                unbindService(serviceConnection);
            }
            Intent intent = new Intent(this, MusicServlce.class);
            //停止服务和解绑服务
            stopService(intent);
        }
        if (ratateImage != null) {
            ratateImage.initSpin();
        }
        setImg(binding.play, R.mipmap.audio_state_pause);
        initSharedPreferences(false);
    }
    /**
     * 显示定时弹窗
     */
    private AudioTimerDialog timerDialog;
    private void showTimingPop() {
        if (timerDialog == null) {
            timerDialog = new AudioTimerDialog(this, R.style.my_dialog);
            timerDialog.setOnChangeListener(new AudioTimerDialog.OnTimerListener() {
                @Override
                public void OnChange() {
                    int timerState = SPManager.getTimerState(MusicActivity.this);
                    switch (timerState) {
                        case TimerFlag.CLOSE:
                        case TimerFlag.CURRENT:
                            //取消定时功能
                            if (connect && musicController != null) {
                                musicController.cancelDelay();
                            }
                            break;
                        default:
                            //定时关闭功能
                            if (connect && musicController != null) {
                                musicController.delayClose(timerState);
                            }
                            break;
                    }
                }
            });
        }
        timerDialog.show();
    }
    /**
     * Dialog点击回调事件（音乐内部列表选择监听）
     */
    private class DialogClickListener implements DialogClickCallBack {
        @Override
        public void viewClick(int viewId) {
            // TODO Auto-generated method stub 做一些你需要做的事情
            myDialog.cancel();
            sid = viewId+1;
            musicController.Choice(viewId);
            if (ratateImage != null) {
                ratateImage.initSpin();
            }
            XToastUtils.info("正在播放第"+(viewId+1)+"首");
        }
    }
    /**
     * 注意两个play是不一样的，这个是用来初始化服务的
     */
    private void play() {
        connect = getSharedPreferences();
        if (connect==false) {
            initMusicService(true);
            return;
        }
    }
    /**
     * 显示音频标题
     */
    private void addAudioTitle() {
        if (this.audio == null) return;
        //设置音乐名称
        MyUtil.setText(binding.actBookDetailTitleId, audio.getName());
        //设置音乐封面
        binding.playAlbumIs.setImageURL(audio.getFaceUrl());
        //设置音乐id
        sid = ((int)audio.getId());
        Log.e(TAG, "onChange: 状态发生改变，当前音乐id为:"+sid);
    }


    /**
     * 这个方法用来对网络音乐资源进行封装，加上名字和id
     * 正常情况是不需要这一步的，只需要服务器返回json，我们解析为Audio对象就ok了
     */
    private List<String> fileArr = new ArrayList<>();
    private List<String> fileArrimg = new ArrayList<>();
    private void setMusicList() {
        MusicURL musicURL = new MusicURL();
        MusicImg musicImg = new MusicImg();
        fileArr = musicURL.getMusicURL();
        fileArrimg = musicImg.getMusicIMG();
        for (int i = 0; i < fileArr.size(); i++) {
            Audio audio = new Audio();
            audio.setFileUrl(fileArr.get(i));
            audio.setFaceUrl(fileArrimg.get(i));
            audio.setId(i + 1);
            audio.setType(1);
            audio.setName("音乐" + (i + 1));
            Log.e(TAG, "setUrl: 新创建一个Audio"+audio);
            //原本是 if (i == fileArr.size()-1) { 在设置音乐锁的
            if (i == fileArr.size()) {
                audio.setLock(true);
            }
            audioList.add(audio);
        }
        Log.e(TAG, "setMusicList: 当前id为："+sid);
        this.audio = audioList.get(sid-1);
    }
    /**
     * 执行播放点击事件
     */
    boolean Wifipaly = false;
    private void play(Audio audio) {
        if (audio == null) return;
        this.audio = audio;
        if (audio.getFileUrl() == null) return;
        judgeState();
        switch (playerState) {
            /**
             * 直接暂停
             * 在这里进行音频控制，但把不在这里进行ui更改
             **/
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY:
                Log.d(TAG, "---执行暂停--");
                if (musicController != null) {
                    musicController.pause();

                }
                break;
            /**
             * 直接开始
             **/
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PREPARE:
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_COMPLETE:
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_STOP:
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE:
                Log.d(TAG, "---执行播放--");
                if (musicController!=null){
                    musicController.play(audio);
                }
                break;
            /**
             * 不是播放的一个
             *1.判断是否是当前页的播放列表
             *2.是： 执行播放当前的某一个；否：将列表传给playerService 并播放当前点击的那个音频
             */
            case AudioFlag.NOT_PLAY_ITEM:
                Log.d(TAG, "---播放特定的某一首--");
                play();
                break;
        }
    }
    boolean skey = false;
    /**
     * 再次进入activity的时候回调（包括从桌面返回，别的activity返回）
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent正在回调");
        initAudioManager();
        if (intent != null) {
            Audio audio = (Audio) intent.getSerializableExtra("audio");
            boolean play = intent.getBooleanExtra("play", false);
            boolean isHead = intent.getBooleanExtra("isHead", false);
            //sid是主页传来点击item的id（也就是选择音乐的id（下标要减1））
            sid = (int)this.audio.getId();
            //skey就是来判断是否是从主页传来的intent（有许多intent，所以要来判断）
            skey = intent.getBooleanExtra("skey",false);
            Log.e(TAG, "onNewIntent: skey为："+skey);
            Log.e(TAG, "onNewIntent: sid为："+sid+" ;getid的值为"+this.audio.getId());
            if (isHead && audio == null) {
                if (musicController != null) {
                    audio = musicController.getAudio();
                }
            }
            //为true就是主页传来的id
            if (skey ==true){
                musicController.Choice(sid-1);
                skey=false;
            }
            if (audio != null) {
                Log.d(TAG, "audio=" + audio.toString());
            }
            if (audio != null) {
                if (!isSame(audio)) {
                    setAudio(audio);
                    changeToCurrent(play);
                } else {
                    judgeState();
                    changePlayUI();
                    if (playerState == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE) {
                        if (musicController != null && musicController.isPause()) {
                            setCurrentProgress(musicController.getPlayedDuration());
                        }
                    }
                }
            }
        }
    }

    /**
     * 播放进度条的初始化（总时间）
     */
    private void setCurrentProgress(long position) {
        if (binding.actAudioPlayerAudioProgressId != null) {
            binding.actAudioPlayerAudioProgressId.setProgress((int) position);
        }
        if (binding.actAudioPlayerCurrentPlayTimeId != null) {
            binding.actAudioPlayerCurrentPlayTimeId.setText(getTimeStr((int) position));
        }
    }
    private void setInitDate(long duration) {
        Log.d(TAG, "duration=" + duration);
        if (musicController == null) return;
        if (binding.actAudioPlayerAudioProgressId != null) {
            binding.actAudioPlayerAudioProgressId.setMax((int) duration);
        }
        if (binding.actAudioPlayerTotalTimeId != null) {
            binding.actAudioPlayerTotalTimeId.setText(getTimeStr((int) duration));
        }
    }

    /**
     * 获取总时长
     */
    private String getTimeStr(int time) {
        time = time / 1000000;
        int min = time / 60;//分
        int second = time % 60;
        String min1 = "";
        if (min < 10) {
            min1 = "0" + min;
        } else {
            min1 = min + "";
        }
        String second1 = "";
        if (second < 10) {
            second1 = "0" + second;
        } else {
            second1 = second + "";
        }
        return min1 + ":" + second1;
    }
    /**
     * 判断状态
     */
    private void judgeState() {
        if (audio == null) return;
        if (musicController != null && musicController.getCurrentId() == audio.getId()) {
            playerState = musicController.getState();
        } else {//不是用以个音频
            playerState = AudioFlag.NOT_PLAY_ITEM;
        }
    }
    /**
     * 改变按钮状态
     */
    private void changePlayUI() {
        Log.d(TAG, "当前播放状态=" + playerState);
        boolean isMainThread = Thread.currentThread() == Looper.getMainLooper().getThread();
        if (!isMainThread) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changePlayUIInternal();
                }
            });
        } else {
            changePlayUIInternal();
        }
    }
    /**
     * 通过统一的playerState来判断播放状态
     */
    private void changePlayUIInternal() {
        Log.d(TAG, "当前音乐状态为:"+playerState);
        switch (playerState) {
            //正在播放
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY:
                setImg(binding.play, R.mipmap.audio_state_play);
                if (ratateImage != null) {
                    ratateImage.startSpin();
                }
                break;
            //暂停/未播放
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE:
                setImg(binding.play, R.mipmap.audio_state_pause);
                if (ratateImage != null) {
                    ratateImage.stopSpin();
                }
                break;
            //播放完成
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_COMPLETE:
                setImg(binding.play, R.mipmap.audio_state_pause);
                if (ratateImage != null) {
                    ratateImage.stopSpin();
                }
                //更新进度
                if (binding.actAudioPlayerAudioProgressId != null && binding.actAudioPlayerAudioProgressId.getProgress() != binding.actAudioPlayerAudioProgressId.getMax()) {
                    binding.actAudioPlayerAudioProgressId.setProgress(binding.actAudioPlayerAudioProgressId.getMax());
                }
                break;
            //未播放/播放错误
            default:
                setImg(binding.play, R.mipmap.audio_state_pause);
                if (ratateImage != null) {
                    ratateImage.stopSpin();
                }
                //更新进度
                if (binding.actAudioPlayerAudioProgressId != null) {
                    binding.actAudioPlayerAudioProgressId.setProgress(0);
                }
                break;
        }
    }
    /**
     * 自动播放
     */
    private void autoPlay(boolean needPlay) {
        Log.e(TAG, "autoPlay:playerState=" + playerState);
        switch (playerState) {
            //正在播放，更新播放ui
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY:
                if (musicController != null && musicController.isPlaying()) {
                    setInitDate(musicController.getDuration());
                }
                break;
            //暂停状态，更新ui
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE:
                if (musicController != null && musicController.isPause()) {
                    setInitDate(musicController.getDuration());
                    setCurrentProgress(musicController.getPlayedDuration());
                }
                break;
            //未播放，执行播放
            default:
                if (needPlay) {
                    play();
                }
                break;
        }
    }

    /**
     * 检测是否为wifi播放
     */
    private void setWifiplay(){
        DialogLoader.getInstance().showConfirmDialog(this,
                getString(R.string.tip_bluetooth_permission),
                getString(R.string.lab_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkUtils.setWifiEnabled(true);
                        stratSerlvce();
                        dialog.dismiss();
                    }
                },
                getString(R.string.lab_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Stop();
                        dialog.dismiss();
                    }
                }
        );
    }

    private boolean WifiMusic(){
        return getWifiEnabled();
    }


    //对设置图片进行方便的封装
    private void setImg(ImageView imageView, int imgRes) {
        if (imageView == null) return;
        imageView.setImageResource(imgRes);
    }
    private boolean isSame(Audio audio) {
        if (audio == null) return false;
        return this.audio != null && audio.getId() == this.audio.getId();
    }
    private void setAudio(Audio audio) {
        this.audio = audio;
    }
    private void changeToCurrent(boolean needPlay) {
        //显示音频标题
        addAudioTitle();
        if (needPlay) {
            play(audio);
        } else if (musicController != null) {
            judgeState();
            autoPlay(false);
            changePlayUI();
        }
    }
    /**
     * 更新页面数据
     */
    private void update() {
        changePlayUI();
        addAudioTitle();
        binding.playAlbumIs.setImageURL(audio.getFaceUrl());
    }

    /**
     *SharedPreferences写入和读取
     */
    public void initSharedPreferences(boolean k){
        SharedPreferences sharedPreferences= getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("connect",k);
        editor.commit();
    }
    public boolean getSharedPreferences(){
        SharedPreferences sharedPreferences= getSharedPreferences("key", Context .MODE_PRIVATE);
        Boolean connect = sharedPreferences.getBoolean("connect",false);
        return connect;
    }
    /**
     * 创建的MediaCallBack音频监听器，监听播放状态而改变界面ui
     */
    MediaCallBack mediaCallBack = new MediaCallBack() {
        @Override
        public void onError() { }
        //更改界面ui的方法
        @Override
        public void onChange(int state) {
            Log.d(TAG, "播放状态发生改变，当前播放状态为："+state);
            if (state == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_PRE_AUDIO || state == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_NEXT_AUDIO || state == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_SELECT_AUDIO) {
                playerState = 0;
                if (musicController != null) {
                    setAudio(musicController.getAudio());
                    setCurrentProgress(0);
                    addAudioTitle();
                }
            } else {
                playerState = state;
            }
            changePlayUI();
        }

        @Override
        public void onPrepare() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDialog();
                }
            });
        }

        @Override
        public void onPrepared(long duration) {
            setInitDate(duration);
        }

        @Override
        public void onPlay() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                }
            });
        }

        @Override
        public void onStop() { }

        @Override
        public void onPause() { }

        @Override
        public void onComplete() { }
    };

    /**
     * 实时更新进度条
     */
    private OnProgressChangedListener progressChangedListener = new OnProgressChangedListener() {
        @Override
        public void onProgressChanged(int track, double currentPercentage, long position) {
            setCurrentProgress(position);
        }

        @Override
        public void onTrackEnd(int track) {

        }

        @Override
        public void onExceptionThrown(String string) {

        }
    };

    /**
     *skbProgress监听事件
     * 滑动监听
     */
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        //单位:s
        long progress;
        long total_time;
        boolean fromUser;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (musicController != null) {
                total_time = musicController.getDuration();
                if (seekBar.getMax() != 0) {
                    this.progress = progress * total_time / seekBar.getMax();
                }
            }
            this.fromUser = fromUser;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (fromUser) {
                Log.e("debug", "  fromUser:" + fromUser + "  progress=" + progress);
                //s—>ms
                if (musicController != null) {
                    musicController.seekTo(progress);
                }
            }
        }
    }
    /**
     *音量控制器
     * 滑动监听
     */
    class SeekBarChangeVolumeControl implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.e(TAG, "onProgressChanged: 当前音量为:"+progress);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:正在注销 ");
        Stop();
    }
//    @Override
//    public void onBackPressed() {
//        Log.e(TAG, "onBackPressed: 按下返回键");
//        //这里的（添加到后台，下次进入不用重新生成该页面）应该和（android:launchMode="singleInstance"）一起才能生效
//        moveTaskToBack(false);
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //需要改变适配尺寸的时候，在重写这两个方法
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }
    @Override
    public float getSizeInDp() {
        return 640;
    }
}
