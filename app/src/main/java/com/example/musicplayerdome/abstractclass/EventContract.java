package com.example.musicplayerdome.abstractclass;


import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.MainEventBean;

import io.reactivex.Observable;

public interface EventContract {
    interface View extends BaseView {
        void onGetMainEventSuccess(MainEventBean bean);

        void onGetMainEventFail(String e);

    }

    interface Model extends BaseModel {
        Observable<MainEventBean> getMainEvent();

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMainEvent();

    }
}
