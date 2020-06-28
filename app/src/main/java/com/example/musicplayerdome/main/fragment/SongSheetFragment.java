package com.example.musicplayerdome.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.collection.view.MyCollectionActivity;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.view.PersonalActivity;
import com.example.musicplayerdome.song.adapter.UserPlaylistAdapter;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.SongsheetfragmentBinding;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.main.other.MinePresenter;
import com.example.musicplayerdome.personal.bean.PlayListItemBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.view.FMSongActivity;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_DESCRIPTION;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;
import static com.example.musicplayerdome.personal.view.PersonalActivity.USER_ID;

/**
 * HomeActivityMusic.SongSheetFragment（Tab之一，歌单）
 * 展示用户的歌单，私人FM
 */
public class SongSheetFragment extends BaseFragment<MinePresenter> implements MineContract.View{
    SongsheetfragmentBinding binding;
    private static final String TAG = "SongSheetFragment";
    private UserPlaylistAdapter adapter;
    private LoginBean loginBean;
    private long uid;
    private List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
    private List<PlayListItemBean> adapterList = new ArrayList<>();
    private List<SongInfo> songList;
    private List<MyFmBean.DataBean> fmList;

    public SongSheetFragment() {
        setFragmentTitle("歌 单");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.songsheetfragment,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        loginBean = GsonUtil.fromJSON(SharePreferenceUtil.getInstance(getContext()).getUserInfo(""), LoginBean.class);
        initUser(loginBean);
        uid = loginBean.getAccount().getId();
        Log.e(TAG, "initData: id为"+uid );
        adapter = new UserPlaylistAdapter(getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        binding.minePlaylist.setLayoutManager(manager);
        binding.minePlaylist.setAdapter(adapter);
        adapter.setListener(listener);
        adapter.setShowSmartPlay(true);
        adapter.setName(loginBean.getAccount().getUserName());

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);

        showDialog();
        mPresenter.getMyFM();
        mPresenter.getUserDetail(uid);
        mPresenter.getUserPlaylist(uid);
    }

    private void initUser(LoginBean bean){
        if (bean.getProfile().getAvatarUrl() != null) {
            String avatarUrl = bean.getProfile().getAvatarUrl();
            Glide.with(this).load(avatarUrl).into(binding.Suserimg);
        }
        if (bean.getProfile().getNickname() != null) {
            binding.Susername.setText(bean.getProfile().getNickname());
        }
        if (bean.getProfile().getVipType()==0){
            binding.userVip.setText("普通用户");
        }else{
            binding.userVip.setText("黑胶CVIP");
        }
    }

    UserPlaylistAdapter.OnPlayListItemClickListener listener = new UserPlaylistAdapter.OnPlayListItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            Intent intent = new Intent(getContext(), SongSheetActivityMusic.class);
            intent.putExtra(PLAYLIST_PICURL, playlistBeans.get(position).getCoverImgUrl());
            intent.putExtra(PLAYLIST_DESCRIPTION, playlistBeans.get(position).getDescription());
            intent.putExtra(PLAYLIST_NAME, playlistBeans.get(position).getName());
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistBeans.get(position).getCreator().getNickname());
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistBeans.get(position).getCreator().getAvatarUrl());
            intent.putExtra(PLAYLIST_ID, playlistBeans.get(position).getId());
            getContext().startActivity(intent);
        }

        @Override
        public void onSmartPlayClick(int position) {
            showDialog();
            mPresenter.getIntelligenceList(1, playlistBeans.get(position).getId());
        }
    };



    @Override
    public MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    protected void initView(){
        binding.myMusic.setOnClickListener(this);
        binding.MCollection.setOnClickListener(this);
        binding.SongLike.setOnClickListener(this);
        binding.SongFM.setOnClickListener(this);
        binding.minePlaylistMore.setOnClickListener(this);
        binding.PersonalPage.setOnClickListener(this);
        //设置 Header式
        binding.refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        //取消Footer
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setDisableContentWhenRefresh(true);

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: 开始刷新");
                mPresenter.getMyFM();
                mPresenter.getUserPlaylistAgain(uid);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_playlist_more:
                break;
            case R.id.Song_FM:
                SongPlayManager.getInstance().clickPlayAll(songList, 0);
                SongInfo songInfo = songList.get(0);
                Intent intent = new Intent(getContext(), FMSongActivity.class);
                intent.putExtra(FMSongActivity.SONG_INFO, songInfo);
                startActivity(intent);
                break;
            case R.id.Song_Like:
            case R.id.my_music:
                Intent intent1 = new Intent(getContext(), SongSheetActivityMusic.class);
                intent1.putExtra(PLAYLIST_PICURL, playlistBeans.get(0).getCoverImgUrl());
                intent1.putExtra(PLAYLIST_NAME, playlistBeans.get(0).getName());
                intent1.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistBeans.get(0).getCreator().getNickname());
                intent1.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistBeans.get(0).getCreator().getAvatarUrl());
                intent1.putExtra(PLAYLIST_ID, playlistBeans.get(0).getId());
                getContext().startActivity(intent1);
                break;
            case R.id.M_collection:
                ActivityUtils.startActivity(MyCollectionActivity.class);
                break;
            case R.id.Personal_page:
                Intent intent2 = new Intent(getContext(),PersonalActivity.class);
                intent2.putExtra(USER_ID,uid);
                getContext().startActivity(intent2);
                break;
        }
    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {
        hideDialog();
        Log.d(TAG, "onGetUserPlaylistSuccess :" + bean);
        playlistBeans.clear();
        playlistBeans.addAll(bean.getPlaylist());
        for (int i = 0; i < playlistBeans.size(); i++) {
            PlayListItemBean beanInfo = new PlayListItemBean();
            beanInfo.setCoverUrl(playlistBeans.get(i).getCoverImgUrl());
            beanInfo.setPlaylistId(playlistBeans.get(i).getId());
            beanInfo.setPlayCount(playlistBeans.get(i).getPlayCount());
            beanInfo.setPlayListName(playlistBeans.get(i).getName());
            beanInfo.setSongNumber(playlistBeans.get(i).getTrackCount());
            beanInfo.setPlaylistCreator(playlistBeans.get(i).getCreator().getNickname());
            adapterList.add(beanInfo);
        }
        adapter.loadMore(adapterList);
        Glide.with(this).load(bean.getPlaylist().get(0).getCoverImgUrl()).transition(new DrawableTransitionOptions().crossFade()).into(binding.SongLikeImg);

    }

    @Override
    public void onGetUserPlaylistFail(String e) {
        hideDialog();
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

    @Override
    public void onGetUserPlaylistAgainSuccess(UserPlaylistBean bean) {
        List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
        List<PlayListItemBean> adapterList = new ArrayList<>();

        playlistBeans.clear();
        playlistBeans.addAll(bean.getPlaylist());
        for (int i = 0; i < playlistBeans.size(); i++) {
            PlayListItemBean beanInfo = new PlayListItemBean();
            beanInfo.setCoverUrl(playlistBeans.get(i).getCoverImgUrl());
            beanInfo.setPlaylistId(playlistBeans.get(i).getId());
            beanInfo.setPlayCount(playlistBeans.get(i).getPlayCount());
            beanInfo.setPlayListName(playlistBeans.get(i).getName());
            beanInfo.setSongNumber(playlistBeans.get(i).getTrackCount());
            beanInfo.setPlaylistCreator(playlistBeans.get(i).getCreator().getNickname());
            adapterList.add(beanInfo);
        }
        Log.e(TAG, "歌单刷新成功");
        adapter.refresh(adapterList);
        binding.refreshLayout.finishRefresh(true);
    }

    @Override
    public void onGetUserPlaylistAgainFail(String e) {
        XToastUtils.error("刷新失败，请检查网络再试");
        binding.refreshLayout.finishRefresh(true);
    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {
        binding.userGrade.setText("Lv."+bean.getLevel());
        binding.Suserimg.setImageURL(bean.getProfile().getAvatarUrl());
    }

    @Override
    public void onGetUserDetailFails(String e) {
        Log.e(TAG, "获取用户信息错误"+e );
    }

    @Override
    public void onGetPlayHistoryListSuccess(SonghistoryBean bean) {

    }

    @Override
    public void onGetPlayHistoryListFail(String e) {
        hideDialog();
    }

    @Override
    public void onGetIntelligenceListSuccess(PlayModeIntelligenceBean bean) {
        hideDialog();
    }

    @Override
    public void onGetIntelligenceListFail(String e) {
        hideDialog();
    }

    @Override
    public void onGetMvSublistBeanSuccess(MvSublistBean bean) {

    }

    @Override
    public void onGetMvSublistBeanFail(String e) {

    }

    @Override
    public void onGetArtistSublistBeanSuccess(ArtistSublistBean bean) {

    }

    @Override
    public void onGetArtistSublistBeanFail(String e) {

    }

    @Override
    public void onGetAlbumSublistBeanSuccess(AlbumSublistBean bean) {

    }

    @Override
    public void onGetAlbumSublistBeanFail(String e) {

    }

    @Override
    public void onGetMyFMSuccess(MyFmBean bean) {
        hideDialog();
        if (fmList!=null){
            Log.e(TAG, "onGetMyFMSuccess: 开始清除旧内容");
            fmList.clear();
            songList.clear();

            fmList = bean.getData();
            songList = new ArrayList<>();
            for (int i = 0; i < fmList.size(); i++) {
                SongInfo songInfo = new SongInfo();
                songInfo.setSongName(fmList.get(i).getName());
                songInfo.setDownloadUrl((String) fmList.get(i).getMp3Url());
                songInfo.setSongUrl(SONG_URL + fmList.get(i).getId() + ".mp3");
                songInfo.setSongCover(fmList.get(i).getAlbum().getBlurPicUrl());
                songInfo.setArtist(fmList.get(i).getArtists().get(0).getName());
                songInfo.setSongId(String.valueOf(fmList.get(i).getId()));
                songInfo.setDuration(fmList.get(i).getDuration());
                songInfo.setArtistId(String.valueOf(fmList.get(i).getArtists().get(0).getId()));
                songInfo.setArtistKey(fmList.get(i).getArtists().get(0).getPicUrl());
                songList.add(songInfo);
            }

            Glide.with(this).load(bean.getData().get(0).getAlbum().getBlurPicUrl()).transition(new DrawableTransitionOptions().crossFade()).into(binding.mySongImg);
            Log.e(TAG, "私人FM刷新成功");
        }
        Log.d(TAG, "onGetMyFMSuccess：" + bean);
        fmList = bean.getData();
        songList = new ArrayList<>();
        for (int i = 0; i < fmList.size(); i++) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongName(fmList.get(i).getName());
            songInfo.setDownloadUrl((String) fmList.get(i).getMp3Url());
            songInfo.setSongUrl(SONG_URL + fmList.get(i).getId() + ".mp3");
            songInfo.setSongCover(fmList.get(i).getAlbum().getBlurPicUrl());
            songInfo.setArtist(fmList.get(i).getArtists().get(0).getName());
            songInfo.setSongId(String.valueOf(fmList.get(i).getId()));
            songInfo.setDuration(fmList.get(i).getDuration());
            songInfo.setArtistId(String.valueOf(fmList.get(i).getArtists().get(0).getId()));
            songInfo.setArtistKey(fmList.get(i).getArtists().get(0).getPicUrl());
            songList.add(songInfo);
        }
        Glide.with(this).load(bean.getData().get(0).getAlbum().getBlurPicUrl()).transition(new DrawableTransitionOptions().crossFade()).into(binding.mySongImg);
    }

    @Override
    public void onGetMyFMFail(String e) {
        hideDialog();
    }
}
