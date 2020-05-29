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

        void onGetgetYuncunAgainSuccess(YuncunReviewBean bean);

        void onGetYuncunAgainFail(String e);
    }
    interface Model extends BaseModel {
        Observable<MvSublistBean> getRecommendMV();

        Observable<YuncunReviewBean> getYuncun();

        Observable<YuncunReviewBean> getYuncunAgain();

    }
    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getRecommendMV();

        public abstract void getYuncun();

        public abstract void getYuncunAgain();
    }
}
