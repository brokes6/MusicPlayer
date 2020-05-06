package com.example.musicplayerdome.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.adapter.MainMusicAdapter;
import com.example.musicplayerdome.adapter.MySongListAdapter;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.databinding.SongSheetBinding;
import com.example.musicplayerdome.base.MusicBaseActivity;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.presenter.WowPresenter;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.lzx.starrysky.model.SongInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.example.musicplayerdome.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.fragment.HomeFragment.PLAYLIST_PICURL;

public class SongSheetActivityMusic extends BaseActivity<WowPresenter> implements View.OnClickListener, WowContract.View{
    SongSheetBinding binding;
    private static final String TAG = "SongSheetActivity";
    private MainMusicAdapter mainMusicAdapter;
    private MySongListAdapter adapter;
    private Intent intent;
    private SomeBroadcastReceiver bReceiver;
    private List<PlaylistDetailBean.PlaylistBean.TracksBean> beanList = new ArrayList<>();
    private List<SongInfo> songInfos = new ArrayList<>();
    private long playlistId;
    private int Sid;
    private boolean go = false;
    /**
     * 上一首 按钮点击 ID
     */
    private final static int BUTTON_PREV_ID = 1;
    /**
     * 播放/暂停 按钮点击 ID
     */
    private final static int BUTTON_PALY_ID = 2;
    /**
     * 下一首 按钮点击 ID
     */
    private final static int BUTTON_NEXT_ID = 3;
    private final static String INTENT_BUTTONID_TAG = "ButtonId";
    private final static String ACTION_BUTTON = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClick";
    private final static String ACTIONS = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClickS";
    int deltaDistance;
    int minDistance;
    private String creatorUrl;
    private ObjectAnimator alphaAnimator;
    private ObjectAnimator coverAlphaAnimator;
    private String playlistName;
    private String playlistPicUrl;
    private String creatorName;
    //计算完成后发送的Handler msg
    public static final int COMPLETED = 0;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.A3A3)
                .init();
        binding = DataBindingUtil.setContentView(this,R.layout.song_sheet);
    }


    private void initView(){
        binding.Pback.setOnClickListener(this);
        binding.btnCustomPlay.setOnClickListener(this);
        binding.btnCustomNext.setOnClickListener(this);
        binding.btnCustomPrev.setOnClickListener(this);
        binding.PlaybackController.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Pback:
                finish();
                break;
            case R.id.btn_custom_prev:
                //上一首
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
                sendBroadcast(intent);
                break;
            case R.id.btn_custom_play:
                //播放
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
                sendBroadcast(intent);
                break;
            case R.id.btn_custom_next:
                //下一首
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
                sendBroadcast(intent);
                break;
            case R.id.Playback_controller:
                ActivityUtils.startActivity(MusicActivityMusic.class);
                break;
        }
    }

    private void initBroadcastReceiver(){
        bReceiver = new SomeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTIONS);
        registerReceiver(bReceiver, intentFilter);
    }

    /**
     * 显示音频标题
     */
    private void addAudioTitle(String name,String author,String img) {
        if (name == null) return;
        //设置音乐名称
        binding.tvCustomSongSinger.setText(name);
        MyUtil.setText(binding.tvCustomSongAuthor,author);
        binding.Mimg.setImageURL(img);
    }
    private void setImg(ImageView imageView, int imgRes) {
        if (imageView == null) return;
        imageView.setImageResource(imgRes);
    }

    
    @Override
    protected void onResume() {
        Log.e(TAG, "onResume: 运行？"+go);
        super.onResume();
        if (go == true)return;
        go = (boolean) SharedPreferencesUtil.getData("go",false);
        Log.e(TAG, "获取成功"+go);
        if (go ==true){
            String name = (String)SharedPreferencesUtil.getData("Mname","");
            String author = (String)SharedPreferencesUtil.getData("Mauthor","");
            String img = (String)SharedPreferencesUtil.getData("Mimg","");
            addAudioTitle(name,author,img);
            binding.PlaybackController.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetBannerSuccess(BannerBean bean) {

    }

    @Override
    public void onGetBannerFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {

    }

    @Override
    public void onGetDailyRecommendFail(String e) {

    }

    @Override
    public void onGetTopListSuccess(TopListBean bean) {

    }

    @Override
    public void onGetTopListFail(String e) {

    }

    @Override
    public void onGetPlayListSuccess(RecommendPlayListBean bean) {

    }

    @Override
    public void onGetPlayListFail(String e) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetPlaylistDetailSuccess(PlaylistDetailBean bean) {
        hideDialog();
        Log.d(TAG, "onGetPlaylistDetailSuccess : " + bean);
        if (!TextUtils.isEmpty(creatorUrl)) {
            Glide.with(this).load(bean.getPlaylist().getCreator().getAvatarUrl()).into(binding.userImg);
        }
        beanList.addAll(bean.getPlaylist().getTracks());
        songInfos.clear();
        for (int i = 0; i < beanList.size(); i++) {
            SongInfo beanInfo = new SongInfo();
            beanInfo.setArtist(beanList.get(i).getAr().get(0).getName());
            beanInfo.setSongName(beanList.get(i).getName());
            beanInfo.setSongCover(beanList.get(i).getAl().getPicUrl());
            beanInfo.setSongId(String.valueOf(beanList.get(i).getId()));
            beanInfo.setDuration(beanList.get(i).getDt());
            beanInfo.setSongUrl(SONG_URL + beanList.get(i).getId() + ".mp3");
            beanInfo.setArtistId(String.valueOf(beanList.get(i).getAr().get(0).getId()));
            beanInfo.setArtistKey(beanList.get(i).getAl().getPicUrl());
            songInfos.add(beanInfo);
        }
        adapter.loadMore(songInfos);

    }

    @Override
    public void onGetPlaylistDetailFail(String e) {
        hideDialog();
    }

    @Override
    public void onGetMusicCanPlaySuccess(MusicCanPlayBean bean) {

    }

    @Override
    public void onGetMusicCanPlayFail(String e) {

    }

    @Override
    public void onGetHighQualitySuccess(HighQualityPlayListBean bean) {

    }

    @Override
    public void onGetHighQualityFail(String e) {

    }

    public class SomeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTIONS)) {
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                Sid = intent.getIntExtra("sid",0);
                switch (buttonId) {
                    case 1:
                    case 4:
                    case 5:
                        String name = (String)SharedPreferencesUtil.getData("Mname","");
                        String author = (String)SharedPreferencesUtil.getData("Mauthor","");
                        String img = (String)SharedPreferencesUtil.getData("Mimg","");
                        addAudioTitle(name,author,img);
                        break;
                    case 2://播放或暂停
                        setImg(binding.btnCustomPlay,R.mipmap.audio_state_play);
                        break;
                    case 3:
                        setImg(binding.btnCustomPlay,R.mipmap.audio_state_pause);
                        break;
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: 歌单页面广播已注销");
        unregisterReceiver(bReceiver);
        super.onDestroy();
    }


    @Override
    protected WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initModule() {

    }

    @Override
    protected void initData() {
        initView();
        initBroadcastReceiver();
        LinearLayoutManager lm = new LinearLayoutManager(SongSheetActivityMusic.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

//        binding.recyclerView.setAdapter(mainMusicAdapter = new MainMusicAdapter(SongSheetActivityMusic.this));
//        /**
//         * 回调监听也行，但没必要，只需要在加个参数用来判断就行了
//         * 通过抽象类来回调监听
//         * 这边才是真正的方法
//         */
//        mainMusicAdapter.setOnItemClickListener(new OnItemListenter() {
//            @Override
//            public void onItemClick(View view, int postionid) {
//                Intent intent = new Intent(SongSheetActivityMusic.this, MusicActivityMusic.class);
//                intent.putExtra ("id",postionid);
//                intent.putExtra ("key",1);
//                startActivity(intent);
//            }
//        });
//        mainMusicAdapter.loadMore(DomeData.getAudioMusic());
        adapter = new MySongListAdapter(this);
        adapter.setType(2);
        binding.recyclerView.setLayoutManager(lm);
        binding.recyclerView.setAdapter(adapter);

        if (getIntent() != null) {
            playlistPicUrl = getIntent().getStringExtra(PLAYLIST_PICURL);
            Glide.with(this).load(playlistPicUrl).into(binding.XLogin);
            playlistName = getIntent().getStringExtra(PLAYLIST_NAME);
            binding.XTitle.setText(playlistName);
            creatorName = getIntent().getStringExtra(PLAYLIST_CREATOR_NICKNAME);
            binding.tvPlaylistName.setText(creatorName);
            creatorUrl = getIntent().getStringExtra(PLAYLIST_CREATOR_AVATARURL);
            Glide.with(this).load(creatorUrl).into(binding.userImg);
            playlistId = getIntent().getLongExtra(PLAYLIST_ID, 0);
            showDialog();
            Log.d(TAG, "playlistId : " + playlistId);
            mPresenter.getPlaylistDetail(playlistId);
        }
    }
}
