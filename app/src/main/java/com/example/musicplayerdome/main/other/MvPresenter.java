package com.example.musicplayerdome.main.other;

import android.util.Log;

import com.example.musicplayerdome.abstractclass.MvContract;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MvPresenter extends MvContract.Presenter{
    private static final String TAG = "WowPresenter";

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
                        Log.e(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
