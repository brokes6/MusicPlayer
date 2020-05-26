package com.example.musicplayerdome.song.other;



import android.util.Log;

import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SongPresenter extends SongContract.Presenter {
    private static final String TAG = "SongPresenter";

    public SongPresenter(SongContract.View view) {
        this.mView = view;
        this.mModel = new SongModel();
    }

    @Override
    public void getSongDetail(long ids) {
        mModel.getSongDetail(ids).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(SongDetailBean bean) {
                        Log.e(TAG, "onNextll :" + bean);
                        mView.onGetSongDetailSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : " + e.getLocalizedMessage());
                        mView.onGetSongDetailFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void likeMusic(long id) {
        mModel.likeMusic(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LikeMusicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(LikeMusicBean bean) {
                        mView.onLikeMusicSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError :" + e.getLocalizedMessage());
                        mView.onLikeMusicFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete ");
                    }
                });
    }

    @Override
    public void NolikeMusic(long id) {
        mModel.NolikeMusic(false,id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LikeMusicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(LikeMusicBean bean) {
                        mView.onNoLikeMusicSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError :" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getLikeList(long uid) {
        mModel.getLikeList(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LikeListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(LikeListBean bean) {
                        mView.onGetLikeListSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : " + e);
                        mView.onGetLikeListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void getMusicComment(long id) {
        mModel.getMusicComment(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicCommentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }
                    @Override
                    public void onNext(MusicCommentBean bean) {
                        mView.onGetMusicCommentSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "评论onError : " + e.getLocalizedMessage());
                        mView.onGetMusicCommentFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void likeComment(long id, long cid, int t, int type) {
        mModel.likeComment(id, cid, t, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentLikeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(CommentLikeBean bean) {
                        Log.d(TAG, "onNext :" + bean);
                        mView.onLikeCommentSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError :" + e.getLocalizedMessage());
                        mView.onLikeCommentFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void getLyric(long id) {
        mModel.getLyric(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LyricBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getLyric onSubscribe");
                    }

                    @Override
                    public void onNext(LyricBean bean) {
                        Log.e(TAG, "getLyric onNext" + bean);
                        mView.onGetLyricSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getLyric onError" + e.getLocalizedMessage());
                        mView.onGetLyricFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getLyric onComplete");
                    }
                });
    }

    @Override
    public void getPlaylistComment(long id) {
        mModel.getPlaylistComment(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayListCommentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getPlaylistComment onSubscribe ");
                    }

                    @Override
                    public void onNext(PlayListCommentBean bean) {
                        Log.d(TAG, "getPlaylistComment onNext:" + bean);
                        mView.onGetPlaylistCommentSuccess(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getPlaylistComment onError: " + e.getLocalizedMessage());
                        mView.onGetPlaylistCommentFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getPlaylistComment onComplete ");
                    }
                });
    }
}
