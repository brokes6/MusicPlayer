package com.example.musicplayerdome.main.presenter;


import android.util.Log;

import com.example.musicplayerdome.abstractclass.EventContract;
import com.example.musicplayerdome.main.EventModel;
import com.example.musicplayerdome.main.bean.MainEventBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventPresenter extends EventContract.Presenter {
    private static final String TAG = "EventPresenter";

    public EventPresenter(EventContract.View view) {
        this.mView = view;
        this.mModel = new EventModel();
    }

    @Override
    public void getMainEvent() {
        mModel.getMainEvent().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainEventBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(MainEventBean bean) {
                        Log.d(TAG, "onNext :" + bean);
                        mView.onGetMainEventSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError :" + e.getLocalizedMessage());
                        mView.onGetMainEventFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

}
