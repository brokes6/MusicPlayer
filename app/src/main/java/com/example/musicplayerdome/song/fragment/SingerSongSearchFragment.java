package com.example.musicplayerdome.song.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.FragmentSingerHotSongBinding;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.adapter.MySongListAdapter;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.song.other.SingIdEvent;
import com.example.musicplayerdome.song.other.SingerPresenter;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌手热门歌曲搜索
 */
@SuppressLint("ValidFragment")
public class SingerSongSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerSongSearchFragmen";
    FragmentSingerHotSongBinding binding;
    private long singerId = -1;
    private MySongListAdapter adapter;
    private List<SingerSongSearchBean.HotSongsBean> hotSongList = new ArrayList<>();
    private List<SongInfo> songInfos = new ArrayList<>();


    public SingerSongSearchFragment() {
        setFragmentTitle("热门单曲");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerId = event.getSingId();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_singer_hot_song, container, false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        adapter = new MySongListAdapter(getContext());
        binding.rvSingerSong.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSingerSong.setAdapter(adapter);
        adapter.setType(2);
        Log.e(TAG, "initData: 数据为"+singerId);
        if (singerId != -1) {
            showDialog();
            mPresenter.getSingerHotSong(singerId);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    public SingerPresenter onCreatePresenter() {
        return new SingerPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetSingerHotSongSuccess(SingerSongSearchBean bean) {
        hideDialog();
        hotSongList.clear();
        songInfos.clear();
        hotSongList.addAll(bean.getHotSongs());
        for (int i = 0; i < hotSongList.size(); i++) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongId(String.valueOf(bean.getHotSongs().get(i).getId()));
            songInfo.setSongUrl(BaseActivity.SONG_URL + bean.getHotSongs().get(i).getId() + ".mp3");
            songInfo.setSongName(bean.getHotSongs().get(i).getName());
            songInfo.setDuration(bean.getHotSongs().get(i).getDt());
            songInfo.setArtist(bean.getArtist().getName());
            songInfo.setSongCover(bean.getHotSongs().get(i).getAl().getPicUrl());
            songInfo.setArtistId(String.valueOf(bean.getArtist().getId()));
            songInfo.setArtistKey(bean.getArtist().getPicUrl());
            songInfos.add(songInfo);
        }
        adapter.loadMore(songInfos);
        adapter.setList(songInfos);
    }

    @Override
    public void onGetSingerHotSongFail(String e) {
        hideDialog();
        Log.e(TAG, "onGetSingerHotSongFail " + e);
        XToastUtils.error(e);
    }

    @Override
    public void onGetSingerAlbumSuccess(SingerAblumSearchBean bean) {

    }

    @Override
    public void onGetSingerAlbumFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerDescSuccess(SingerDescriptionBean bean) {

    }

    @Override
    public void onGetSingerDescFail(String e) {

    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {

    }

    @Override
    public void onGetSimiSingerFail(String e) {

    }

    @Override
    public void onGetSongMvDataSuccess(SongMvBean bean) {

    }

    @Override
    public void onGetSongMvDataFail(String e) {

    }

    @Override
    public void onLoadMoreSongMvDataSuccess(SongMvBean bean) {

    }

    @Override
    public void onLoadMoreSongMvDataFail(String e) {

    }
}
