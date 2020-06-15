package com.example.musicplayerdome.collection.other;

import com.example.musicplayerdome.abstractclass.CollectionContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;

import io.reactivex.Observable;

public class CollectionModel implements CollectionContract.Model {
    @Override
    public Observable<ArtistSublistBean> getArtistSublist() {
        return ApiEngine.getInstance().getApiService().getArtistSublist();
    }

    @Override
    public Observable<MvSublistBean> getMvSublist() {
        return ApiEngine.getInstance().getApiService().getMvSublist();
    }
}
