package com.example.musicplayerdome.collection.other;

import com.example.musicplayerdome.abstractclass.CollectionContract;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectionPresenter extends CollectionContract.Presenter {

    public CollectionPresenter(CollectionContract.View view){
        this.mView = view;
        this.mModel = new CollectionModel();
    }

    @Override
    public void getArtistSublist() {
        mModel.getArtistSublist().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArtistSublistBean artistSublistBean) {
                        mView.onGetArtistSublistSuccess(artistSublistBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetArtistSublistFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getMvSublist() {
        mModel.getMvSublist().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MvSublistBean bean) {
                        mView.onGetMvSublistSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetMvSublistFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
