package com.example.musicplayerdome.activity;

import androidx.databinding.DataBindingUtil;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.DialogClickCallBack;
import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.audio.RatateImage;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.ActivityMusicBinding;
import com.example.musicplayerdome.dialog.AudioTimerDialog;
import com.example.musicplayerdome.dialog.MusicList;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.resources.DomeData;
import com.example.musicplayerdome.servlce.MusicServlce;
import com.example.musicplayerdome.song.SongPresenter;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.util.AudioFlag;
import com.example.musicplayerdome.util.AudioPlayerConstant;
import com.example.musicplayerdome.util.FilesUtil;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SPManager;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.example.musicplayerdome.util.TimerFlag;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;
import com.lauzy.freedom.library.LrcView;
import com.lzx.starrysky.model.SongInfo;
import com.smp.soundtouchandroid.AudioSpeed;
import com.smp.soundtouchandroid.MediaCallBack;
import com.smp.soundtouchandroid.OnProgressChangedListener;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.jessyan.autosize.internal.CustomAdapt;

import static com.blankj.utilcode.util.NetworkUtils.getWifiEnabled;
import static com.example.musicplayerdome.util.AudioPlayerConstant.playerState;

public class MusicActivityMusic extends BaseActivity<SongPresenter> implements View.OnClickListener, CustomAdapt, SongContract.View {
    ActivityMusicBinding binding;
    private static final String TAG = "MusicActivity";
    public static final String SONG_INFO = "songInfo";
    int lp;
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
    private final static String ACTIONS = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClickS";
    private final static String INTENT_BUTTONID_TAG = "ButtonId";
    //传递进来的数据
    private SongDetailBean songDetail;
    //结束
    /**
     * 播放/暂停 按钮点击 ID
     */
    private final static int BUTTON_PALY_ID = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //更新数据（音乐名称和别的）
                case MSG_SHOW_UI:
                    update();
                    getAlphaAnimator();
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
                SharedPreferencesUtil.putData("key",true);
                Log.d(TAG, "onServiceConnected: 服务以启动");
                MusicServlce.MediaplayerBinder binder =(MusicServlce.MediaplayerBinder)service;
                musicController = binder.getService();
                SharedPreferencesUtil.putData("go",true);
                if (musicController.getPlayList() == null || musicController.getPlayList().isEmpty()) {
                    if (audioList != null) {
                        musicController.setPlayList(audioList);
                    }
                }
                if (musicController != null) {
                    musicController.stop();
                    musicController.Choice(id-1);
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
    protected void onCreateView(Bundle savedInstanceState) {
        ImmersionBar.with(MusicActivityMusic.this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .init();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_music);
        initView();
    }
    @Override
    protected void initData() {
        getIntentData();
        goPlay();
    }
    /**
     * 初始化资源
     */
    private void initView(){
        binding.play.setOnClickListener(this);
        binding.musicList.setOnClickListener(this);
        binding.button.setOnClickListener(this);
        binding.audioTiming.setOnClickListener(this);
        binding.playAlbumIs.setOnClickListener(this);
        binding.mLrcView.setOnClickListener(this);
        binding.back.setOnClickListener(this);
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
    private SongInfo currentSongInfo;
    private int key;
    private int id;
    private void goPlay(){

        if (musicController==null){
            Log.e(TAG, "goPlay: 开始注册绑定服务");
            Wifipaly=WifiMusic();
            if (Wifipaly==false){
                setWifiplay();
                return;
            }else{
                stratSerlvce();
            }
            return;
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentSongInfo = intent.getParcelableExtra(SONG_INFO);
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
    private void initResources(SongDetailBean songDetail){
        handler.sendEmptyMessage(MSG_SHOW_UI);
        initAudioManager();
        String coverUrl = songDetail.getSongs().get(0).getAl().getPicUrl();
        Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.shape_record)
                .into(binding.playAlbumIs);
        Glide.with(this)
                .load(coverUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 12)))
                .transition(new DrawableTransitionOptions().crossFade(1500))
                .into(binding.ivBg);
        //将图片封面加载为RatateImage（旋转动画）
        ratateImage = new RatateImage(this, binding.playAlbumIs);
        SharedPreferencesUtil.putData("name",audio.getName());
        SharedPreferencesUtil.putData("author",audio.getAuthor());
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
                myDialog = new MusicList(this,lp,(int)audio.getId(),musicController.getPlayList());
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
                //上一首
            case R.id.act_audio_player_button_prebuttonId:
                if (musicController != null) {
                    musicController.pre();
                }
                if (ratateImage != null) {
                    ratateImage.initSpin();
                }
                break;
                //定时
            case R.id.audio_timing:
                showTimingPop();
                break;
                //音频歌词
            case R.id.play_album_is:
                binding.playAlbumIs.setVisibility(View.GONE);
                binding.lrcViews.setVisibility(View.VISIBLE);
                binding.PropsColumnS.setVisibility(View.GONE);
                binding.volume.setVisibility(View.VISIBLE);
                break;
            case R.id.mLrcView:
                binding.playAlbumIs.setVisibility(View.VISIBLE);
                binding.lrcViews.setVisibility(View.GONE);
                binding.PropsColumnS.setVisibility(View.VISIBLE);
                binding.volume.setVisibility(View.GONE);
                break;
            case R.id.back:
                ActivityUtils.startActivity(SongSheetActivityMusic.class);
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
        connect = (boolean)SharedPreferencesUtil.getData("key",false);
        SharedPreferencesUtil.putData("go",false);
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
        SharedPreferencesUtil.putData("key",false);
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
                    int timerState = SPManager.getTimerState(MusicActivityMusic.this);
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
     * 音乐歌曲初始化
     */
    private void setMusicLrcView(Audio audio){
        String name = audio.getName();
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC)+"/"+name+".lrc");
        if(FilesUtil.fileIsExists(file)==false){
            Log.e(TAG, "音乐歌曲初始化: 未发现lrc文件");
            String url = audio.getLrcurl();
            binding.mLrcView.setEmptyContent("暂时没有歌词~");
//            FilesUtil.downloadFile(file,url);
            return;
        }
        List<Lrc> Lyric = LrcHelper.parseLrcFromFile(file);
        //设置歌词数据：
        binding.mLrcView.setLrcData(Lyric);
        binding.mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                musicController.seekTo(((int) time * 1000));
            }
        });
    }
    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {
        songDetail = bean;
        initResources(songDetail);
    }

    @Override
    public void onGetSongDetailFail(String e) {

    }

    @Override
    public void onLikeMusicSuccess(LikeMusicBean bean) {

    }

    @Override
    public void onNoLikeMusicSuccess(LikeMusicBean bean) {

    }

    @Override
    public void onLikeMusicFail(String e) {

    }

    @Override
    public void onGetLikeListSuccess(LikeListBean bean) {

    }

    @Override
    public void onGetLikeListFail(String e) {

    }

    @Override
    public void onGetMusicCommentSuccess(MusicCommentBean bean) {

    }

    @Override
    public void onGetMusicCommentFail(String e) {

    }

    @Override
    public void onLikeCommentSuccess(CommentLikeBean bean) {

    }

    @Override
    public void onLikeCommentFail(String e) {

    }

    @Override
    public void onGetLyricSuccess(LyricBean bean) {

    }

    @Override
    public void onGetLyricFail(String e) {

    }

    @Override
    public void onGetPlaylistCommentSuccess(PlayListCommentBean bean) {

    }

    @Override
    public void onGetPlaylistCommentFail(String e) {

    }

    /**
     * Dialog点击回调事件（音乐内部列表选择列表监听）
     */
    private class DialogClickListener implements DialogClickCallBack {
        @Override
        public void viewClick(int viewId) {
            myDialog.cancel();
            id = viewId+1;
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
        connect = (boolean)SharedPreferencesUtil.getData("key",false);
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
        MyUtil.setText(binding.actBookDetailAuthor,audio.getAuthor());
        //设置音乐封面
        binding.playAlbumIs.setImageURL(audio.getFaceUrl());
        //设置音乐id
        id = ((int)audio.getId());
        //初始化歌曲
        setMusicLrcView(audio);
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
                Intent intent = new Intent();
                intent.setAction(ACTIONS);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
                intent.putExtra("sid",playerState);
                sendBroadcast(intent);
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
    boolean rkey = false;
//    int rid;
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
            key = intent.getIntExtra("key",0);
            id = intent.getIntExtra("id",0);
            Log.e(TAG, "goPlay: key值为："+key+";id为："+id);
            switch (key){
                case 1:skey=true;
                    break;
                case 2:rkey=true;
                    break;
            }
            Log.e(TAG, "skey为："+skey+";rkey为是:"+rkey);
            if (isHead && audio == null) {
                if (musicController != null) {
                    audio = musicController.getAudio();
                }
            }
            //为true就是主页推荐歌单传来的id
            if (rkey==true){
                if (musicController.getPlayList()!=DomeData.getRecommendMusic()){
                    musicController.cleanPlayList();
                    musicController.setPlayList(DomeData.getRecommendMusic());
                }
                musicController.Choice(id-1);
                rkey=false;
                Log.e(TAG, "rkey以为:"+rkey);
            }
            //为true就是主页歌单传来的id
            if (skey ==true){
                if (musicController.getPlayList()!=DomeData.getAudioMusic()){
                    musicController.cleanPlayList();
                    musicController.setPlayList(DomeData.getAudioMusic());
                }
                musicController.Choice(id-1);
                skey=false;
                Log.e(TAG, "skey以为:"+skey);
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
        binding.mLrcView.updateTime(musicController.getPlayedDuration() / 1000);
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

    private ObjectAnimator rotateAnimator;
    private ObjectAnimator alphaAnimator;
    private ObjectAnimator getAlphaAnimator() {
        if (alphaAnimator == null) {
            alphaAnimator = ObjectAnimator.ofFloat(binding.ivBg, "alpha", 0f, 0.13f);
            alphaAnimator.setDuration(1500);
            alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return alphaAnimator;
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


    @Override
    protected SongPresenter onCreatePresenter() {
        return new SongPresenter(this);
    }

    @Override
    protected void initModule() {

    }


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
