package com.example.musicplayerdome.search.fragment;

import android.content.Intent;
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
import com.example.musicplayerdome.databinding.FragmentRecyclerviewBinding;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.MvBean;
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
import com.example.musicplayerdome.search.view.VideoActivity;
import com.example.musicplayerdome.song.adapter.FeedAdapter;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.view.SongMvActivity;
import com.example.musicplayerdome.util.XToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchActivity.SearchResultActivity.FeedSearchFragment 搜索结果之一 视频/MV页面
 * 展示搜索的结果为视频的页面
 */
public class FeedSearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View{
    private static final String TAG = "FeedSearchFragment";
    FragmentRecyclerviewBinding binding;
    private String keywords;
    private List<FeedSearchBean.ResultBean.VideosBean> videoList = new ArrayList<>();
    private List<MvBean> mvList = new ArrayList<>();
    private FeedSearchBean.ResultBean feedSearchBean;
    private FeedAdapter adapter;
    private boolean needRefresh = false;
    private int searchType = 1014;

    public FeedSearchFragment() {
        setFragmentTitle("视 频");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetKeywordsEvent(KeywordsEvent event) {
        //在主线程中进行，处理粘性事件
        Log.e(TAG, "FeedSearchEvent : " + event.toString());
        if (event != null) {
            if (keywords != null && !event.getKeyword().equals(keywords)) {
                needRefresh = true;
                if (((SearchResultActivity) getActivity()).getPosition() == 2) {
                    needRefresh = false;
                    keywords = event.getKeyword();
                }
            }
            showDialog();
            keywords = event.getKeyword();
            mPresenter.getFeedSearch(keywords, searchType);
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    protected void initView(){
        adapter = new FeedAdapter(getContext());
        adapter.setType(1);
        adapter.setKeywords(keywords == null ? "" : keywords);
        adapter.setListener(onSimiSingerClickListener);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        videoList.clear();

        //取消Header
        binding.refreshLayout.setEnableRefresh(false);
        //取消Footer
        binding.refreshLayout.setEnableLoadMore(false);

        if (keywords != null) {
        }
    }

    FeedAdapter.OnSimiSingerClickListener onSimiSingerClickListener = new FeedAdapter.OnSimiSingerClickListener() {
        @Override
        public void onSimiClick(int position) {
            if (feedSearchBean.getVideos().get(position).getType()==0){
                Intent intent1 = new Intent(getContext(), SongMvActivity.class);
                intent1.putExtra("pid", position);
                Long id = Long.valueOf(feedSearchBean.getVideos().get(position).getVid());
                intent1.putExtra(SongMvActivity.MVSONG_INFO,id);
                getContext().startActivity(intent1);
            }else{
                Intent intent = new Intent(getContext(), VideoActivity.class);
                intent.putExtra("Vid",feedSearchBean.getVideos().get(position).getVid());
                intent.putExtra("VcoverUrl",feedSearchBean.getVideos().get(position).getCoverUrl());
                intent.putExtra("Vtitle",feedSearchBean.getVideos().get(position).getTitle());
                intent.putExtra("userName",feedSearchBean.getVideos().get(position).getCreator().get(0).getUserName());
                getContext().startActivity(intent);
            }
        }
    };

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

    }

    @Override
    public void onGetSongSearchFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {
        hideDialog();
        if (videoList.size()!=0){
            videoList.clear();
            mvList.clear();
            if (bean.getResult().getVideos() != null) {
                videoList.addAll(bean.getResult().getVideos());
            }
            for (int i = 0; i < videoList.size(); i++) {
                MvBean mvBean = new MvBean();
                mvBean.setCoverUrl(videoList.get(i).getCoverUrl());
                mvBean.setCreator(videoList.get(i).getCreator());
                mvBean.setDuration(videoList.get(i).getDurationms());
                mvBean.setPlayTime(videoList.get(i).getPlayTime());
                mvBean.setTitle(videoList.get(i).getTitle());
                mvBean.setType(videoList.get(i).getType());
                mvBean.setVid(videoList.get(i).getVid());
                mvList.add(mvBean);
            }
            adapter.refresh(mvList);
        }else{
            feedSearchBean = bean.getResult();
            videoList.clear();
            if (bean.getResult().getVideos() != null) {
                videoList.addAll(bean.getResult().getVideos());
            }
            mvList.clear();
            for (int i = 0; i < videoList.size(); i++) {
                MvBean mvBean = new MvBean();
                mvBean.setCoverUrl(videoList.get(i).getCoverUrl());
                mvBean.setCreator(videoList.get(i).getCreator());
                mvBean.setDuration(videoList.get(i).getDurationms());
                mvBean.setPlayTime(videoList.get(i).getPlayTime());
                mvBean.setTitle(videoList.get(i).getTitle());
                mvBean.setType(videoList.get(i).getType());
                mvBean.setVid(videoList.get(i).getVid());
                mvList.add(mvBean);
            }
            adapter.loadMore(mvList);
        }
    }

    @Override
    public void onGetFeedSearchFail(String e) {
        XToastUtils.error("网络请求失败，请检查网络再试");
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
