package com.example.musicplayerdome.song.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.blankj.utilcode.util.ToastUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.FragmentRecyclerviewBinding;
import com.example.musicplayerdome.search.bean.AlbumAdapterBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.adapter.AlbumAdapter;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.song.other.SingIdEvent;
import com.example.musicplayerdome.song.other.SingerPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
/**
 * 歌手专辑界面
 */
public class SingerAlbumSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View {
    private static final String TAG = "SingerAlbumSearchFragme";
    FragmentRecyclerviewBinding binding;
    private AlbumAdapter adapter;
    private List<SingerAblumSearchBean.HotAlbumsBean> hotAlbumsList = new ArrayList<>();
    private List<AlbumAdapterBean> adapterList = new ArrayList<>();

    private long singerId = -1;


    public SingerAlbumSearchFragment() {
        setFragmentTitle("专辑");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerId = event.getSingId();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_recyclerview,container,false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        //取消Header
        binding.refreshLayout.setEnableRefresh(false);
        //取消Footer
        binding.refreshLayout.setEnableLoadMore(false);

        adapter = new AlbumAdapter(getContext());
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(adapter);
        adapter.setType(2);
        adapter.setListener(listener);

        if (singerId != -1) {
            showDialog();
            mPresenter.getSingerAlbum(singerId);
        }
    }

    @Override
    protected void initView() {

    }

    AlbumAdapter.OnAlbumClickListener listener = new AlbumAdapter.OnAlbumClickListener() {
        @Override
        public void onAlbumClick(int position) {
            ToastUtils.showShort(position + "");
        }
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
        hideDialog();
        adapterList.clear();
        hotAlbumsList.clear();
        hotAlbumsList.addAll(bean.getHotAlbums());
        addBeanToAdapter();
    }

    private void addBeanToAdapter() {
        for (int i = 0; i < hotAlbumsList.size(); i++) {
            AlbumAdapterBean infoBean = new AlbumAdapterBean();
            infoBean.setCreateTime(hotAlbumsList.get(i).getPublishTime());
            infoBean.setAlbumCoverUrl(hotAlbumsList.get(i).getBlurPicUrl());
            infoBean.setAlbumName(hotAlbumsList.get(i).getName());
            infoBean.setSongCount(hotAlbumsList.get(i).getSize());
            adapterList.add(infoBean);
        }
        adapter.loadMore(adapterList);
    }

    @Override
    public void onGetSingerAlbumFail(String e) {
        hideDialog();
        Log.e(TAG, "onGetSingerAlbumFail : " + e);
        ToastUtils.showShort(e);
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
