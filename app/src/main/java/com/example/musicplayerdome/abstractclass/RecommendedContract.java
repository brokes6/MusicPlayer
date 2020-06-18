package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observable;

public interface RecommendedContract {
    interface View extends BaseView {
        void onRecommendedVideosSuccess(RecommendedVideoBean bean);

        void onRecommendedVideosFail(String e);

    }
    interface Model extends BaseModel {
        Observable<RecommendedVideoBean> getRecommendedVideos();

    }
    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getRecommendedVideos();

    }
}
