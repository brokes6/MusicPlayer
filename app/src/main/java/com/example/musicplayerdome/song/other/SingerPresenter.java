package com.example.musicplayerdome.song.other;

import android.util.Log;

import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SingerPresenter extends SingerContract.Presenter {
    private static final String TAG = "SingerPresenter";

    public SingerPresenter(SingerContract.View v) {
        this.mView = v;
        this.mModel = new SingerModel();
    }

    @Override
    public void getSingerHotSong(long id) {
        mModel.getSingerHotSong(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerSongSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSingerHotSong  onSubscribe");
                    }

                    @Override
                    public void onNext(SingerSongSearchBean bean) {
                        Log.d(TAG, "getSingerHotSong  onNext : " + bean);
                        mView.onGetSingerHotSongSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSingerHotSong  onError:" + e.toString());
                        mView.onGetSingerHotSongFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSingerHotSong  onComplete");
                    }
                });
    }

    @Override
    public void getSingerAlbum(long id) {
        mModel.getSingerAlbum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerAblumSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSingerAlbum  onSubscribe");
                    }

                    @Override
                    public void onNext(SingerAblumSearchBean bean) {
                        Log.d(TAG, "getSingerAlbum  onNext:" + bean);
                        mView.onGetSingerAlbumSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSingerAlbum  onError :" + e);
                        mView.onGetSingerAlbumFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSingerAlbum  onComplete");
                    }
                });
    }

    @Override
    public void getFeedSearch(String keywords, int type) {
        mModel.getFeedSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getFeedSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(FeedSearchBean feedSearchBean) {
                        Log.d(TAG, "getFeedSearch  onNext : " + feedSearchBean);
                        mView.onGetFeedSearchSuccess(feedSearchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getFeedSearch  onError : " + e.toString());
                        mView.onGetFeedSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getFeedSearch  onComplete");
                    }
                });
    }

    @Override
    public void getSingerDesc(long id) {
        mModel.getSingerDesc(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerDescriptionBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSingerDesc  onSubscribe");
                    }

                    @Override
                    public void onNext(SingerDescriptionBean bean) {
                        Log.d(TAG, "getSingerDesc onNext:" + bean);
                        mView.onGetSingerDescSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSingerDesc  onError:" + e);
                        mView.onGetSingerDescFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSingerDesc  onComplete");
                    }
                });
    }

    @Override
    public void getSimiSinger(long id) {
        mModel.getSimiSinger(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimiSingerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSimiSinger  onSubscribe");
                    }

                    @Override
                    public void onNext(SimiSingerBean bean) {
                        mView.onGetSimiSingerSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSimiSinger  onError");
                        mView.onGetSimiSingerFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSimiSinger  onComplete");
                    }
                });
    }

        @Override
        public void getSongMvData(long id) {
            mModel.getSongMvData(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SongMvBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(SongMvBean songMvBean) {
                            mView.onGetSongMvDataSuccess(songMvBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetSongMvDataFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public void LoadMoreSongMvData(long id, int offset) {
            mModel.LoadMoreSongMvData(id,offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SongMvBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(SongMvBean songMvBean) {
                            mView.onLoadMoreSongMvDataSuccess(songMvBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onLoadMoreSongMvDataFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
}
