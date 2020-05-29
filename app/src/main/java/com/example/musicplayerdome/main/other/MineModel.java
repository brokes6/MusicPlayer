package com.example.musicplayerdome.main.other;


import android.util.Log;

import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;

import io.reactivex.Observable;

import static org.greenrobot.eventbus.EventBus.TAG;

public class MineModel implements MineContract.Model {
    @Override
    public Observable<UserPlaylistBean> getUserPlaylist(long uid) {
        return ApiEngine.getInstance().getApiService().getUserPlaylist(uid);
    }

    @Override
    public Observable<UserPlaylistBean> getUserPlaylistAgain(long uid) {
        return ApiEngine.getInstance().getApiService().getUserPlaylist(uid);
    }

    @Override
    public Observable<PlayModeIntelligenceBean> getIntelligenceList(long id, long pid) {
        return ApiEngine.getInstance().getApiService().getIntelligenceList(id, pid);
    }

    @Override
    public Observable<SonghistoryBean> getPlayHistoryList(long id, int type) {
        return ApiEngine.getInstance().getApiService().getPlayHistoryList(id,type);
    }

    @Override
    public Observable<UserDetailBean> getUserDetail(long id) {
        return ApiEngine.getInstance().getApiService().getUserDetail(id);
    }

    @Override
    public Observable<MvSublistBean> getMvSublist() {
        return ApiEngine.getInstance().getApiService().getMvSublist();
    }

    @Override
    public Observable<ArtistSublistBean> getArtistSublist() {
        return ApiEngine.getInstance().getApiService().getArtistSublist();
    }

    @Override
    public Observable<AlbumSublistBean> getAlbumSublistBean() {
        return ApiEngine.getInstance().getApiService().getAlbumSublist();
    }

    @Override
    public Observable<MyFmBean> getMyFM() {
        return ApiEngine.getInstance().getApiService().getMyFm();
    }

}
