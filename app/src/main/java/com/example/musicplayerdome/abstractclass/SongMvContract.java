package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.song.bean.CollectionMVBean;
import com.example.musicplayerdome.song.bean.MVDetailBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.SongMvDataBean;

import io.reactivex.Observable;

public interface SongMvContract {

    interface View extends BaseView {
        void onGetgetSongMvSuccess(SongMvDataBean bean);

        void onGetgetSongMvFail(String e);

        void onGetSongMvCommentSuccess(MusicCommentBean bean);

        void onGetSongMvCommentFail(String e);

        void onGetMVDetailSuccess(MVDetailBean bean);

        void onGetMVDetailFail(String e);

        void onCollectionMvSuccess(CollectionMVBean bean);

        void onCollectionMvFail(String e);
    }

    interface Model extends BaseModel {
        Observable<SongMvDataBean> getSongMv(long id);

        Observable<MusicCommentBean> getSongMvComment(long id);

        Observable<MVDetailBean> getMVDetail(long id);

        Observable<CollectionMVBean> CollectionMv(long id,int t);
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getSongMv(long id);

        public abstract void getSongMvComment(long id);

        public abstract void getMVDetail(long id);

        public abstract void CollectionMV(long id,int t);
    }
}
