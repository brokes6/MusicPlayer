package com.example.musicplayerdome.song.other;

import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observable;

public class MvModel implements SongMvContract.Model{

    @Override
    public Observable<SongMvBean> getSongMv(long ids) {
        return ApiEngine.getInstance().getApiService().getSongMv(ids);
    }
}
