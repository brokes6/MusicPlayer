package com.example.musicplayerdome.login.other;


import android.util.Log;

import com.example.musicplayerdome.abstractclass.LoginContract;
import com.example.musicplayerdome.login.bean.LoginBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginPresenter extends LoginContract.Presenter {
    private static final String TAG = "LoginPresenter";

    public LoginPresenter(LoginContract.View view) {
        this.mView = view;
        this.mModel = new LoginModel();
    }

    @Override
    public void login(String phone, String password) {
        mModel.login(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(LoginBean bean) {
                        mView.onLoginSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "登录请求错误 : 请检查api文件夹中的ApiService的BASE_URL地址是否正确，如果是使用本地服务，请确保在同一个wift（不能是手机端的热点）下");
                        mView.onLoginFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete!");
                    }
                });
    }
}
