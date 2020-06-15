package com.example.musicplayerdome.song.other;

import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.song.bean.CollectionMVBean;
import com.example.musicplayerdome.song.bean.MVDetailBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.SongMvDataBean;

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
                .subscribe(new Observer<SongMvDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SongMvDataBean songMvDataBean) {
                        mView.onGetgetSongMvSuccess(songMvDataBean);
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

        @Override
        public void getSongMvComment(long id) {
            mModel.getSongMvComment(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MusicCommentBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MusicCommentBean musicCommentBean) {
                            mView.onGetSongMvCommentSuccess(musicCommentBean);
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

        @Override
        public void getMVDetail(long id) {
            mModel.getMVDetail(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MVDetailBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MVDetailBean mvDetailBean) {
                            mView.onGetMVDetailSuccess(mvDetailBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetMVDetailFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public void CollectionMV(long id, int t) {
            mModel.CollectionMv(id,t).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CollectionMVBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CollectionMVBean collectionMVBean) {
                            mView.onCollectionMvSuccess(collectionMVBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onCollectionMvFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
}
