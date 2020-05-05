package com.example.musicplayerdome.base;


import com.example.musicplayerdome.abstractclass.BaseModel;
import com.example.musicplayerdome.abstractclass.BaseView;

public class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected V mView;
    protected M mModel;


    public void unSubscribe(){
        if(mView != null){
            mView = null;
        }
    }
}
