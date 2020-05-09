package com.example.musicplayerdome.abstractclass;


import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.bean.LogoutBean;

import io.reactivex.Observable;


public interface MainContract {
    interface View extends BaseView {
        void onLogoutSuccess();

        void onLogoutFail(String e);

        void onGetLikeListSuccess(LikeListBean bean);

        void onGetLikeListFail(String e);
    }

    interface Model extends BaseModel {
        Observable<LogoutBean> logout();

        Observable<LikeListBean> getLikeList(long uid);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void logout();

        public abstract void getLikeList(long uid);
    }
}
