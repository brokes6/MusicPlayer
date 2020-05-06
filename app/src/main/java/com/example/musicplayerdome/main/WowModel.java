package com.example.musicplayerdome.main;


import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.TopListBean;

import io.reactivex.Observable;

public class WowModel implements WowContract.Model {
    @Override
    public Observable<BannerBean> getBanner() {
        return ApiEngine.getInstance().getApiService().getBanner("2");
    }

    @Override
    public Observable<MainRecommendPlayListBean> getRecommendPlayList() {
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
}
