package com.example.musicplayerdome.login;



import com.example.musicplayerdome.abstractclass.LoginContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.login.bean.LoginBean;

import io.reactivex.Observable;

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<LoginBean> login(String phone, String password) {
        return ApiEngine.getInstance().getApiService().login(phone, password);
    }
}
