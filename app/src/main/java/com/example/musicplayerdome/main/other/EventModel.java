package com.example.musicplayerdome.main.other;



import com.example.musicplayerdome.abstractclass.EventContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.MainEventBean;

import io.reactivex.Observable;

public class EventModel implements EventContract.Model {

    @Override
    public Observable<MainEventBean> getMainEvent() {
        return ApiEngine.getInstance().getApiService().getMainEvent();
    }

}
