package com.example.musicplayerdome.song.other;

import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.song.bean.CollectionMVBean;
import com.example.musicplayerdome.song.bean.MVDetailBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.SongMvDataBean;

import io.reactivex.Observable;

public class MvModel implements SongMvContract.Model{

    @Override
    public Observable<SongMvDataBean> getSongMv(long ids) {
        return ApiEngine.getInstance().getApiService().getSongMv(ids);
    }

    @Override
    public Observable<MusicCommentBean> getSongMvComment(long id) {
        return ApiEngine.getInstance().getApiService().getSongMvComment(id);
    }

    @Override
    public Observable<MVDetailBean> getMVDetail(long id) {
        return ApiEngine.getInstance().getApiService().getMVDetail(id);
    }

    @Override
    public Observable<CollectionMVBean> CollectionMv(long id, int t) {
        return ApiEngine.getInstance().getApiService().CollectionMV(id,t);
    }
}
