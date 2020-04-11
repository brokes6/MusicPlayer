package com.example.musicplayerdome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.DialogClickCallBack;
import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.ActivityMusicBinding;
import com.example.musicplayerdome.dialog.MusicList;
import com.example.musicplayerdome.object.BaseActivity;
import com.example.musicplayerdome.resources.MusicURL;
import com.example.musicplayerdome.servlce.MusicServlce;
import com.example.musicplayerdome.util.AudioFlag;
import com.example.musicplayerdome.util.AudioPlayerConstant;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.smp.soundtouchandroid.MediaCallBack;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.NetworkUtils.getWifiEnabled;
import static com.example.musicplayerdome.util.AudioPlayerConstant.playerState;

public class MusicActivity extends BaseActivity implements View.OnClickListener {
    ActivityMusicBinding binding;
    private static final String TAG = "MusicActivity";
    int lp;
    MusicList myDialog;
    //用于判断是否绑定成功
    private boolean connect;
    //音频播放类
    private Audio audio;
    //音乐列表
    private List<Audio> audioList = new ArrayList<>();
    private MusicController musicController;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service!=null) {
                initSharedPreferences(true);
                Log.d(TAG, "onServiceConnected: 服务以启动");
                Log.d(TAG, "onServiceConnected: 当前connect的值为:"+getSharedPreferences());
                MusicServlce.MediaplayerBinder binder =(MusicServlce.MediaplayerBinder)service;
                musicController = binder.getService();
                if (musicController.getPlayList() == null || musicController.getPlayList().isEmpty()) {
                    if (audioList != null) {
                        musicController.setPlayList(audioList);
                    }
                }
                if (musicController != null) {
                    Log.d(TAG, "play: 调用播放");
                    musicController.stop();
                    musicController.play(audio);
                }
                //这里我为了监听播放状态而改变界面ui，所以添加监听接口
                musicController.setMediaCallBack(mediaCallBack);
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
    }
    private void initView(){
        binding.play.setOnClickListener(this);
        binding.musicList.setOnClickListener(this);
        binding.button.setOnClickListener(this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        lp = (int)(display.getHeight()*0.5);
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
                myDialog = new MusicList(this,lp);
                myDialog.setDialogClickCallBack(new DialogClickListener());
                myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                myDialog.show();
                break;
            case R.id.button:
                Stop();
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
                musicController.removeCallBack();
                musicController.resume();
                unbindService(serviceConnection);
                musicController = null;
            }
            Intent intent = new Intent(this, MusicServlce.class);
            //停止服务和解绑服务
            stopService(intent);
        }
        setImg(binding.play, R.mipmap.audio_state_pause);
        initSharedPreferences(false);
    }
    /**
     * Dialog点击回调事件
     */
    private class DialogClickListener implements DialogClickCallBack {
        @Override
        public void viewClick(int viewId) {
            // TODO Auto-generated method stub 做一些你需要做的事情
            myDialog.cancel();
            musicController.Choice(viewId);
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
     * 这个方法用来对网络音乐资源进行封装，加上名字和id
     */
    private List<String> fileArr = new ArrayList<>();
    private void setMusicList() {
        MusicURL musicURL = new MusicURL();
        fileArr = musicURL.getMusicURL();
        for (int i = 0; i < fileArr.size(); i++) {
            Audio audio = new Audio();
            audio.setFileUrl(fileArr.get(i));
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
        this.audio = audioList.get(0);
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

    /**
     * 再次进入的时候回调
     * 和回调差不多，这个是指从桌面，或者别的activity页面返回回来调用的方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent正在回调");
        if (intent != null) {
            Audio audio = (Audio) intent.getSerializableExtra("audio");
            boolean play = intent.getBooleanExtra("play", false);
            boolean isHead = intent.getBooleanExtra("isHead", false);
            if (isHead && audio == null) {
                if (musicController != null) {
                    audio = musicController.getAudio();
                }
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
                }
            }
        }
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
                break;
            //暂停/未播放
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PAUSE:
                setImg(binding.play, R.mipmap.audio_state_pause);
                break;
            //播放完成
            case AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY_COMPLETE:
                setImg(binding.play, R.mipmap.audio_state_pause);
                break;
        }
    }

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
        if (needPlay) {
            play(audio);
        } else if (musicController != null) {
            judgeState();
            changePlayUI();
        }
    }


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
        public void onPrepared(long duration) { }

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:正在注销 ");
        Stop();
        finish();
    }
    @Override
    public void onBackPressed() {
        //这里的（添加到后台，下次进入不用重新生成该页面）应该和（android:launchMode="singleInstance"）一起才能生效
        moveTaskToBack(true);
    }
}
