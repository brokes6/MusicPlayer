package com.example.musicplayerdome.song.other;


import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observable;

public class SingerModel implements SingerContract.Model {
    @Override
    public Observable<SingerSongSearchBean> getSingerHotSong(long id) {
        return ApiEngine.getInstance().getApiService().getSingerHotSong(id);
    }

    @Override
    public Observable<SingerAblumSearchBean> getSingerAlbum(long id) {
        return ApiEngine.getInstance().getApiService().getSingerAlbum(id);
    }

    @Override
    public Observable<FeedSearchBean> getFeedSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getFeedSearch(keywords, type);
    }

    @Override
    public Observable<SingerDescriptionBean> getSingerDesc(long id) {
        return ApiEngine.getInstance().getApiService().getSingerDesc(id);
    }

    @Override
    public Observable<SimiSingerBean> getSimiSinger(long id) {
        return ApiEngine.getInstance().getApiService().getSimiSinger(id);
    }

    @Override
    public Observable<SongMvBean> getSongMvData(long id) {
        return ApiEngine.getInstance().getApiService().getSongMvData(id);
    }

    @Override
    public Observable<SongMvBean> LoadMoreSongMvData(long id, int offset) {
        return ApiEngine.getInstance().getApiService().LoadMoreSongMvData(id,offset);
    }
}
