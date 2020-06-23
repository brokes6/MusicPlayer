package com.example.musicplayerdome.song.other;

import android.util.Log;

import com.example.musicplayerdome.MyApplication;
import com.example.musicplayerdome.api.ApiService;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.search.other.KeywordsEvent;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.util.AudioFocusManager;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.manager.OnPlayerEventListener;
import com.lzx.starrysky.model.SongInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 封装音乐播放器，因为StarrySkyJava自己就已经连接了服务，所以我们就不必再写服务了
 * 采用单例模式
 */
public class SongPlayManager implements AudioFocusManager.AudioFocusListener{
    private static final String TAG = "SongPlayManager";

    private String CHECK_MUSIC_URL = "check/music";

    //列表循环
    public static final int MODE_LIST_LOOP_PLAY = 0x001;
    //单曲循环
    public static final int MODE_SINGLE_LOOP_PLAY = 0x002;
    //随机播放
    public static final int MODE_RANDOM = 0x003;

    //播放模式
    private int mode;
    //播放列表
    private List<SongInfo> songList = new ArrayList<>();
    //备份播放列表
    private List<SongInfo> BFsongList = new ArrayList<>();
    //随机播放列表
    private List<SongInfo> ShufflesongList = new ArrayList<>();
    //播放到第几首歌曲
    private int currentSongIndex;
    //单例模式
    private static SongPlayManager instance;
    //维护一个哈希表， key是 SongId, value 是 isMusicCanPlay，如果一首歌已经知道它是否可以播放，就把它放在这个哈希表里面
    private HashMap<String, Boolean> musicCanPlayMap;
    //设置监听器
    private SongPlayListener songListener;
    //维护第二个哈希表，Key是SongId,value是 songDetail，如果歌曲详情已经获取，则不必再获取
    private HashMap<Long, SongDetailBean> songDetailMap;
    private boolean display = false;
    private AudioFocusManager mAudioFocusManager;

    private SongPlayManager() {
        musicCanPlayMap = new HashMap<>();
        songDetailMap = new HashMap<>();
        musicCanPlayMap.clear();
        songList.clear();
        songDetailMap.clear();
        songListener = new SongPlayListener();
        MusicManager.getInstance().addPlayerEventListener(songListener);
        mode = MODE_LIST_LOOP_PLAY;
        // 初始化音频焦点管理器
        mAudioFocusManager = new AudioFocusManager(MyApplication.getContext(), this);
    }

    public static SongPlayManager getInstance() {
        if (instance == null) {
            synchronized (SongPlayManager.class) {
                if (instance == null) {
                    instance = new SongPlayManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加一首歌曲到列表，并返回这首歌所在的位置
     */
    public int addSong(SongInfo songInfo) {
        //查重
        if (songList.contains(songInfo)) {
            for (int i = 0; i < songList.size(); i++) {
                if (songInfo.getSongId().equals(songList.get(i).getSongId())) {
                    return i;
                }
            }
            return songList.size() - 1;
        } else {
            songList.add(songInfo);
            return songList.size() - 1;
        }
    }

    /**
     * 删除一首歌
     */
    public void deleteSong(int position) {
        songList.remove(position);
    }

    /**
     * 清空列表
     */
    public void clearSongList() {
        songList.clear();
        BFsongList.clear();
        currentSongIndex = 0;
    }

    /**
     * 添加一个歌曲列表，一般是播放歌单列表时用的，进来的时候要清空一下播放列表
     */
    public void addSongList(List<SongInfo> songInfoList, int index) {
        clearSongList();
        songList.addAll(songInfoList);
        BFsongList.addAll(songList);
        if (index >= songInfoList.size()) {
            currentSongIndex = songInfoList.size() - 1;
        } else {
            currentSongIndex = index;
        }
    }

    /**
     * 添加一个歌曲列表，并播放
     */
    public void addSongListAndPlay(List<SongInfo> songInfoList, int index) {
        if (songInfoList == null || songInfoList.size() == 0) {
            Log.e(TAG, "songInfoList is null");
            return;
        }
        addSongList(songInfoList, index);
        checkMusic(songInfoList.get(index).getSongId());
    }

    /**
     * 添加一首歌并且播放
     */
    public void addSongAndPlay(SongInfo songInfo) {
        if (songInfo == null) {
            Log.e(TAG, "songInfo is null");
            return;
        }
        currentSongIndex = addSong(songInfo);
        checkMusic(songInfo.getSongId());
    }


    /**
     * 检查一首歌是否可以播放
     */
    private void checkMusic(String songId) {
        if (songId == null) {
            Log.e(TAG, "songId is null");
            return;
        }
        if (musicCanPlayMap.get(songId) == null) {
//            //如果一首歌还没有去检测它 是否可以播放，则就去做检测
//            setOnSongCanPlayListener(songId, new OnSongListener() {
//                @Override
//                public void onSongCanPlaySuccess(MusicCanPlayBean bean) {
//                    if (bean.isSuccess()) {
//                        musicCanPlayMap.put(songId, true);
//                    } else {
//                        musicCanPlayMap.put(songId, false);
//                    }
//                    playMusic(songId);
//                }
//
//                @Override
//                public void onSongCnaPlayFail(String e) {
//                    XToastUtils.error(e);
//                }
//            });
            musicCanPlayMap.put(songId, true);
            playMusic(songId);
        } else {
            //如果一首歌曲之前已经检测过了，则直接调用结果即可
            playMusic(songId);
        }
    }


    /**
     * 根据是否可以播放去 播放歌曲/弹出吐司
     */
    public void playMusic(String songId) {
        Log.d(TAG, "songId :" + songId + "music size : " + songList.size());
        display = true;
        if (musicCanPlayMap.get(songId) || judgeContainsStr(songId)) {
            //歌曲是可以播放的，直接播放,或者他是本地音乐
            MusicManager.getInstance().playMusic(songList, currentSongIndex);
            SharePreferenceUtil.getInstance(MyApplication.getContext()).saveLatestSong(songList.get(currentSongIndex));
        } else {
            //弹出Toast
            Log.e(TAG, "music can not play");
            XToastUtils.info("本歌曲不能播放，可能是没有版权Or你不是尊贵的VIP用户");
            if (mode != MODE_SINGLE_LOOP_PLAY) {
                playNextMusic();
            } else {
                EventBus.getDefault().post(new StopMusicEvent(songList.get(currentSongIndex)));
            }
        }
    }

    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    private void start(){
        if(!mAudioFocusManager.requestAudioFocus()){
            Log.e(TAG, "requestAudioFocus失败");
        }
        Log.e(TAG, "requestAudioFocus成功");
        MusicManager.getInstance().playMusic();
        //对外发送start事件
//        EventBus.getDefault().post(new AudioStartEvent());
//        Log.e(TAG, "AudioStartEvent");
    }

    /**
     * 停止播放
     */
    public void cancelPlay() {
        if (isPlaying() || isPaused()) {
            Log.d(TAG, "cancel Play");
            display = false;
            MusicManager.getInstance().stopMusic();
            //释放音频焦点
            if(mAudioFocusManager != null){
                mAudioFocusManager.abandonAudioFocus();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pauseMusic() {
        if (isPlaying()) {
            MusicManager.getInstance().pauseMusic();
            //释放音频焦点
            if(mAudioFocusManager != null){
                mAudioFocusManager.abandonAudioFocus();
            }
        }
    }

    /**
     * 恢复播放
     */
    public void playMusic() {
        if (isPaused()) {
            start();
        }
    }

    /**
     * 是否正在播放歌曲
     */
    public boolean isPlaying() {
        return MusicManager.getInstance().isPlaying();
    }

    public boolean isDisplay(){
        return display;
    }

    /**
     * 是否 当前有歌曲播放 暂停状态
     */
    public boolean isPaused() {
        return MusicManager.getInstance().isPaused();
    }

    /**
     * 当前播放器 是否是 空闲状态
     */
    public boolean isIdle() {
        return MusicManager.getInstance().isIdea();
    }

    /**
     * 播放到指定位置
     */
    public void seekTo(long progress) {
        MusicManager.getInstance().seekTo(progress);
    }

    private boolean isPausedByFocusLossTransient;
    @Override
    public void audioFocusGrant() {
        Log.e(TAG, "恢复焦点");
        MusicManager.getInstance().setVolume(1.0f);
        if(isPausedByFocusLossTransient){
            playMusic();
        }
        isPausedByFocusLossTransient = false;
    }

    @Override
    public void audioFocusLoss() {
        //失去焦点
        pauseMusic();
    }

    @Override
    public void audioFocusLossTransient() {
        //暂时失去焦点
        Log.e(TAG, "暂时失去焦点");
        pauseMusic();
        isPausedByFocusLossTransient = true;
    }

    @Override
    public void audioFocusLossDuck() {
        //瞬间失去焦点
        Log.e(TAG, "瞬间失去焦点");
        MusicManager.getInstance().setVolume(0.5f);
    }

    /**
         * 播放监听器
         */
    public class SongPlayListener implements OnPlayerEventListener {

        @Override
        public void onMusicSwitch(SongInfo songInfo) {
            Log.d(TAG, "onMusicSwitch");
        }

        @Override
        public void onPlayerStart() {
            //开始播放
            if (getNowPlayingIndex()!=currentSongIndex){
                currentSongIndex = getNowPlayingIndex();
            }
            EventBus.getDefault().post(new MusicStartEvent(songList.get(currentSongIndex)));
            Log.e(TAG, "onPlayerStart: 输出当前id"+getNowPlayingSongInfo().getSongId() +"当前index为:"+getNowPlayingIndex());
            EventBus.getDefault().postSticky(new MusicStartEvent(getNowPlayingSongInfo()));
        }

        @Override
        public void onPlayerPause() {
            Log.d(TAG, "onPlayerPause");
            EventBus.getDefault().post(new MusicPauseEvent());
        }

        @Override
        public void onPlayerStop() {
            Log.d(TAG, "onPlayerStop");
        }

        @Override
        public void onPlayCompletion(SongInfo songInfo) {
            Log.d(TAG, "songInfo : " + songInfo);
            playNextMusic();
        }

        @Override
        public void onBuffering() {
            Log.d(TAG, "onBuffering onBuffering");
        }

        @Override
        public void onError(int errorCode, String errorMsg) {
            Log.d(TAG, "onError : " + errorCode + " msg:" + errorMsg);
            XToastUtils.info("无法播放此歌曲,切换至下一首");
            playNextMusic();
        }
    }

    /**
     * 切歌，一般是从播放列表中去切换，只要更换一下index就可以了
     */
    public void switchMusic(int index) {
        if (index >= songList.size() || index < 0) {
            Log.d(TAG, "index >= songList.size");
            return;
        }
        cancelPlay();
        currentSongIndex = index;
        checkMusic(songList.get(currentSongIndex).getSongId());
    }

    /**
     * 播放下一首歌曲，根据不同的模式来
     */
    public void playNextMusic() {
        Log.d(TAG, "playNextMusic");
        cancelPlay();
        switch (mode) {
            case MODE_LIST_LOOP_PLAY:
                if (currentSongIndex < songList.size()) {
                    //列表播完了的话，继续播放，并且要把index置为0，最近听过的歌曲调成列表第一个
                    if (currentSongIndex == songList.size() - 1) {
                        currentSongIndex = 0;
                    } else {
                        currentSongIndex++;
                    }
                    checkMusic(songList.get(currentSongIndex).getSongId());
                } else {
                    Log.w(TAG, "currentSongIndex >= songListSize");
                }
                break;
            case MODE_SINGLE_LOOP_PLAY:
                playMusic(songList.get(currentSongIndex).getSongId());
                break;
            case MODE_RANDOM:
//                Random ra = new Random();
//                int random = ra.nextInt(songList.size() - 1);
//                while (random == currentSongIndex) {
//                    random = ra.nextInt(songList.size() - 1);
//                }
//                currentSongIndex = random;

                if (currentSongIndex == ShufflesongList.size() - 1) {
                    currentSongIndex = 0;
                } else {
                    currentSongIndex++;
                }

                checkMusic(ShufflesongList.get(currentSongIndex).getSongId());
                break;
        }
    }

    /**
     * 播放前一首歌曲
     */
    public void playPreMusic() {
        Log.d(TAG, "playPreMusic");
        cancelPlay();
        switch (mode) {
            case MODE_LIST_LOOP_PLAY:
                if (currentSongIndex < songList.size()) {
                    if (currentSongIndex == 0) {
                        currentSongIndex = songList.size() - 1;
                    } else {
                        currentSongIndex--;
                    }
                    checkMusic(songList.get(currentSongIndex).getSongId());
                } else {
                    Log.w(TAG, "currentSongIndex >= songListSize");
                }
                break;
            case MODE_SINGLE_LOOP_PLAY:
                playMusic(songList.get(currentSongIndex).getSongId());
                break;
            case MODE_RANDOM:
//                Random ra = new Random();
//                int random = ra.nextInt(songList.size() - 1);
//                while (random == currentSongIndex) {
//                    random = ra.nextInt(songList.size() - 1);
//                }
//                currentSongIndex = random;
                if (currentSongIndex == 0) {
                    currentSongIndex = ShufflesongList.size() - 1;
                } else {
                    currentSongIndex--;
                }

                checkMusic(ShufflesongList.get(currentSongIndex).getSongId());
                break;
        }
    }

    /**
     * 判断传入的歌曲是否正在播放
     */
    public boolean isCurMusicPlaying(String songId) {
        return MusicManager.getInstance().isCurrMusicIsPlayingMusic(songId);
    }

    /**
     * 判断传入的歌曲是否正在暂停
     */
    public boolean isCurMusicPaused(String songId) {
        return MusicManager.getInstance().isCurrMusicIsPaused(songId);
    }

    /**
     * 在Activity点击一首歌的item的时候，需要进入 听歌界面
     * 同时播放这首歌，需要在进入这首歌界面之前重置下状态
     */
    public void clickASong(SongInfo songInfo) {
        if (isPlaying()) {
            Log.d(TAG, "isPlaying");
            //当前播放的歌曲不是准备播放的歌曲，停止该歌曲
            if (!isCurMusicPlaying(songInfo.getSongId())) {
                Log.d(TAG, "!isCurMusicPlaying");
                cancelPlay();
                addSongAndPlay(songInfo);
            }
        } else if (isPaused()) {
            Log.d(TAG, "isPaused");
            //当前有歌曲正在暂停，但是并不是要选中的歌曲，则停止该歌曲
            if (!isCurMusicPaused(songInfo.getSongId())) {
                Log.d(TAG, "!isCurMusicPaused");
                cancelPlay();
                addSongAndPlay(songInfo);
            }
        } else if (isIdle()) {
            Log.d(TAG, "isIdle");
            //空闲中 则直接播放这首歌
            addSongAndPlay(songInfo);
        } else {
            Log.d(TAG, "no idle,no playing ,no paused state:" + MusicManager.getInstance().getState());
        }
    }

    /**
     * 点击一个列表全部播放，则先把正在播放的歌停掉
     */
    public void clickPlayAll(List<SongInfo> songList, int position) {
        cancelPlay();
        addSongListAndPlay(songList, position);
    }

    /**
     * 点击底部音乐控制栏的开始播放
     * 如果有歌曲正在暂停，则播放，如果是idle状态，则播放该歌曲
     */
    public void clickBottomContrllerPlay(SongInfo songInfo) {
        if (isPaused()) {
            playMusic();
        } else if (isIdle()) {
            SongPlayManager.getInstance().addSongAndPlay(songInfo);
        }
    }

    /**
     * 关于歌曲是否可以播放的OkHttp网络请求
     */
    public void setOnSongCanPlayListener(String id, OnSongListener listener) {
        Log.d(TAG, "id :" + id);
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ApiService.BASE_URL + CHECK_MUSIC_URL).newBuilder();
        urlBuilder.addQueryParameter("id", id);
        Request request = requestBuilder.url(urlBuilder.build()).build();
        Log.d(TAG, "request 请求头:" + request.toString());
        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onSongCnaPlayFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dstStr = getErrorCodeString(response);
                Log.d(TAG, "dstStr : " + dstStr);
                MusicCanPlayBean bean = GsonUtil.fromJSON(dstStr, MusicCanPlayBean.class);
                if (listener != null && bean != null) {
                    listener.onSongCanPlaySuccess(bean);
                } else {
                    listener.onSongCnaPlayFail("response is null");
                }
            }
        });
    }

    private String getErrorCodeString(Response response) {
        String res = "";
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        if (!bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            try {
                source.request(Long.MAX_VALUE); // Buffer the entire body.
            } catch (IOException e) {
                e.printStackTrace();
            }
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                } catch (UnsupportedCharsetException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            if (contentLength != 0) {
                res = buffer.clone().readString(charset);
            }
        }
        return res;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public interface OnSongListener {
        void onSongCanPlaySuccess(MusicCanPlayBean bean);

        void onSongCnaPlayFail(String e);
    }

    /**
     * 设置/获取 播放模式
     */
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode==MODE_RANDOM){
            ShuffleMusic(getCurrentSongIndex());
        }
        if (mode==MODE_LIST_LOOP_PLAY){
            songList.clear();
            songList.addAll(BFsongList);
        }
    }

    private void ShuffleMusic(int index){
        ShufflesongList = songList;
        if(index>-1 && index<ShufflesongList.size()){
            SongInfo list =  ShufflesongList.get(index);
            ShufflesongList.remove(index);
            //洗牌算法打乱除当前音乐的其它音乐顺序
            Collections.shuffle(ShufflesongList);
            //将当前播放的音乐置为第一位
            ShufflesongList.add(0, list);
        }else{
            Collections.shuffle(ShufflesongList);
        }
    }

    public SongDetailBean getSongDetail(long ids) {
        return songDetailMap.get(ids);
    }

    public void putSongDetail(SongDetailBean bean) {
        songDetailMap.put(bean.getSongs().get(0).getId(), bean);
    }

    /**
     * 获取播放进度
     */
    public long getPlayProgress() {
        if (isPlaying()) {
            return MusicManager.getInstance().getPlayingPosition();
        } else {
            return 0;
        }
    }

    public SongInfo getNowPlayingSongInfo(){
        return MusicManager.getInstance().getNowPlayingSongInfo();
    }

    public int getNowPlayingIndex(){
        return MusicManager.getInstance().getNowPlayingIndex();
    }

    /**
     * 获取歌单列表
     */
    public List<SongInfo> getSongList() {
        return songList;
    }

    /**
     * 扫描本地媒体信息
     */
    public List<SongInfo> getLocalSongInfoList() {
        return MusicManager.getInstance().querySongInfoInLocal();
    }

    /**
     * 获取当前正在播放的歌曲的index
     */
    public int getCurrentSongIndex() {
        return currentSongIndex;
    }
    /**
     * 获取当前正在播放的歌曲的id
     */
//    public long getsongID(){
//        return songInfo.getSongId();
//    }
}
