package com.example.musicplayerdome.main.view;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.databinding.ActivityRankBinding;
import com.example.musicplayerdome.main.adapter.RankAdapter;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.other.WowPresenter;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;

/**
 * HomeFragment.RankActivity（HomeFragment页面 5个按钮之一）
 * 展示歌曲排行榜
 */
public class RankActivity extends BaseActivity<WowPresenter> implements WowContract.View {
    private static final String TAG = "RankActivity";
    ActivityRankBinding binding;
    private List<TopListBean.ListBean> list = new ArrayList<>();
    private RankAdapter adapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rank);

        ImmersionBar.with(this)
                .statusBarColor(R.color.red)
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    protected WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initData() {
        list.clear();
        adapter = new RankAdapter(this);
        adapter.setListener(listener);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        binding.rvToplist.setLayoutManager(manager);
        binding.rvToplist.setAdapter(adapter);
        showDialog();
        mPresenter.getTopList();
    }

    @Override
    protected void initView() {
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleText(getString(R.string.rank), getString(R.string.colorWhite));
        setMargins(binding.rlTitle,0,getStatusBarHeight(this),0,0);
    }

    private RankAdapter.OnTopListClickListener listener = position -> {
        if (list != null || !list.isEmpty()) {
            //进入歌单详情页面
            Intent intent = new Intent(RankActivity.this, SongSheetActivityMusic.class);
            TopListBean.ListBean bean = list.get(position);
            String playlistName = bean.getName();
            intent.putExtra(PLAYLIST_NAME, playlistName);
            String playlistPicUrl = bean.getCoverImgUrl();
            intent.putExtra(PLAYLIST_PICURL, playlistPicUrl);
            String playlistCreatorNickname = "";
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistCreatorNickname);
            String playlistCreatorAvatarUrl = "";
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistCreatorAvatarUrl);
            long playlistId = bean.getId();
            intent.putExtra(PLAYLIST_ID, playlistId);
            startActivity(intent);
        }
    };


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SongPlayManager.getInstance().isDisplay()) {
            binding.bottomController.setVisibility(View.VISIBLE);
        } else {
            binding.bottomController.setVisibility(View.GONE);
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
    public void onGetRecommendPlayListAgainSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListAgainFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {

    }

    @Override
    public void onGetDailyRecommendFail(String e) {

    }

    @Override
    public void onGetTopListSuccess(TopListBean bean) {
        hideDialog();
        Log.d(TAG, "onGetTopListSuccess : " + bean);
        list.addAll(bean.getList());
        adapter.loadMore(list);
    }

    @Override
    public void onGetTopListFail(String e) {
        hideDialog();
        XToastUtils.warning(e);
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