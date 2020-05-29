package com.example.musicplayerdome.main.other;

import android.util.Log;

import com.example.musicplayerdome.abstractclass.MvContract;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yuncun.bean.YuncunReviewBean;

public class MvPresenter extends MvContract.Presenter{
    private static final String TAG = "MvPresenter";

    public MvPresenter(MvContract.View view) {
        this.mView = view;
        this.mModel = new MvModel();
    }

    @Override
    public void getRecommendMV() {
        mModel.getRecommendMV()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MvSublistBean mvSublistBean) {
                        mView.onGetRecommendMVSuccess(mvSublistBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "MVonError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

        @Override
        public void getYuncun() {
            mModel.getYuncun()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<YuncunReviewBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(YuncunReviewBean yuncunReviewBean) {
                            mView.onGetYuncunSuccess(yuncunReviewBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "云村热评onError: "+e.getMessage());
                            mView.onGetYuncunFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public void getYuncunAgain() {
            mModel.getYuncun()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<YuncunReviewBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(YuncunReviewBean bean) {
                            mView.onGetgetYuncunAgainSuccess(bean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetYuncunAgainFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
}
