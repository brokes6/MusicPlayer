package com.example.musicplayerdome.main.other;


import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;

import io.reactivex.Observable;

public class WowModel implements WowContract.Model {
    @Override
    public Observable<SongDetailBean> getSongDetailAll(String idlist) {
        return ApiEngine.getInstance().getApiService().getSongDetailAll(idlist);
    }

    @Override
    public Observable<BannerBean> getBanner() {
        return ApiEngine.getInstance().getApiService().getBanner("2");
    }

    @Override
    public Observable<MainRecommendPlayListBean> getRecommendPlayList() {
        return ApiEngine.getInstance().getApiService().getRecommendPlayList();
    }

    @Override
    public Observable<MainRecommendPlayListBean> getRecommendPlayListAgain() {
        return ApiEngine.getInstance().getApiService().getRecommendPlayList();
    }

    @Override
    public Observable<DailyRecommendBean> getDailyRecommend() {
        return ApiEngine.getInstance().getApiService().getDailyRecommend();
    }

    @Override
    public Observable<TopListBean> getTopList() {
        return ApiEngine.getInstance().getApiService().getTopList();
    }

    @Override
    public Observable<RecommendPlayListBean> getPlayList(String type, int limit) {
        return ApiEngine.getInstance().getApiService().getPlayList(type, limit);
    }

    @Override
    public Observable<PlaylistDetailBean> getPlaylistDetail(long id) {
        return ApiEngine.getInstance().getApiService().getPlaylistDetail(id);
    }

    @Override
    public Observable<MusicCanPlayBean> getMusicCanPlay(long id) {
        return ApiEngine.getInstance().getApiService().getMusicCanPlay(id);
    }

    @Override
    public Observable<HighQualityPlayListBean> getHighQuality(int limit, long before) {
        return ApiEngine.getInstance().getApiService().getHighquality(limit, before);
    }

    @Override
    public Observable<RecommendsongBean> getRecommendsong() {
        return ApiEngine.getInstance().getApiService().getRecommendsong();
    }

    @Override
    public Observable<CollectionListBean> CollectionList(int t, long id) {
        return ApiEngine.getInstance().getApiService().CollectionMusicList(t,id);
    }
}
