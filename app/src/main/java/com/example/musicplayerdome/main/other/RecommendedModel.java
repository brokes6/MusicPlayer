package com.example.musicplayerdome.main.other;

import com.example.musicplayerdome.abstractclass.EventContract;
import com.example.musicplayerdome.abstractclass.RecommendedContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;

import io.reactivex.Observable;

public class RecommendedModel implements RecommendedContract.Model{
    @Override
    public Observable<RecommendedVideoBean> getRecommendedVideos() {
        return ApiEngine.getInstance().getApiService().getRecommendedVideos();
    }

    @Override
    public Observable<MusicCommentBean> getVideoComment(String id) {
        return ApiEngine.getInstance().getApiService().getVideoComment(id);
    }

    @Override
    public Observable<RecommendedVideoBean> getGroupVideos(int id) {
        return ApiEngine.getInstance().getApiService().getGroupVideos(id);
    }
}
