package com.example.musicplayerdome.main.other;


import com.example.musicplayerdome.abstractclass.MainContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.bean.LogoutBean;

import io.reactivex.Observable;


public class MainModel implements MainContract.Model {

    @Override
    public Observable<LogoutBean> logout() {
        return ApiEngine.getInstance().getApiService().logout();
    }

    @Override
    public Observable<LikeListBean> getLikeList(long uid) {
        return ApiEngine.getInstance().getApiService().getLikeList(uid);
    }
}
