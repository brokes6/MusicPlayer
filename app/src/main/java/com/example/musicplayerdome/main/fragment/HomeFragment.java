package com.example.musicplayerdome.main.fragment;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.other.WowPresenter;
import com.example.musicplayerdome.main.view.DailyRecommendActivity;
import com.example.musicplayerdome.rewrite.GlideImageLoader;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.main.adapter.RecommendMusicAdapter;
import com.example.musicplayerdome.main.adapter.SongListAdapter;
import com.example.musicplayerdome.databinding.FragmentHomeBinding;
import com.example.musicplayerdome.resources.DomeData;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.view.SongActivity;
import com.example.musicplayerdome.yuncun.adapter.YuncunAdapter;
import com.example.musicplayerdome.yuncun.view.YuncunSongActivity;
import com.lzx.starrysky.model.SongInfo;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<WowPresenter> implements WowContract.View{
    private static final String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    SongListAdapter songListAdapter;
    RecommendMusicAdapter recommendMusicAdapter;
    public static final String PLAYLIST_NAME = "playlistName";
    public static final String PLAYLIST_PICURL = "playlistPicUrl";
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        initView();
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        recommends.clear();
        mPresenter.getBanner();
        mPresenter.getRecommendPlayList();
        mPresenter.getRecommendsong();

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

    private void initView(){
        binding.hDailyRecommend.setOnClickListener(this);
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
    private SongListAdapter.OnPlayListClickListener listClickListener = position -> {
        if (recommends != null && !recommends.isEmpty()) {
            //进入歌单详情页面
            Intent intent = new Intent(getActivity(), SongSheetActivityMusic.class);
            MainRecommendPlayListBean.RecommendBean bean = recommends.get(position);
            intent.putExtra(PLAYLIST_NAME, bean.getName());
            intent.putExtra(PLAYLIST_PICURL, bean.getPicUrl());
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, bean.getCreator().getNickname());
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, bean.getCreator().getAvatarUrl());
            intent.putExtra(PLAYLIST_ID, bean.getId());
            startActivity(intent);
        }
    };

    @Override
    public void onGetRecommendPlayListFail(String e) {
        hideDialog();
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
    //刷新歌单详情
    @Override
    public void onGetPlaylistDetailAgainSuccess(PlaylistDetailBean bean) {

    }

    @Override
    public void onGetPlaylistDetailAgainFail(String e) {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.h_daily_recommend:
                startActivity(new Intent(activity, DailyRecommendActivity.class));
                break;
        }
    }
}
