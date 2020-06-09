package com.example.musicplayerdome.search.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.ActivityVideoBinding;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.PlayListSearchBean;
import com.example.musicplayerdome.search.bean.RadioSearchBean;
import com.example.musicplayerdome.search.bean.SingerSearchBean;
import com.example.musicplayerdome.search.bean.SongSearchBean;
import com.example.musicplayerdome.search.bean.SynthesisSearchBean;
import com.example.musicplayerdome.search.bean.UserSearchBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.search.other.SearchPresenter;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class VideoActivity extends BaseActivity<SearchPresenter> implements SearchContract.View,View.OnClickListener {
    private static final String TAG = "VideoActivity";
    ActivityVideoBinding binding;
    String title,coverUrl,vid,userName;



    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
        goDialog();
        initView();
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initModule() {

    }

    private void initView(){
        //不保存播放进度
        Jzvd.SAVE_PROGRESS = false;
        //重力感应
        Jzvd.FULLSCREEN_ORIENTATION= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setMargins(binding.VTop,0,getStatusBarHeight(this),0,0);
    }

    @Override
    protected void initData() {
        showDialog();
        setBackBtn(getString(R.string.colorWhite));
        getMvIntent();
        if (SongPlayManager.getInstance().isPlaying()){
            SongPlayManager.getInstance().pauseMusic();
        }
    }

    private void getMvIntent(){
        Intent intent = getIntent();
        if (intent!=null){
            title = intent.getStringExtra("Vtitle");
            coverUrl = intent.getStringExtra("VcoverUrl");
            vid = intent.getStringExtra("Vid");
            userName = intent.getStringExtra("userName");
            setSongInfo(title,userName);
            Glide.with(this).load(coverUrl).into(binding.jzVideo.posterImageView);
            Glide.with(this)
                    .load(coverUrl)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 25)))
                    .transition(new DrawableTransitionOptions().crossFade(1500))
                    .into(binding.VImg);

            mPresenter.getVideoData(vid);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {

    }

    @Override
    public void onGetHotSearchDetailFail(String e) {

    }

    @Override
    public void onGetSongSearchSuccess(SongSearchBean bean) {

    }

    @Override
    public void onGetSongSearchFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {

    }

    @Override
    public void onGetSingerSearchFail(String e) {

    }

    @Override
    public void onGetAlbumSearchSuccess(AlbumSearchBean bean) {

    }

    @Override
    public void onGetAlbumSearchFail(String e) {

    }

    @Override
    public void onGetPlayListSearchSuccess(PlayListSearchBean bean) {

    }

    @Override
    public void onGetPlayListSearchFail(String e) {

    }

    @Override
    public void onGetRadioSearchSuccess(RadioSearchBean bean) {

    }

    @Override
    public void onGetRadioSearchFail(String e) {

    }

    @Override
    public void onGetUserSearchSuccess(UserSearchBean bean) {

    }

    @Override
    public void onGetUserSearchFail(String e) {

    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }

    private List<VideoUrlBean.urlsData> videoList = new ArrayList<>();
    @Override
    public void onGetVideoDataSuccess(VideoUrlBean bean) {
        hideDialog();
        videoList.addAll(bean.getUrls());

        binding.jzVideo.setUp(videoList.get(0).getUrl(),title);
    }

    @Override
    public void onGetVideoDataFail(String e) {

    }

//    @Override
//    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }

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
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}