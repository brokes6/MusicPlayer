package com.example.musicplayerdome.abstractclass;

import io.reactivex.Observable;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.MvSublistBean;

public interface MvContract {
    interface View extends BaseView {
        void onGetRecommendMVSuccess(MvSublistBean bean);

        void onGetRecommendMVFail(String e);
    }
    interface Model extends BaseModel {
        Observable<MvSublistBean> getRecommendMV();

    }
    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getRecommendMV();
    }
}
