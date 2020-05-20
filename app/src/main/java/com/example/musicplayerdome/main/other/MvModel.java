package com.example.musicplayerdome.main.other;

import com.example.musicplayerdome.abstractclass.MvContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.MvSublistBean;

import io.reactivex.Observable;

public class MvModel implements MvContract.Model{
    @Override
    public Observable<MvSublistBean> getRecommendMV() {
        return ApiEngine.getInstance().getApiService().getRecommendMV();
    }
}
