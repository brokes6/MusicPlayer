package com.example.musicplayerdome.main.other;


import android.util.Log;

import com.example.musicplayerdome.abstractclass.MainContract;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.bean.LogoutBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends MainContract.Presenter {
    private static final String TAG = "MainPresenter";

    public MainPresenter(MainContract.View view) {
        this.mView = view;
        this.mModel = new MainModel();
    }

    @Override
    public void logout() {
        mModel.logout().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LogoutBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(LogoutBean logoutBean) {
                        mView.onLogoutSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "退出error : " + e.toString());
                        mView.onLogoutFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete!");
                    }
                });
    }

    @Override
    public void getLikeList(long uid) {
        mModel.getLikeList(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LikeListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(LikeListBean bean) {
                        mView.onGetLikeListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "获取喜欢列表onError : " + e);
                        mView.onGetLikeListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }
}
