package com.example.musicplayerdome.main.other;

import com.example.musicplayerdome.abstractclass.EventContract;
import com.example.musicplayerdome.abstractclass.RecommendedContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;

import io.reactivex.Observable;

public class RecommendedModel implements RecommendedContract.Model{
    @Override
    public Observable<RecommendedVideoBean> getRecommendedVideos() {
        return ApiEngine.getInstance().getApiService().getRecommendedVideos();
    }
}
