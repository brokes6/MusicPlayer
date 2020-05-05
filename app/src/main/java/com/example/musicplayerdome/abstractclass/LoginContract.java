package com.example.musicplayerdome.abstractclass;



import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.login.bean.LoginBean;

import io.reactivex.Observable;

public interface LoginContract {
    interface View extends BaseView {
        void onLoginSuccess(LoginBean bean);

        void onLoginFail(String e);
    }

    interface Model extends BaseModel {
        Observable<LoginBean> login(String phone, String password);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void login(String phone, String password);
    }
}
