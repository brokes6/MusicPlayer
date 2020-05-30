package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observable;

public interface SongMvContract {

    interface View extends BaseView {
        void onGetgetSongMvSuccess(SongMvBean bean);

        void onGetgetSongMvFail(String e);

    }

    interface Model extends BaseModel {
        Observable<SongMvBean> getSongMv(long id);
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getSongMv(long id);

    }
}
