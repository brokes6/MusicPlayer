package com.example.musicplayerdome.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.bean.SongRecommendation;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.main.adapter.MySongAdapter;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
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
import com.example.musicplayerdome.resources.DomeData;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.view.FMSongActivity;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.popupwindow.easypopup.EasyPopup;
import com.xuexiang.xui.widget.popupwindow.easypopup.HorizontalGravity;
import com.xuexiang.xui.widget.popupwindow.easypopup.VerticalGravity;

import java.util.ArrayList;
import java.util.List;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;

public class SongSheetFragment extends BaseFragment<MinePresenter> implements View.OnClickListener, MineContract.View{
    SongsheetfragmentBinding binding;
    private static final String TAG = "SongSheetFragment";
    private UserPlaylistAdapter adapter;
    private MySongAdapter mainMusicAdapter;
    private EasyPopup mCirclePop;
    private LoginBean loginBean;
    private long uid;
    private List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
    private List<PlayListItemBean> adapterList = new ArrayList<>();

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
        initView();
        initCirclePop();
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
        binding.myMusicList.setLayoutManager(lm);
        binding.myMusicList.setAdapter(mainMusicAdapter = new MySongAdapter());
        mainMusicAdapter.loadMore(DomeData.getMySong());
        mainMusicAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<SongRecommendation>() {
            @Override
            public void onItemClick(View itemView, SongRecommendation item, int position) {
                if (position==1){
                    mPresenter.getMyFM();
                }
            }
        });

        showDialog();
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
    }

    UserPlaylistAdapter.OnPlayListItemClickListener listener = new UserPlaylistAdapter.OnPlayListItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            Intent intent = new Intent(getContext(), SongSheetActivityMusic.class);
            intent.putExtra(PLAYLIST_PICURL, playlistBeans.get(position).getCoverImgUrl());
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

    public void initCirclePop() {
        mCirclePop = new EasyPopup(getContext())
                .setContentView(R.layout.layout_friend_circle_comment)
                .setFocusAndOutsideEnable(true)
                .createPopup();

        TextView tvZan = mCirclePop.getView(R.id.tv_zan);
        TextView tvComment = mCirclePop.getView(R.id.tv_comment);
        tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.minePlaylist.setVisibility(View.GONE);
                mCirclePop.dismiss();
            }
        });

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.minePlaylist.setVisibility(View.VISIBLE);
                mCirclePop.dismiss();
            }
        });
    }


    @Override
    public MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }


    private void initView(){
        binding.minePlaylistMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_playlist_more:
                mCirclePop.showAtAnchorView(binding.minePlaylistMore, VerticalGravity.CENTER, HorizontalGravity.LEFT, 0, 0);
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
    }

    @Override
    public void onGetUserPlaylistFail(String e) {
        hideDialog();
    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {
        binding.userGrade.setText("Lv."+bean.getLevel());
        binding.Suserimg.setImageURL(bean.getProfile().getAvatarUrl());
    }

    @Override
    public void onGetUserDetailFails(String e) {

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
        Log.d(TAG, "onGetMyFMSuccess：" + bean);
        List<MyFmBean.DataBean> fmList = bean.getData();
        List<SongInfo> songList = new ArrayList<>();
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
        SongPlayManager.getInstance().clickPlayAll(songList, 0);
        SongInfo songInfo = songList.get(0);
        Intent intent = new Intent(getContext(), FMSongActivity.class);
        intent.putExtra(FMSongActivity.SONG_INFO, songInfo);
        startActivity(intent);
    }

    @Override
    public void onGetMyFMFail(String e) {
        hideDialog();
    }
}
