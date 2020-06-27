package com.example.musicplayerdome.main.other;


import android.util.Log;

import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WowPresenter extends WowContract.Presenter {
    private static final String TAG = "WowPresenter";

    public WowPresenter(WowContract.View view) {
        this.mView = view;
        this.mModel = new WowModel();
    }


    @Override
    public void getSongDetailAll(String idlist) {
      mModel.getSongDetailAll(idlist).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SongDetailBean songDetailBean) {
                        mView.onGetSongDetailSuccess(songDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetSongDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void getBanner() {
        mModel.getBanner().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getBanner onSubscribe");
                    }

                    @Override
                    public void onNext(BannerBean bean) {
                        mView.onGetBannerSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetBannerFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getBanner onComplete");
                    }
                });
    }

    @Override
    public void getRecommendPlayList() {
        mModel.getRecommendPlayList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainRecommendPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getRecommendPlayList onSubscribe");
                    }

                    @Override
                    public void onNext(MainRecommendPlayListBean recommendPlayListBean) {
                        Log.d(TAG, "onNext" + recommendPlayListBean.toString());
                        mView.onGetRecommendPlayListSuccess(recommendPlayListBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "推荐歌单onError:" + e);
                        mView.onGetRecommendPlayListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getRecommendPlayList onComplete");
                    }
                });
    }

    @Override
    public void getRecommendPlayListAgain() {
        mModel.getRecommendPlayList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainRecommendPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MainRecommendPlayListBean mainRecommendPlayListBean) {
                        mView.onGetRecommendPlayListAgainSuccess(mainRecommendPlayListBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "推荐歌单onError:" + e);
                        mView.onGetRecommendPlayListAgainFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getDailyRecommend() {
        mModel.getDailyRecommend().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyRecommendBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getDailyRecommend Subscribe");
                    }

                    @Override
                    public void onNext(DailyRecommendBean bean) {
                        Log.d(TAG, "日推数据：" + bean);
                        mView.onGetDailyRecommendSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "日推onError：" + e);
                        mView.onGetDailyRecommendFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getDailyRecommend onComplete");
                    }
                });
    }

        @Override
        public void getRecommendsong() {
            mModel.getRecommendsong().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RecommendsongBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RecommendsongBean recommendsongBean) {
                            mView.onGetRecommendsongSuccess(recommendsongBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetRecommendsongFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    @Override
    public void getTopList() {
        mModel.getTopList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TopListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getTopList onSubscribe");
                    }

                    @Override
                    public void onNext(TopListBean bean) {
                        Log.d(TAG, "onNext : " + bean);
                        mView.onGetTopListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        mView.onGetTopListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getTopList onComplete");
                    }
                });
    }

    @Override
    public void getPlayList(String type, int limit) {
        mModel.getPlayList(type, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getPlayList onSubscribe");
                    }

                    @Override
                    public void onNext(RecommendPlayListBean bean) {
                        Log.d(TAG, "onNext : " + bean);
                        mView.onGetPlayListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "歌单onError : " + e);
                        mView.onGetPlayListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getPlayList onComplete");
                    }
                });
    }

    @Override
    public void getPlaylistDetail(long id) {
        mModel.getPlaylistDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlaylistDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getPlaylistDetail  onSubscribe");
                    }

                    @Override
                    public void onNext(PlaylistDetailBean playlistDetailBean) {
                        Log.d(TAG, "PlaylistDetailBean : " + playlistDetailBean);
                        mView.onGetPlaylistDetailSuccess(playlistDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "不清楚啥歌单onError : " + e);
                        mView.onGetPlaylistDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getPlaylistDetail  onComplete");
                    }
                });
    }


    @Override
    public void getMusicCanPlay(long id) {
        Log.d(TAG, "getMusicCanPlay  id = " + id);
        mModel.getMusicCanPlay(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicCanPlayBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getMusicCanPlay : onSubscribe");
                    }

                    @Override
                    public void onNext(MusicCanPlayBean musicCanPlayBean) {
                        Log.d(TAG, "getMusicCanPlay : onNext " + musicCanPlayBean);
                        mView.onGetMusicCanPlaySuccess(musicCanPlayBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getMusicCanPlay : onError " + e);
                        mView.onGetMusicCanPlayFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getMusicCanPlay : onComplete");
                    }
                });
    }

    @Override
    public void getHighQuality(int limit, long before) {
        mModel.getHighQuality(limit, before)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HighQualityPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(HighQualityPlayListBean bean) {
                        Log.d(TAG, "onNext : " + bean);
                        mView.onGetHighQualitySuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : " + e);
                        mView.onGetHighQualityFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

        @Override
        public void CollectionList(int t, long id) {
            mModel.CollectionList(t,id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CollectionListBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CollectionListBean collectionListBean) {
                            mView.onGetCollectionListSuccess(collectionListBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetCollectionListFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
}
