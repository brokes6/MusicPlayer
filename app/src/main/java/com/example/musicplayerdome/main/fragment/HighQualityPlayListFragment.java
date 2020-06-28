package com.example.musicplayerdome.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.databinding.FragmentHighQualityBinding;
import com.example.musicplayerdome.main.adapter.HighPlayListAdapter;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.other.WowPresenter;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.rewrite.PlayListCover;
import com.example.musicplayerdome.rewrite.RikkaPlayListPager;
import com.example.musicplayerdome.song.bean.SongDetailBean;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;

/**
 * 主页歌单页面.精品页面
 * 展示精品歌单
 */
public class HighQualityPlayListFragment extends BaseFragment<WowPresenter> implements WowContract.View{
    private static final String TAG = "HighQualityPlayListFrag";
    FragmentHighQualityBinding binding;
    private HighPlayListAdapter adapter;
    private List<HighQualityPlayListBean.PlaylistsBean> playlist = new ArrayList<>();
    private List<PlaylistBean> list = new ArrayList<>();
    private GridLayoutManager manager;
    public HighQualityPlayListFragment() {
        setFragmentTitle("精品");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_high_quality,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        adapter = new HighPlayListAdapter(getContext());
        adapter.setListener(listener);
        manager = new GridLayoutManager(getContext(), 3);
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);

        showDialog();
        mPresenter.getHighQuality(21, 0);
    }

    @Override
    protected void initView() {

    }

    private HighPlayListAdapter.OnTopListClickListener listener = position -> {
        if (playlist != null || !playlist.isEmpty()) {
            Intent intent = new Intent(getActivity(), SongSheetActivityMusic.class);
            HighQualityPlayListBean.PlaylistsBean bean = playlist.get(position + 3);
            String playlistName = bean.getName();
            intent.putExtra(PLAYLIST_NAME, playlistName);
            String playlistPicUrl = bean.getCoverImgUrl();
            intent.putExtra(PLAYLIST_PICURL, playlistPicUrl);
            String playlistCreatorNickName = bean.getCreator().getNickname();
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistCreatorNickName);
            String playlistCreatorAvatarUrl = bean.getCreator().getAvatarUrl();
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistCreatorAvatarUrl);
            long playlistId = bean.getId();
            intent.putExtra(PLAYLIST_ID, playlistId);
            startActivity(intent);
        }
    };

    @Override
    public WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        
    }

    @Override
    public void onGetBannerSuccess(BannerBean bean) {

    }

    @Override
    public void onGetBannerFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListAgainSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListAgainFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {

    }

    @Override
    public void onGetDailyRecommendFail(String e) {

    }

    @Override
    public void onGetTopListSuccess(TopListBean bean) {

    }

    @Override
    public void onGetTopListFail(String e) {

    }

    @Override
    public void onGetPlayListSuccess(RecommendPlayListBean bean) {

    }

    @Override
    public void onGetPlayListFail(String e) {

    }

    @Override
    public void onGetPlaylistDetailSuccess(PlaylistDetailBean bean) {

    }

    @Override
    public void onGetPlaylistDetailFail(String e) {

    }


    @Override
    public void onGetMusicCanPlaySuccess(MusicCanPlayBean bean) {

    }

    @Override
    public void onGetMusicCanPlayFail(String e) {

    }

    @Override
    public void onGetHighQualitySuccess(HighQualityPlayListBean bean) {
        hideDialog();
        playlist.addAll(bean.getPlaylists());
        addInfoToPager();
        for (int i = 3; i < playlist.size(); i++) {
            PlaylistBean beanInfo = new PlaylistBean();
            beanInfo.setPlaylistName(playlist.get(i).getName());
            beanInfo.setPlaylistCoverUrl(playlist.get(i).getCoverImgUrl());
            list.add(beanInfo);
        }
        adapter.loadMore(list);
    }

    private void addInfoToPager() {
        for (int i = 0; i < 3; i++) {
            PlayListCover cover = new PlayListCover(getContext());
            cover.setCover(playlist.get(i).getCoverImgUrl());
            cover.setText(playlist.get(i).getName());
            binding.pager.addView(cover);
            RikkaPlayListPager.RikkaLayoutParams lp = (RikkaPlayListPager.RikkaLayoutParams) cover.getLayoutParams();
            lp.setFrom(i);
            lp.setTo(i);
            lp.setIndex(i);
        }
        binding.pager.setClickListener(playlistClickListener);
    }

    RikkaPlayListPager.OnPlayListClickListener playlistClickListener = position -> {
        if (playlist != null || !playlist.isEmpty()) {
            Intent intent = new Intent(getActivity(), SongSheetActivityMusic.class);
            HighQualityPlayListBean.PlaylistsBean bean = playlist.get(position);
            String playlistName = bean.getName();
            intent.putExtra(PLAYLIST_NAME, playlistName);
            String playlistPicUrl = bean.getCoverImgUrl();
            intent.putExtra(PLAYLIST_PICURL, playlistPicUrl);
            String playlistCreatorNickName = bean.getCreator().getNickname();
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistCreatorNickName);
            String playlistCreatorAvatarUrl = bean.getCreator().getAvatarUrl();
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistCreatorAvatarUrl);
            long playlistId = bean.getId();
            intent.putExtra(PLAYLIST_ID, playlistId);
            startActivity(intent);
        }
    };

    @Override
    public void onGetHighQualityFail(String e) {
        hideDialog();
    }

    @Override
    public void onGetRecommendsongSuccess(RecommendsongBean bean) {

    }

    @Override
    public void onGetRecommendsongFail(String e) {

    }

    @Override
    public void onGetCollectionListSuccess(CollectionListBean bean) {

    }

    @Override
    public void onGetCollectionListFail(String e) {

    }

    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {

    }

    @Override
    public void onGetSongDetailFail(String e) {

    }
}
