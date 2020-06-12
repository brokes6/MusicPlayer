package com.example.musicplayerdome.search.other;

import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.PlayListSearchBean;
import com.example.musicplayerdome.search.bean.RadioSearchBean;
import com.example.musicplayerdome.search.bean.SingerSearchBean;
import com.example.musicplayerdome.search.bean.SongSearchBean;
import com.example.musicplayerdome.search.bean.SynthesisSearchBean;
import com.example.musicplayerdome.search.bean.UserSearchBean;
import com.example.musicplayerdome.search.bean.VideoDataBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;

import io.reactivex.Observable;

public class SearchModel implements SearchContract.Model {
    @Override
    public Observable<HotSearchDetailBean> getHotSearchDetail() {
        return ApiEngine.getInstance().getApiService().getSearchHotDetail();
    }

    @Override
    public Observable<SongSearchBean> getSongSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getSongSearch(keywords, type);
    }

    @Override
    public Observable<FeedSearchBean> getFeedSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getFeedSearch(keywords, type);
    }

    @Override
    public Observable<SingerSearchBean> getSingerSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getSingerSearch(keywords, type);
    }

    @Override
    public Observable<AlbumSearchBean> getAlbumSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getAlbumSearch(keywords, type);
    }

    @Override
    public Observable<PlayListSearchBean> getPlayListSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getPlayListSearch(keywords, type);
    }

    @Override
    public Observable<RadioSearchBean> getRadioSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getRadioSearch(keywords, type);
    }

    @Override
    public Observable<UserSearchBean> getUserSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getUserSearch(keywords, type);
    }

    @Override
    public Observable<SynthesisSearchBean> getSynthesisSearch(String keywords, int type) {
        return ApiEngine.getInstance().getApiService().getSynthesisSearch(keywords, type);
    }

    @Override
    public Observable<VideoUrlBean> getVideoData(String id) {
        return ApiEngine.getInstance().getApiService().getVideoData(id);
    }

    @Override
    public Observable<MusicCommentBean> getVideoComment(String id) {
        return ApiEngine.getInstance().getApiService().getVideoComment(id);
    }

    @Override
    public Observable<VideoDataBean> getVideoDetails(String id) {
        return ApiEngine.getInstance().getApiService().getVideoDetails(id);
    }

}
