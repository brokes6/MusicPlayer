package com.example.musicplayerdome.search.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.FragmentSearchSongBinding;
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
import com.example.musicplayerdome.search.other.KeywordsEvent;
import com.example.musicplayerdome.search.other.SearchPresenter;
import com.example.musicplayerdome.search.view.SearchResultActivity;
import com.example.musicplayerdome.song.adapter.MySongListAdapter;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchActivity.SearchResultActivity.SongSearchFragment 搜索结果之一 歌曲页面
 * 展示搜索的结果为歌曲的页面
 */
public class SongSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View{
    private static final String TAG = "SongSearchFragment";
    private FragmentSearchSongBinding binding;
    private String keywords;
    private int searchType = 1;
    private MySongListAdapter adapter;
    private List<SongSearchBean.ResultBean.SongsBean> resultBeans = new ArrayList<>();
    private boolean needRefresh = false;
    private List<SongInfo> songInfos = new ArrayList<>();

    public SongSearchFragment() {
        setFragmentTitle("单 曲");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetKeywordsEvent(KeywordsEvent event) {
        //在主线程中进行，处理粘性事件
        Log.e(TAG, "SongSearchEvent : " + event);
        if (event != null) {
            if (keywords != null && !event.getKeyword().equals(keywords)) {
                needRefresh = true;
                if (((SearchResultActivity) getActivity()).getPosition() == 1) {
                    needRefresh = false;
                    keywords = event.getKeyword();
                }
            }
            showDialog();
            keywords = event.getKeyword();
            mPresenter.getSongSearch(keywords, searchType);
        }
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_song,container,false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        resultBeans.clear();

        adapter = new MySongListAdapter(getContext());
        adapter.setType(3);
        adapter.setKeywords(keywords);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvSongSearch.setLayoutManager(manager);
        binding.rvSongSearch.setAdapter(adapter);
    }

    @Override
    protected void initView() {

    }

    @Override
    public SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {

    }

    @Override
    public void onGetHotSearchDetailFail(String e) {

    }

    @Override
    public void onGetSongSearchSuccess(SongSearchBean bean) {
        hideDialog();
        if (resultBeans.size()!=0){
            resultBeans.clear();
            songInfos.clear();
            if (bean.getResult().getSongs() != null) {
                resultBeans.addAll(bean.getResult().getSongs());
            }
            for (int i = 0; i < resultBeans.size(); i++) {
                SongInfo songInfo = new SongInfo();
                songInfo.setSongId(String.valueOf(resultBeans.get(i).getId()));
                songInfo.setArtist(resultBeans.get(i).getArtists().get(0).getName());
                songInfo.setSongCover(resultBeans.get(i).getArtists().get(0).getPicUrl() != null ? resultBeans.get(i).getArtists().get(0).getPicUrl() :
                        resultBeans.get(i).getArtists().get(0).getImg1v1Url());
                songInfo.setSongName(resultBeans.get(i).getName());
                songInfo.setSongUrl(SONG_URL + resultBeans.get(i).getId() + ".mp3");
                songInfo.setDuration(resultBeans.get(i).getDuration());
                songInfo.setArtistId(String.valueOf(resultBeans.get(i).getArtists().get(0).getId()));
                songInfo.setArtistKey(resultBeans.get(i).getArtists().get(0).getPicUrl());
                songInfos.add(songInfo);
            }
            adapter.refresh(songInfos);
        }else{
            resultBeans.clear();
            if (bean.getResult().getSongs() != null) {
                resultBeans.addAll(bean.getResult().getSongs());
            }
            songInfos.clear();
            for (int i = 0; i < resultBeans.size(); i++) {
                SongInfo songInfo = new SongInfo();
                songInfo.setSongId(String.valueOf(resultBeans.get(i).getId()));
                songInfo.setArtist(resultBeans.get(i).getArtists().get(0).getName());
                songInfo.setSongCover(resultBeans.get(i).getArtists().get(0).getPicUrl() != null ? resultBeans.get(i).getArtists().get(0).getPicUrl() :
                        resultBeans.get(i).getArtists().get(0).getImg1v1Url());
                songInfo.setSongName(resultBeans.get(i).getName());
                songInfo.setSongUrl(SONG_URL + resultBeans.get(i).getId() + ".mp3");
                songInfo.setDuration(resultBeans.get(i).getDuration());
                songInfo.setArtistId(String.valueOf(resultBeans.get(i).getArtists().get(0).getId()));
                songInfo.setArtistKey(resultBeans.get(i).getArtists().get(0).getPicUrl());
                songInfos.add(songInfo);
            }
            adapter.loadMore(songInfos);
        }
    }

    @Override
    public void onGetSongSearchFail(String e) {
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {

    }

    @Override
    public void onGetSingerSearchFail(String e) {

    }

    @Override
    public void onGetAlbumSearchSuccess(AlbumSearchBean bean) {

    }

    @Override
    public void onGetAlbumSearchFail(String e) {

    }

    @Override
    public void onGetPlayListSearchSuccess(PlayListSearchBean bean) {

    }

    @Override
    public void onGetPlayListSearchFail(String e) {

    }

    @Override
    public void onGetRadioSearchSuccess(RadioSearchBean bean) {

    }

    @Override
    public void onGetRadioSearchFail(String e) {

    }

    @Override
    public void onGetUserSearchSuccess(UserSearchBean bean) {

    }

    @Override
    public void onGetUserSearchFail(String e) {

    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }

    @Override
    public void onGetVideoDataSuccess(VideoUrlBean bean) {

    }

    @Override
    public void onGetVideoDataFail(String e) {

    }

    @Override
    public void onGetVideoCommentSuccess(MusicCommentBean bean) {

    }

    @Override
    public void onGetVideoCommentFail(String e) {

    }

    @Override
    public void onGetVideoDetailsSuccess(VideoDataBean bean) {

    }

    @Override
    public void onGetVideoDetailsFail(String e) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
