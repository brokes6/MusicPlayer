package com.example.musicplayerdome.api;


import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.DjCategoryRecommendBean;
import com.example.musicplayerdome.bean.DjCatelistBean;
import com.example.musicplayerdome.bean.DjDetailBean;
import com.example.musicplayerdome.bean.DjPaygiftBean;
import com.example.musicplayerdome.bean.DjProgramBean;
import com.example.musicplayerdome.bean.DjRecommendBean;
import com.example.musicplayerdome.bean.DjRecommendTypeBean;
import com.example.musicplayerdome.bean.DjSubBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.CatlistBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.bean.LogoutBean;
import com.example.musicplayerdome.main.bean.MainEventBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserEventBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.PlayListSearchBean;
import com.example.musicplayerdome.search.bean.RadioSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSearchBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.search.bean.SongSearchBean;
import com.example.musicplayerdome.search.bean.SynthesisSearchBean;
import com.example.musicplayerdome.search.bean.UserSearchBean;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MVDetailBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.song.bean.SongMvDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import yuncun.bean.YuncunReviewBean;

/**
 * Created By Rikka on 2019/7/12
 */
public interface ApiService {
    //电脑ip
    String BASE_URL = "http:///192.168.0.150:3000";
    //小米8手机ip
//    String BASE_URL = "http:///192.168.43.96:3000";
    @GET("login/cellphone")
    Observable<LoginBean> login(@Query("phone") String phone, @Query("password") String password);

    @GET("logout")
    Observable<LogoutBean> logout();

    @GET("banner")
    Observable<BannerBean> getBanner(@Query("type") String type);

    @GET("recommend/resource")
    Observable<MainRecommendPlayListBean> getRecommendPlayList();

    @GET("recommend/songs")
    Observable<DailyRecommendBean> getDailyRecommend();

    @GET("toplist")
    Observable<TopListBean> getTopList();

    @GET("dj/recommend")
    Observable<DjRecommendBean> getRadioRecommend();

    @GET("dj/recommend/type")
    Observable<DjRecommendTypeBean> getDjRecommend(@Query("type") int type);

    @GET("top/playlist")
    Observable<RecommendPlayListBean> getPlayList(@Query("cat") String type, @Query("limit") int limit);

    @GET("top/playlist/highquality")
    Observable<HighQualityPlayListBean> getHighquality(@Query("limit") int limit, @Query("before") long before);

    @GET("playlist/catlist")
    Observable<CatlistBean> getCatlist();

    @GET("playlist/detail")
    Observable<PlaylistDetailBean> getPlaylistDetail(@Query("id") long id);

    @GET("check/music")
    Observable<MusicCanPlayBean> getMusicCanPlay(@Query("id") long id);

    @GET("user/playlist")
    Observable<UserPlaylistBean> getUserPlaylist(@Query("uid") long uid);

    @GET("user/event")
    Observable<UserEventBean> getUserEvent(@Query("uid") long uid, @Query("limit") int limit, @Query("lasttime") long time);

    @GET("user/detail")
    Observable<UserDetailBean> getUserDetail(@Query("uid") long uid);

    @GET("user/record")
    Observable<SonghistoryBean> getPlayHistoryList(@Query("uid") long uid, @Query("type") int type);

    @GET("search/hot/detail")
    Observable<HotSearchDetailBean> getSearchHotDetail();

    @GET("search")
    Observable<SongSearchBean> getSongSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<FeedSearchBean> getFeedSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<SingerSearchBean> getSingerSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<AlbumSearchBean> getAlbumSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<PlayListSearchBean> getPlayListSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<RadioSearchBean> getRadioSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<UserSearchBean> getUserSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<SynthesisSearchBean> getSynthesisSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("artists")
    Observable<SingerSongSearchBean> getSingerHotSong(@Query("id") long id);

    @GET("artist/album")
    Observable<SingerAblumSearchBean> getSingerAlbum(@Query("id") long id);

    @GET("artist/desc")
    Observable<SingerDescriptionBean> getSingerDesc(@Query("id") long id);

    @GET("simi/artist")
    Observable<SimiSingerBean> getSimiSinger(@Query("id") long id);

    @GET("likelist")
    Observable<LikeListBean> getLikeList(@Query("uid") long uid);

    @GET("song/detail")
    Observable<SongDetailBean> getSongDetail(@Query("ids") long ids);

    @GET("like")
    Observable<LikeMusicBean> likeMusice(@Query("id") long id);

    @GET("like")
    Observable<LikeMusicBean> NolikeMusic(@Query("like") boolean y,@Query("id") long id);

    @GET("comment/music")
    Observable<MusicCommentBean> getMusicComment(@Query("id") long id);

    @GET("mv/detail")
    Observable<MVDetailBean> getMVDetail(@Query("mvid") long id);

    @GET("comment/mv")
    Observable<MusicCommentBean> getSongMvComment(@Query("id") long id);

    @GET("comment/like")
    Observable<CommentLikeBean> likeComment(@Query("id") long id, @Query("cid") long cid, @Query("t") int t, @Query("type") int type);

    @GET("playmode/intelligence/list")
    Observable<PlayModeIntelligenceBean> getIntelligenceList(@Query("id") long id, @Query("pid") long pid);

    @GET("mv/url")
    Observable<SongMvDataBean> getSongMv(@Query("id") long id);

    @GET("artist/mv")
    Observable<SongMvBean> getSongMvData(@Query("id") long id);

    @GET("artist/mv")
    Observable<SongMvBean> LoadMoreSongMvData(@Query("id") long id, @Query("offset") long offset);

    @GET("album/sublist")
    Observable<AlbumSublistBean> getAlbumSublist();

    @GET("artist/sublist")
    Observable<ArtistSublistBean> getArtistSublist();

    @GET("personalized/mv")
    Observable<MvSublistBean> getRecommendMV();

    @GET("comment/hotwall/list")
    Observable<YuncunReviewBean> getYuncun();

    @GET("mv/sublist")
    Observable<MvSublistBean> getMvSublist();

    @GET("personal_fm")
    Observable<MyFmBean> getMyFm();

    @GET("event")
    Observable<MainEventBean> getMainEvent();

    @GET("lyric")
    Observable<LyricBean> getLyric(@Query("id") long id);

    @GET("comment/playlist")
    Observable<PlayListCommentBean> getPlaylistComment(@Query("id") long id);

    @GET("dj/paygift")
    Observable<DjPaygiftBean> getDjPaygift(@Query("limit") int limit, @Query("offset") int offset);

    @GET("dj/category/recommend")
    Observable<DjCategoryRecommendBean> getDjCategoryRecommend();

    @GET("dj/catelist")
    Observable<DjCatelistBean> getDjCatelist();

    @GET("dj/sub")
    Observable<DjSubBean> subDj(@Query("rid") long rid, @Query("t") int isSub);

    @GET("dj/program")
    Observable<DjProgramBean> getDjProgram(@Query("rid") long rid);

    @GET("dj/detail")
    Observable<DjDetailBean> getDjDetail(@Query("rid") long rid);
}
