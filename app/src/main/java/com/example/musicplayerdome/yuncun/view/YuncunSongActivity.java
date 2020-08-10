package com.example.musicplayerdome.yuncun.view;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivityYuncunSongBinding;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.other.MusicPauseEvent;
import com.example.musicplayerdome.song.other.MusicStartEvent;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.other.SongPresenter;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.lzx.starrysky.model.SongInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class YuncunSongActivity extends BaseActivity<SongPresenter> implements SongContract.View,View.OnClickListener{
    ActivityYuncunSongBinding binding;
    private static final String TAG = "YuncunSongActivity";
    public static final String YUNSONG_INFO = "yunsongInfo";
    private SongInfo currentSongInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicStartEvent(MusicStartEvent event) {
        Log.d(TAG, "onMusicStartEvent");
        checkMusicPlaying();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicPauseEvent(MusicPauseEvent event) {
        Log.d(TAG, "onMusicPauseEvent");
        checkMusicPlaying();
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_yuncun_song);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected SongPresenter onCreatePresenter() {
        return new SongPresenter(this);
    }

    @Override
    protected void initData() {
        showDialog();
        getIntentData();
        setBackBtn(getString(R.string.colorWhite));
        setSongDetailBean();
    }

    @Override
    protected void initView(){
        EventBus.getDefault().register(this);

        setMargins(binding.rlTitle,0,getStatusBarHeight(this),0,0);
        binding.userImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_img:
                break;
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentSongInfo = intent.getParcelableExtra(YUNSONG_INFO);
    }


    private void checkMusicPlaying() {
        if (SongPlayManager.getInstance().isPlaying()) {
            hideDialog();
            Log.e(TAG, "--music正在播放--");
        } else {
            Log.e(TAG, "--music没有播放--");
        }
    }

    private void setSongDetailBean() {
        SharedPreferencesUtil.putData("Ykey",3);
        Glide.with(this).load(currentSongInfo.getSongCover()).transition(new DrawableTransitionOptions().crossFade()).into(binding.mainImg);
        Glide.with(this).load(currentSongInfo.getAlbumCover()).transition(new DrawableTransitionOptions().crossFade()).into(binding.userImg);
        Glide.with(this).load(currentSongInfo.getSongCover()).transition(new DrawableTransitionOptions().crossFade()).into(binding.yunMusicImg);
        binding.yunName.setText("@"+currentSongInfo.getAlbumArtist());
        binding.mainText.setText(currentSongInfo.getDescription());
        binding.yunMusicName.setText(currentSongInfo.getSongName()+" -"+currentSongInfo.getArtist());
    }

    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SongPlayManager.getInstance().clearSongList();
        SongPlayManager.getInstance().pauseMusic();
        EventBus.getDefault().unregister(this);
    }
}
