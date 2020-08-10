package com.example.musicplayerdome.song.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.FragmentRecyclerviewBinding;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.MvBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.adapter.SongMvAdapter;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.song.other.SingIdEvent;
import com.example.musicplayerdome.song.other.SingerPresenter;
import com.example.musicplayerdome.song.view.SongMvActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌手相关视频
 */
public class SingerFeedSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerFeedSearchFragmen";
    FragmentRecyclerviewBinding binding;
    private SongMvAdapter adapter;
    private SongMvBean songMvBean;
    private int searchType = 1014,offset = 0;
    private String singerName;
    private long singId;

    public SingerFeedSearchFragment() {
        setFragmentTitle("相关视频");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerName = event.getSingerName();
        singId = event.getSingId();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_recyclerview, container, false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        adapter = new SongMvAdapter(getContext());
        adapter.setListener(listClickListener);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(adapter);

        if (singerName != null) {
            showDialog();
            mPresenter.getSongMvData(singId);
        }
    }

    @Override
    protected void initView() {
        //设置 Header式
        binding.refreshLayout.setEnableRefresh(false);
        //取消Footer
        binding.refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        binding.refreshLayout.setDisableContentWhenRefresh(true);
        binding.refreshLayout.setEnableAutoLoadMore(false);

        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: 开始加载下一页");
                offset+=10;
                mPresenter.LoadMoreSongMvData(singId,offset);
            }
        });
    }

    private SongMvAdapter.OnSimiSingerClickListener listClickListener = position -> {
        Intent intent = new Intent(getContext(), SongMvActivity.class);
        intent.putExtra(SongMvActivity.MVSONG_INFO, songMvBean.getMvs().get(position).getId());
        getContext().startActivity(intent);


    };

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

    }

    @Override
    public void onGetSingerHotSongFail(String e) {

    }

    @Override
    public void onGetSingerAlbumSuccess(SingerAblumSearchBean bean) {

    }

    @Override
    public void onGetSingerAlbumFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {
//        hideDialog();
//        Log.d(TAG, "onGetFeedSearchSuccess : " + bean);
//        videoList.clear();
//        videoList.addAll(bean.getResult().getVideos());
//        addDataToAdapter();
    }

    private void addDataToAdapter() {
//        mvList.clear();
//        for (int i = 0; i < videoList.size(); i++) {
//            MvBean mvBean = new MvBean();
//            mvBean.setCoverUrl(videoList.get(i).getCoverUrl());
//            mvBean.setCreator(videoList.get(i).getCreator());
//            mvBean.setDuration(videoList.get(i).getDurationms());
//            mvBean.setPlayTime(videoList.get(i).getPlayTime());
//            mvBean.setTitle(videoList.get(i).getTitle());
//            mvBean.setType(videoList.get(i).getType());
//            mvBean.setVid(videoList.get(i).getVid());
//            Log.e(TAG, "歌曲id为"+videoList.get(i).getVid()+";歌曲mv名称"+videoList.get(i).getTitle());
//            mvList.add(mvBean);
//        }
//        adapter.loadMore(mvList);
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
        hideDialog();
        adapter.loadMore(bean.getMvs());
        songMvBean = bean;
    }

    @Override
    public void onGetSongMvDataFail(String e) {

    }

    @Override
    public void onLoadMoreSongMvDataSuccess(SongMvBean bean) {
        if (bean.isHasMore()==false){
            Log.e(TAG, "没有更多了");
            binding.refreshLayout.finishLoadMoreWithNoMoreData();
        }
        Log.e(TAG, "onRefresh加载更多成功");
        adapter.loadMore(bean.getMvs());
        binding.refreshLayout.finishLoadMore(true);
        songMvBean.addMvs(bean.getMvs());
    }

    @Override
    public void onLoadMoreSongMvDataFail(String e) {

    }
}
