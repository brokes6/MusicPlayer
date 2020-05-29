package com.example.musicplayerdome.main.other;


import android.util.Log;

import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MinePresenter extends MineContract.Presenter {
    private static final String TAG = "PersonalPresenter";

    public MinePresenter(MineContract.View v) {
        this.mView = v;
        mModel = new MineModel();
    }

    @Override
    public void getUserPlaylist(long uid) {
        mModel.getUserPlaylist(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserPlaylistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getUserPlaylist  onSubscribe");
                    }

                    @Override
                    public void onNext(UserPlaylistBean userPlaylistBean) {
                        Log.d(TAG, "getUserPlaylist  onNext : " + userPlaylistBean);
                        mView.onGetUserPlaylistSuccess(userPlaylistBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getUserPlaylist  onError : " + e.toString());
                        mView.onGetUserPlaylistFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getUserPlaylist  onComplete");
                    }
                });
    }

        @Override
        public void getUserPlaylistAgain(long uid) {
            mModel.getUserPlaylist(uid).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UserPlaylistBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(UserPlaylistBean userPlaylistBean) {
                            mView.onGetUserPlaylistAgainSuccess(userPlaylistBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetUserPlaylistAgainFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    @Override
    public void getUserDetail(long id) {
        mModel.getUserDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(UserDetailBean userDetailBean) {
                        Log.e(TAG, "用户信息为"+userDetailBean.getLevel()+userDetailBean.getProfile().getNickname()+userDetailBean.getProfile().getAvatarUrl());
                        mView.onGetUserDetailSuccess(userDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetUserDetailFails(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void getPlayHistoryList(long id, int type) {
        mModel.getPlayHistoryList(id,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SonghistoryBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SonghistoryBean songhistoryBean) {
                        Log.e(TAG, "onNext: 播放历史"+songhistoryBean);
                        mView.onGetPlayHistoryListSuccess(songhistoryBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "播放历史获取错误 : " + e.toString());
                        mView.onGetPlayHistoryListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void getIntelligenceList(long id, long pid) {
        mModel.getIntelligenceList(id, pid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayModeIntelligenceBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getIntelligenceList onSubscribe");
                    }

                    @Override
                    public void onNext(PlayModeIntelligenceBean bean) {
                        Log.d(TAG, "getIntelligenceList onNext :" + bean);
                        mView.onGetIntelligenceListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError :" + e.getLocalizedMessage());
                        mView.onGetIntelligenceListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getIntelligenceList onComplete");
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
                        Log.d(TAG, "getMvSublist onSubscribe");
                    }

                    @Override
                    public void onNext(MvSublistBean bean) {
                        Log.d(TAG, "getMvSublist onNext : " + bean);
                        mView.onGetMvSublistBeanSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getMvSublist onError" + e.getLocalizedMessage());
                        mView.onGetMvSublistBeanFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getMvSublist onComplete");
                    }
                });
    }

    @Override
    public void getArtistSublist() {
        mModel.getArtistSublist().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getArtistSublist onSubscribe");
                    }

                    @Override
                    public void onNext(ArtistSublistBean bean) {
                        Log.d(TAG, "getArtistSublist onNext : " + bean);
                        mView.onGetArtistSublistBeanSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getArtistSublist onError" + e.getLocalizedMessage());
                        mView.onGetArtistSublistBeanFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getArtistSublist onComplete");
                    }
                });
    }

    @Override
    public void getAlbumSublist() {
        mModel.getAlbumSublistBean().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumSublistBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getAlbumSublist onSubscribe");
                    }

                    @Override
                    public void onNext(AlbumSublistBean bean) {
                        Log.d(TAG, "getAlbumSublist onNext : " + bean);
                        mView.onGetAlbumSublistBeanSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getAlbumSublist onError" + e.getLocalizedMessage());
                        mView.onGetAlbumSublistBeanFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getAlbumSublist onComplete");
                    }
                });
    }

    @Override
    public void getMyFM() {
        mModel.getMyFM().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyFmBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getMyFM onSubscribe");
                    }

                    @Override
                    public void onNext(MyFmBean bean) {
                        Log.d(TAG, "getMyFM onNext:"+bean);
                        mView.onGetMyFMSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getMyFM onError :" + e.getLocalizedMessage());
                        mView.onGetMyFMFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getMyFM onComplete");
                    }
                });
    }
}
