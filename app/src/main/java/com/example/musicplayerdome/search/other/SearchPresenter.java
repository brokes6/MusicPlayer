package com.example.musicplayerdome.search.other;
import android.util.Log;

import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.PlayListSearchBean;
import com.example.musicplayerdome.search.bean.RadioSearchBean;
import com.example.musicplayerdome.search.bean.SingerSearchBean;
import com.example.musicplayerdome.search.bean.SongSearchBean;
import com.example.musicplayerdome.search.bean.SynthesisSearchBean;
import com.example.musicplayerdome.search.bean.UserSearchBean;
import com.example.musicplayerdome.search.bean.VideoDataBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends SearchContract.Presenter {
    private static final String TAG = "SearchPresenter";

    public SearchPresenter(SearchContract.View v) {
        this.mView = v;
        this.mModel = new SearchModel();
    }


    @Override
    public void getHotSearchDetail() {
        mModel.getHotSearchDetail().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotSearchDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getHotSearchDetail  onSubscribe");
                    }

                    @Override
                    public void onNext(HotSearchDetailBean hotSearchDetailBean) {
                        Log.d(TAG, "getHotSearchDetail onNext :" + hotSearchDetailBean);
                        mView.onGetHotSearchDetailSuccess(hotSearchDetailBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getHotSearchDetail  onError:" + e.toString());
                        mView.onGetHotSearchDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getHotSearchDetail  onComplete");
                    }
                });
    }

    @Override
    public void getSongSearch(String keywords, int type) {
        mModel.getSongSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSongSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(SongSearchBean bean) {
                        Log.d(TAG, "getSongSearch onNext :" + bean);
                        mView.onGetSongSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSongSearch  onError : " + e.toString());
                        mView.onGetSongSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSongSearch  onComplete");
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
    public void getSingerSearch(String keywords, int type) {
        mModel.getSingerSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingerSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSingerSearch onSubscribe ");
                    }

                    @Override
                    public void onNext(SingerSearchBean bean) {
                        Log.d(TAG, "getSingerSearch onNext : " + bean);
                        mView.onGetSingerSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSingerSearch onError: " + e.toString());
                        mView.onGetSingerSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSingerSearch onComplete ");
                    }
                });
    }

    @Override
    public void getAlbumSearch(String keywords, int type) {
        mModel.getAlbumSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getAlbumSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(AlbumSearchBean bean) {
                        Log.d(TAG, "getAlbumSearch onNext ：" + bean);
                        mView.onGetAlbumSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getAlbumSearch onError : " + e);
                        mView.onGetAlbumSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getAlbumSearch  onComplete");
                    }
                });
    }

    @Override
    public void getPlayListSearch(String keywords, int type) {
        mModel.getPlayListSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayListSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getPlayListSearch onSubscribe");
                    }

                    @Override
                    public void onNext(PlayListSearchBean bean) {
                        Log.d(TAG, "getPlayListSearch onNext : " + bean);
                        mView.onGetPlayListSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getPlayListSearch  onError :" + e);
                        mView.onGetPlayListSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getPlayListSearch onComplete");
                    }
                });
    }

    @Override
    public void getRadioSearch(String keywords, int type) {
        mModel.getRadioSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RadioSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getRadioSearch onSubscribe");
                    }

                    @Override
                    public void onNext(RadioSearchBean bean) {
                        Log.d(TAG, "getRadioSearch : " + bean);
                        mView.onGetRadioSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getRadioSearch onError : " + e.toString());
                        mView.onGetRadioSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getRadioSearch onComplete");
                    }
                });
    }

    @Override
    public void getUserSearch(String keywords, int type) {
        mModel.getUserSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getUserSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(UserSearchBean bean) {
                        Log.d(TAG, "getUserSearch onNext : " + bean);
                        mView.onGetUserSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getUserSearch  onError : " + e.toString());
                        mView.onGetUserSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getUserSearch  onComplete");
                    }
                });
    }

    @Override
    public void getSynthesisSearch(String keywords, int type) {
        mModel.getSynthesisSearch(keywords, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SynthesisSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getSynthesisSearch  onSubscribe");
                    }

                    @Override
                    public void onNext(SynthesisSearchBean bean) {
                        Log.d(TAG, "getSynthesisSearch  onNext");
                        mView.onGetSynthesisSearchSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSynthesisSearch  onError : " + e.toString());
                        mView.onGetSynthesisSearchFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getSynthesisSearch  onComplete");
                    }
                });
    }

        @Override
        public void getVideoData(String id) {
            mModel.getVideoData(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<VideoUrlBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(VideoUrlBean videoUrlBean) {
                            mView.onGetVideoDataSuccess(videoUrlBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetVideoDataFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public void getVideoComment(String id) {
            mModel.getVideoComment(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MusicCommentBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MusicCommentBean musicCommentBean) {
                            mView.onGetVideoCommentSuccess(musicCommentBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetVideoCommentFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public void getVideoDetails(String id) {
            mModel.getVideoDetails(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<VideoDataBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(VideoDataBean videoDataBean) {
                            mView.onGetVideoDetailsSuccess(videoDataBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onGetVideoDetailsFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
}
