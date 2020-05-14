package com.example.musicplayerdome.song.other;



import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;

import io.reactivex.Observable;

public class SongModel implements SongContract.Model {
    @Override
    public Observable<SongDetailBean> getSongDetail(long ids) {
        return ApiEngine.getInstance().getApiService().getSongDetail(ids);
    }

    @Override
    public Observable<LikeMusicBean> likeMusic(long id) {
        return ApiEngine.getInstance().getApiService().likeMusice(id);
    }

    @Override
    public Observable<LikeMusicBean> NolikeMusic(boolean f,long id) {
        return ApiEngine.getInstance().getApiService().NolikeMusic(f,id);
    }

    @Override
    public Observable<LikeListBean> getLikeList(long uid) {
        return ApiEngine.getInstance().getApiService().getLikeList(uid);
    }

    @Override
    public Observable<MusicCommentBean> getMusicComment(long id) {
        return ApiEngine.getInstance().getApiService().getMusicComment(id);
    }

    @Override
    public Observable<CommentLikeBean> likeComment(long id, long cid, int t, int type) {
        return ApiEngine.getInstance().getApiService().likeComment(id, cid, t, type);
    }

    @Override
    public Observable<LyricBean> getLyric(long id) {
        return ApiEngine.getInstance().getApiService().getLyric(id);
    }

    @Override
    public Observable<PlayListCommentBean> getPlaylistComment(long id) {
        return ApiEngine.getInstance().getApiService().getPlaylistComment(id);
    }
}
