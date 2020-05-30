package com.example.musicplayerdome.song.other;

import android.util.Log;

import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MvPersenter extends SongMvContract.Presenter{
    private static final String TAG = "MvPersenter";

    public MvPersenter(SongMvContract.View view) {
        this.mView = view;
        this.mModel = new MvModel();
    }

    @Override
    public void getSongMv(long id) {
        mModel.getSongMv(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongMvBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SongMvBean songMvBean) {
                        mView.onGetgetSongMvSuccess(songMvBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetgetSongMvFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
