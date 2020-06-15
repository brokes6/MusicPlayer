package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;

import io.reactivex.Observable;

public interface CollectionContract {
    interface View extends BaseView {
        void onGetArtistSublistSuccess(ArtistSublistBean bean);

        void onGetArtistSublistFail(String e);

        void onGetMvSublistSuccess(MvSublistBean bean);

        void onGetMvSublistFail(String e);

    }
    interface Model extends BaseModel {
        Observable<ArtistSublistBean> getArtistSublist();

        Observable<MvSublistBean> getMvSublist();
    }
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getArtistSublist();

        public abstract void getMvSublist();

    }
}
