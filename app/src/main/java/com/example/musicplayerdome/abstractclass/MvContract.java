package com.example.musicplayerdome.abstractclass;

import io.reactivex.Observable;
import yuncun.bean.YuncunReviewBean;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.MvSublistBean;

public interface MvContract {
    interface View extends BaseView {
        void onGetRecommendMVSuccess(MvSublistBean bean);

        void onGetRecommendMVFail(String e);

        void onGetYuncunSuccess(YuncunReviewBean bean);

        void onGetYuncunFail(String e);
    }
    interface Model extends BaseModel {
        Observable<MvSublistBean> getRecommendMV();

        Observable<YuncunReviewBean> getYuncun();

    }
    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getRecommendMV();

        public abstract void getYuncun();
    }
}
