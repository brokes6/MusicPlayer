package com.example.musicplayerdome.abstractclass;

import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.bean.SongMvBean;

import io.reactivex.Observable;

public interface SingerContract {
    interface View extends BaseView {
        void onGetSingerHotSongSuccess(SingerSongSearchBean bean);

        void onGetSingerHotSongFail(String e);

        void onGetSingerAlbumSuccess(SingerAblumSearchBean bean);

        void onGetSingerAlbumFail(String e);

        void onGetFeedSearchSuccess(FeedSearchBean bean);

        void onGetFeedSearchFail(String e);

        void onGetSingerDescSuccess(SingerDescriptionBean bean);

        void onGetSingerDescFail(String e);

        void onGetSimiSingerSuccess(SimiSingerBean bean);

        void onGetSimiSingerFail(String e);

        void onGetSongMvDataSuccess(SongMvBean bean);

        void onGetSongMvDataFail(String e);

        void onLoadMoreSongMvDataSuccess(SongMvBean bean);

        void onLoadMoreSongMvDataFail(String e);
    }

    interface Model extends BaseModel {
        Observable<SingerSongSearchBean> getSingerHotSong(long id);

        Observable<SingerAblumSearchBean> getSingerAlbum(long id);

        Observable<FeedSearchBean> getFeedSearch(String keywords, int type);

        Observable<SingerDescriptionBean> getSingerDesc(long id);

        Observable<SimiSingerBean> getSimiSinger(long id);

        Observable<SongMvBean> getSongMvData(long id);

        Observable<SongMvBean> LoadMoreSongMvData(long id,int offset);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getSingerHotSong(long id);

        public abstract void getSingerAlbum(long id);

        public abstract void getFeedSearch(String keywords, int type);

        public abstract void getSingerDesc(long id);

        public abstract void getSimiSinger(long id);

        public abstract void getSongMvData(long id);

        public abstract void LoadMoreSongMvData(long id,int offset);
    }
}
