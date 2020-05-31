package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.song.bean.SongMvDataBean;

import io.reactivex.Observable;

public interface SongMvContract {

    interface View extends BaseView {
        void onGetgetSongMvSuccess(SongMvDataBean bean);

        void onGetgetSongMvFail(String e);

    }

    interface Model extends BaseModel {
        Observable<SongMvDataBean> getSongMv(long id);
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getSongMv(long id);

    }
}
