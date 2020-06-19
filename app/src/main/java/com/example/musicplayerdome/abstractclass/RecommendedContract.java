package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observable;
import retrofit2.http.Query;

public interface RecommendedContract {
    interface View extends BaseView {
        void onRecommendedVideosSuccess(RecommendedVideoBean bean);

        void onRecommendedVideosFail(String e);

        void onGetVideoCommentSuccess(MusicCommentBean bean);

        void onGetVideoCommentFail(String e);

        void onGetGroupVideosSuccess(RecommendedVideoBean bean);

        void onGetGroupVideosFail(String e);


    }
    interface Model extends BaseModel {
        Observable<RecommendedVideoBean> getRecommendedVideos();

        Observable<MusicCommentBean> getVideoComment(String id);

        Observable<RecommendedVideoBean> getGroupVideos(int id);


    }
    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getRecommendedVideos();

        public abstract void getVideoComment(String id);

        public abstract void getGroupVideos(int id);


    }
}
