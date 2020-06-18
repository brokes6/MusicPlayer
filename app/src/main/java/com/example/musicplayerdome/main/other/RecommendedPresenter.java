package com.example.musicplayerdome.main.other;

import com.example.musicplayerdome.abstractclass.EventContract;
import com.example.musicplayerdome.abstractclass.RecommendedContract;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecommendedPresenter extends RecommendedContract.Presenter{

    public RecommendedPresenter(RecommendedContract.View view) {
        this.mView = view;
        this.mModel = new RecommendedModel();
    }

    @Override
    public void getRecommendedVideos() {
        mModel.getRecommendedVideos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendedVideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RecommendedVideoBean recommendedVideoBean) {
                        mView.onRecommendedVideosSuccess(recommendedVideoBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onRecommendedVideosFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
