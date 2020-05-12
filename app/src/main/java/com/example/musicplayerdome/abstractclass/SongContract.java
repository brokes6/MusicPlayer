package com.example.musicplayerdome.abstractclass;


import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;

import io.reactivex.Observable;

public interface SongContract {
    interface View extends BaseView {
        void onGetSongDetailSuccess(SongDetailBean bean);

        void onGetSongDetailFail(String e);

        void onLikeMusicSuccess(LikeMusicBean bean);

        void onNoLikeMusicSuccess(LikeMusicBean bean);

        void onLikeMusicFail(String e);

        void onGetLikeListSuccess(LikeListBean bean);

        void onGetLikeListFail(String e);

        void onGetMusicCommentSuccess(MusicCommentBean bean);

        void onGetMusicCommentFail(String e);

        void onLikeCommentSuccess(CommentLikeBean bean);

        void onLikeCommentFail(String e);

        void onGetLyricSuccess(LyricBean bean);

        void onGetLyricFail(String e);

        void onGetPlaylistCommentSuccess(PlayListCommentBean bean);

        void onGetPlaylistCommentFail(String e);
    }

    interface Model extends BaseModel {
        Observable<SongDetailBean> getSongDetail(long ids);

        Observable<LikeMusicBean> likeMusic(long id);

        Observable<LikeMusicBean> NolikeMusic(boolean f,long id);

        Observable<LikeListBean> getLikeList(long uid);

        Observable<MusicCommentBean> getMusicComment(long id);

        Observable<CommentLikeBean> likeComment(long id, long cid, int t, int type);

        Observable<LyricBean> getLyric(long id);

        Observable<PlayListCommentBean> getPlaylistComment(long id);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getSongDetail(long ids);

        public abstract void likeMusic(long id);

        public abstract void getLikeList(long uid);

        public abstract void NolikeMusic(long id);

        public abstract void getMusicComment(long id);

        public abstract void likeComment(long id, long cid, int t, int type);

        public abstract void getLyric(long id);

        public abstract void getPlaylistComment(long id);
    }
}
