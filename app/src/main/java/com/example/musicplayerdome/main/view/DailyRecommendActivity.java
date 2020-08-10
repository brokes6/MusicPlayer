package com.example.musicplayerdome.main.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.song.adapter.MySongListAdapter;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.database.DailyRecommendDaoOp;
import com.example.musicplayerdome.databinding.ActivityDailyRecommendBinding;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.DRGreenDaoBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.other.WowPresenter;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.AppBarStateChangeListener;
import com.example.musicplayerdome.util.DensityUtil;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.TimeUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.lzx.starrysky.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * HomeFragment.DailyRecommendActivity（HomeFragment页面 5个按钮之一）
 * 展示每日推荐歌曲
 */
public class DailyRecommendActivity extends BaseActivity<WowPresenter> implements WowContract.View,View.OnClickListener {
    private static final String TAG = "DailyRecommendActivity";
    ActivityDailyRecommendBinding binding;
    //日推集合
    private List<DailyRecommendBean.RecommendBean> dailyList = new ArrayList<>();
    private List<SongInfo> songInfos = new ArrayList<>();
    private List<DRGreenDaoBean> greenDaoList = new ArrayList<>();
    private MySongListAdapter songAdapter;
    int deltaDistance;
    int minDistance;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_daily_recommend);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
    }


    @Override
    protected WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initData() {
        dailyList.clear();

        songAdapter = new MySongListAdapter(this);
        songAdapter.setType(1);
        binding.rvDailyrecommend.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDailyrecommend.setAdapter(songAdapter);

        long updateTime = SharePreferenceUtil.getInstance(this).getDailyUpdateTime();
        Log.d(TAG, "上次日推更新时间： " + TimeUtil.getTimeStandard(updateTime));
        //上次更新日推时间小于当天7点，则更新日推
        if (!TimeUtil.isOver7am(updateTime)) {
            DailyRecommendDaoOp.deleteAllData(this);
            showDialog();
            mPresenter.getDailyRecommend();
        } else {
            //从GreenDao里面取出日推
            greenDaoList = DailyRecommendDaoOp.queryAll(this);
            if (greenDaoList != null) {
                notifyAdapter(greenDaoList);
            } else {
                DailyRecommendDaoOp.deleteAllData(this);
                showDialog();
                mPresenter.getDailyRecommend();
            }
        }
    }

    @Override
    protected void initView() {
        setLeftTitleText(R.string.day_recommend);
        setBackBtn(getString(R.string.colorWhite));
        binding.rlPlayall.setOnClickListener(this);

        String coverUrl = GsonUtil.fromJSON(SharePreferenceUtil.getInstance(this).getUserInfo(""), LoginBean.class).getProfile().getBackgroundUrl();
        if (coverUrl != null) {
            Glide.with(this)
                    .load(coverUrl)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(binding.ivBackgroundCover);
            Glide.with(this)
                    .load(coverUrl)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(binding.ivBackground);
        }
        binding.tvDay.setText(TimeUtil.getDay(System.currentTimeMillis()));
        binding.tvMonth.setText("/" + TimeUtil.getMonth(System.currentTimeMillis()));

        setMargins(binding.toolbar,0,getStatusBarHeight(this),0,0);
        minDistance = DensityUtil.dp2px(DailyRecommendActivity.this, 85);
        deltaDistance = DensityUtil.dp2px(DailyRecommendActivity.this, 200) - minDistance;
    }

    private void notifyAdapter(List<DRGreenDaoBean> greenDaoList) {
        songInfos.clear();
        for (int i = 0; i < greenDaoList.size(); i++) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongCover(greenDaoList.get(i).getSongCover());
            songInfo.setSongName(greenDaoList.get(i).getSongName());
            songInfo.setDuration(greenDaoList.get(i).getDuration());
            songInfo.setArtist(greenDaoList.get(i).getArtist());
            songInfo.setSongId(greenDaoList.get(i).getSongId());
            songInfo.setSongUrl(greenDaoList.get(i).getSongUrl());
            songInfo.setArtistId(greenDaoList.get(i).getArtistId());
            songInfo.setArtistKey(greenDaoList.get(i).getArtistAvatar());
            songInfos.add(songInfo);
        }
        songAdapter.setList(songInfos);
        songAdapter.loadMore(songInfos);
        hideDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SongPlayManager.getInstance().isDisplay()) {
            binding.bottomController.setVisibility(View.VISIBLE);
        } else {
            binding.bottomController.setVisibility(View.GONE);
        }
        initAppBarLayoutListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_playall:
                if (songInfos != null && !songInfos.isEmpty()) {
                    SongPlayManager.getInstance().clickPlayAll(songInfos, 0);
                }
                break;
        }
    }

    //根据AppBarLayout的滑动来模糊图片
    private void initAppBarLayoutListener() {
        binding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if (state == State.COLLAPSED) {
                    setLeftTitleAlpha(255f);
                }
            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (float) (binding.rlPlay.getTop() - minDistance) / (float) deltaDistance;
                int alpha = (int) (alphaPercent * 255);
                binding.ivBackgroundCover.setImageAlpha(alpha);
                binding.tvMonth.setAlpha(alphaPercent);
                binding.tvDay.setAlpha(alphaPercent);
                binding.wenzi.setAlpha(alphaPercent);
                setLeftTitleTextColorWhite();
                if (alphaPercent < 0.2f) {
                    float leftTitleAlpha = (1.0f - alphaPercent / 0.2f);
                    setLeftTitleAlpha(leftTitleAlpha);
                } else {
                    setLeftTitleAlpha(0);
                }
            }
        });
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
    public void onGetRecommendPlayListAgainSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListAgainFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {
        SharePreferenceUtil.getInstance(this).saveDailyUpdateTime(System.currentTimeMillis());
        dailyList.addAll(bean.getRecommend());

        greenDaoList.clear();
        for (int i = 0; i < dailyList.size(); i++) {
            DRGreenDaoBean listInfo = new DRGreenDaoBean();
            listInfo.setSongId(String.valueOf(bean.getRecommend().get(i).getId()));
            listInfo.setSongName(bean.getRecommend().get(i).getName());
            listInfo.setArtist(bean.getRecommend().get(i).getArtists().get(0).getName());
            listInfo.setSongCover(bean.getRecommend().get(i).getAlbum().getPicUrl());
            listInfo.setSongUrl(SONG_URL + bean.getRecommend().get(i).getId() + ".mp3");
            listInfo.setDuration(bean.getRecommend().get(i).getDuration());
            listInfo.setArtistId(String.valueOf(bean.getRecommend().get(i).getArtists().get(0).getId()));
            listInfo.setArtistAvatar(bean.getRecommend().get(i).getArtists().get(0).getPicUrl());
            greenDaoList.add(listInfo);
        }

        //先删除GreenDao里的所有数据，再将日推存储到GreenDao
        DailyRecommendDaoOp.saveData(this, greenDaoList);

        notifyAdapter(greenDaoList);
    }

    @Override
    public void onGetDailyRecommendFail(String e) {
        hideDialog();
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

    @Override
    public void onGetPlaylistDetailSuccess(PlaylistDetailBean bean) {

    }

    @Override
    public void onGetPlaylistDetailFail(String e) {

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

    @Override
    public void onGetRecommendsongSuccess(RecommendsongBean bean) {

    }

    @Override
    public void onGetRecommendsongFail(String e) {

    }

    @Override
    public void onGetCollectionListSuccess(CollectionListBean bean) {

    }

    @Override
    public void onGetCollectionListFail(String e) {

    }

    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {

    }

    @Override
    public void onGetSongDetailFail(String e) {

    }

}
