package com.example.musicplayerdome.song.other;

import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.api.ApiEngine;
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
}
