package com.example.musicplayerdome.song.view;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivitySongMvBinding;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.song.bean.SongMvDataBean;
import com.example.musicplayerdome.song.other.MvPersenter;
import com.gyf.immersionbar.ImmersionBar;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SongMvActivity extends BaseActivity<MvPersenter> implements SongMvContract.View,View.OnClickListener{
    private static final String TAG = "SongMvActivity";
    ActivitySongMvBinding binding;
    public static final String MVSONG_INFO = "mvsongInfo";
    private SongMvBean mvData;
    private int sid;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_song_mv);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
        goDialog();
        initView();
    }

    @Override
    protected MvPersenter onCreatePresenter() {
        return new MvPersenter(this);
    }

    @Override
    protected void initModule() {

    }

    @Override
    protected void initData() {
        showDialog();
        getMvIntent();
    }

    //初始化视频播放器
    private void initView(){
        //不保存播放进度
        Jzvd.SAVE_PROGRESS = false;
        //自动播放
//        binding.jzVideo.startVideo();
        //重力感应
        Jzvd.FULLSCREEN_ORIENTATION=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    }

    private void getMvIntent(){
        Intent intent = getIntent();
        sid = intent.getIntExtra("sid",-1);
        mvData = (SongMvBean) intent.getSerializableExtra(MVSONG_INFO);
        mPresenter.getSongMv(mvData.getMvs().get(sid).getId());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetgetSongMvSuccess(SongMvDataBean bean) {
        hideDialog();
        binding.jzVideo.setUp(bean.getData().getUrl(),mvData.getMvs().get(sid).getName());
        Glide.with(this).load(mvData.getMvs().get(sid).getImgurl()).into(binding.jzVideo.posterImageView);

        binding.userName.setText(mvData.getMvs().get(sid).getArtist().getName());
        binding.SMTitle.setText(mvData.getMvs().get(sid).getName());
        binding.SMNumber.setText("播放量："+mvData.getMvs().get(sid).getPlayCount());
        Glide.with(this).load(mvData.getMvs().get(sid).getArtist().getImg1v1Url()).into(binding.userImg);
    }

    @Override
    public void onGetgetSongMvFail(String e) {
        hideDialog();
        Log.e(TAG, "onGetgetSongMvFail: 错误"+e);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JzvdStd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //     Jzvd.clearSavedProgress(this, null);
        //home back
        JzvdStd.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
