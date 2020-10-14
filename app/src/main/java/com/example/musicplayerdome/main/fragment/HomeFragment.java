package com.example.musicplayerdome.main.fragment;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.databinding.FragmentHomeBinding;
import com.example.musicplayerdome.main.adapter.RecommendMusicAdapter;
import com.example.musicplayerdome.main.adapter.SongListAdapter;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.other.WowPresenter;
import com.example.musicplayerdome.main.view.DailyRecommendActivity;
import com.example.musicplayerdome.main.view.PlayListRecommendActivity;
import com.example.musicplayerdome.main.view.RankActivity;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.rewrite.GlideImageLoader;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.view.SongActivity;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * HomeActivityMusic.HomeFragment（Tab之一，主页）
 * 展示轮播图，推荐歌单，推荐新歌
 */
public class HomeFragment extends BaseFragment<WowPresenter> implements WowContract.View,View.OnClickListener{
    private static final String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    SongListAdapter songListAdapter;
    RecommendMusicAdapter recommendMusicAdapter;
    public static final String PLAYLIST_NAME = "playlistName";
    public static final String PLAYLIST_PICURL = "playlistPicUrl";
    public static final String PLAYLIST_DESCRIPTION = "Description";
    public static final String PLAYLIST_CREATOR_NICKNAME = "playlistCreatorNickname";
    public static final String PLAYLIST_CREATOR_AVATARURL = "playlistCreatorAvatarUrl";
    public static final String PLAYLIST_ID = "playlistId";
    //轮播图
    List<BannerBean.BannersBean> banners = new ArrayList<>();
    List<URL> bannerImageList = new ArrayList<>();
    //推荐歌单集合
    List<MainRecommendPlayListBean.RecommendBean> recommends = new ArrayList<>();
    List<PlaylistBean> list = new ArrayList<>();
    private SongInfo msongInfo;

    public HomeFragment() {
        setFragmentTitle("主 页");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        songListAdapter = new SongListAdapter(getContext());
        songListAdapter.setType(1);
        LinearLayoutManager i = new LinearLayoutManager(getContext());
        i.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.songList.setLayoutManager(i);
        binding.songList.setHasFixedSize(true);
        binding.songList.setAdapter(songListAdapter);

        recommendMusicAdapter = new RecommendMusicAdapter(getContext());
        LinearLayoutManager i1 = new LinearLayoutManager(getContext());
        i1.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recommendMusic.setLayoutManager(i1);
        binding.recommendMusic.setAdapter(recommendMusicAdapter);
        recommendMusicAdapter.setListener(listener);

        showDialog();
        recommends.clear();
        mPresenter.getBanner();
        mPresenter.getRecommendPlayList();
        mPresenter.getRecommendsong();
    }

    @Override
    public WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    RecommendMusicAdapter.RecommendMusicItemClickListener listener = new RecommendMusicAdapter.RecommendMusicItemClickListener() {

        @Override
        public void onPlayListItemClick(int position) {
            msongInfo = new SongInfo();
            msongInfo.setSongId(String.valueOf(resultData.get(position).getSong().getId()));
            msongInfo.setSongName(resultData.get(position).getName());
            msongInfo.setSongUrl(SONG_URL + resultData.get(position).getSong().getId() + ".mp3");
            msongInfo.setArtist(resultData.get(position).getSong().getArtists().get(0).getName());
            msongInfo.setArtistId(String.valueOf(resultData.get(position).getSong().getArtists().get(0).getId()));
            msongInfo.setSongCover(resultData.get(position).getPicUrl());

            SongPlayManager.getInstance().clickASong(msongInfo);
            Intent intent = new Intent(getContext(), SongActivity.class);
            intent.putExtra(SongActivity.SONG_INFO, msongInfo);
            startActivity(intent);
        }
    };

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    protected void initView(){
        binding.hRank.setOnClickListener(this);
        binding.hDailyRecommend.setOnClickListener(this);
        binding.songSheet.setOnClickListener(this);
        binding.hRadioStation.setOnClickListener(this);
        binding.hLiveBroadcast.setOnClickListener(this);

        //设置 Header式
        binding.refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        //取消Footer
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setDisableContentWhenRefresh(true);

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: 开始刷新");
                mPresenter.getRecommendPlayListAgain();
                mPresenter.getRecommendsong();
                mPresenter.getBanner();
            }
        });
    }
    private void initBanner(List<?> imageUrls){
        binding.banner.setImageLoader(new GlideImageLoader());
        binding.banner.setImages(imageUrls);
        binding.banner.setDelayTime(4000);
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
            }
        });
        binding.banner.setClipToOutline(true);
        binding.banner.start();
    }

    @Override
    public void onGetBannerSuccess(BannerBean bean) {
        if (bannerImageList.size()>0){
            Log.e(TAG, "轮播图刷新成功");
            banners.clear();
            bannerImageList.clear();
        }
        banners.addAll(bean.getBanners());
        loadImageToList();
        initBanner(bannerImageList);
    }

    //将图片装到BannerList中
    private void loadImageToList() {
        for (int i = 0; i < banners.size(); i++) {
            try {
                URL url = new URL(banners.get(i).getPic());
                bannerImageList.add(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetBannerFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean) {
        hideDialog();
        recommends.addAll(bean.getRecommend());
        for (int i = 0; i < recommends.size(); i++) {
            PlaylistBean beanInfo = new PlaylistBean();
            beanInfo.setPlaylistName(recommends.get(i).getName());
            beanInfo.setPlaylistCoverUrl(recommends.get(i).getPicUrl());
            list.add(beanInfo);
        }
        songListAdapter.setListener(listClickListener);
        songListAdapter.loadMore(list);
    }

    private SongListAdapter.OnPlayListClickListener listClickListener = (position,imageView,textView) -> {
        if (recommends != null && !recommends.isEmpty()) {
            //进入歌单详情页面
            Intent intent = new Intent(getContext(), SongSheetActivityMusic.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    new Pair<View,String>(imageView,"testImg"),
                    new Pair<View,String>(textView,"testText"));
            MainRecommendPlayListBean.RecommendBean bean = recommends.get(position);
            intent.putExtra(PLAYLIST_NAME, bean.getName());
            intent.putExtra(PLAYLIST_DESCRIPTION, bean.getCreator().getSignature());
            intent.putExtra(PLAYLIST_PICURL, bean.getPicUrl());
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, bean.getCreator().getNickname());
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, bean.getCreator().getAvatarUrl());
            intent.putExtra(PLAYLIST_ID, bean.getId());
            startActivity(intent,options.toBundle());
        }
    };


    @Override
    public void onGetRecommendPlayListFail(String e) {
        hideDialog();
        Log.e(TAG, "onGetRecommendPlayListFail: 获取歌单错误为===》"+e );
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

    @Override
    public void onGetRecommendPlayListAgainSuccess(MainRecommendPlayListBean bean) {
        List<MainRecommendPlayListBean.RecommendBean> recommends = new ArrayList<>();
        List<PlaylistBean> list = new ArrayList<>();
        recommends.addAll(bean.getRecommend());
        for (int i = 0; i < recommends.size(); i++) {
            PlaylistBean beanInfo = new PlaylistBean();
            beanInfo.setPlaylistName(recommends.get(i).getName());
            beanInfo.setPlaylistCoverUrl(recommends.get(i).getPicUrl());
            list.add(beanInfo);
        }
        Log.e(TAG, "推荐歌单刷新成功");
        songListAdapter.refresh(list);
        binding.refreshLayout.finishRefresh(true);
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
    //获取歌单详情
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

    private List<RecommendsongBean.resultData> resultData = new ArrayList<>();
    @Override
    public void onGetRecommendsongSuccess(RecommendsongBean bean) {
        if (resultData.size()>0){
            Log.e(TAG, "新歌刷新成功");
            recommendMusicAdapter.refresh(bean.getResult());
            binding.refreshLayout.finishRefresh(true);
        }
        recommendMusicAdapter.loadMore(bean.getResult());
//        resultData.clear();
        resultData.addAll(bean.getResult());
    }

    @Override
    public void onGetRecommendsongFail(String e) {
        binding.refreshLayout.finishRefresh(true);
        XToastUtils.error("获取新歌失败，请检查网络再试");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.h_daily_recommend:
                ActivityUtils.startActivity(DailyRecommendActivity.class);
                break;
            case R.id.h_rank:
                ActivityUtils.startActivity(RankActivity.class);
                break;
            case R.id.song_sheet:
                ActivityUtils.startActivity(PlayListRecommendActivity.class);
                break;
            case R.id.h_radio_station:
            case R.id.h_live_broadcast:
                XToastUtils.info("此功能暂未制作");
                break;
        }
    }
}
