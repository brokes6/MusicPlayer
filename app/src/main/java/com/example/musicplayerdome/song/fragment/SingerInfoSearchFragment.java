package com.example.musicplayerdome.song.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.blankj.utilcode.util.ToastUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.LayoutSingerInfoBinding;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.adapter.SimiSingerAdapter;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.song.other.SingIdEvent;
import com.example.musicplayerdome.song.other.SingerPresenter;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.XToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
/**
 * 歌手详细信息Fragment
 * Created By Rikka on 2019/8/17
 */
public class SingerInfoSearchFragment extends BaseFragment<SingerPresenter> implements SingerContract.View,View.OnClickListener {
    private static final String TAG = "SingerInfoSearchFragmen";
    LayoutSingerInfoBinding binding;
    private long singerId = -1;
    private SimiSingerAdapter adapter;
    private List<SimiSingerBean.ArtistsBean> simiList = new ArrayList<>();
    private SingerDescriptionBean descBean;

    public SingerInfoSearchFragment() {
        setFragmentTitle("歌手信息");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetSingerIdEvent(SingIdEvent event) {
        singerId = event.getSingId();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.layout_singer_info, container, false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        adapter = new SimiSingerAdapter(getContext());
        adapter.setListener(listener);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);

        if (singerId != -1) {
            showDialog();
            mPresenter.getSingerDesc(singerId);
            mPresenter.getSimiSinger(singerId);
        }
    }

    SimiSingerAdapter.OnSimiSingerClickListener listener = position -> {
        ToastUtils.showShort(position);
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
        Intent intent = new Intent();
        switch (v.getId()) {
//            case R.id.singer_info_detail:
//                intent.setClass(getActivity(), SingerInfoDetailActivity.class);
//                if (simiList.size() != 0) {
//                    String infoDetail = GsonUtil.toJson(descBean);
//                    intent.putExtra(SingerInfoDetailActivity.INFO_DETAIL, infoDetail);
//                    getActivity().startActivity(intent);
//                }
//                break;
        }
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

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerDescSuccess(SingerDescriptionBean bean) {
        hideDialog();
        Log.d(TAG, "onGetSingerDescSuccess :" + bean);
        descBean = bean;
        if(bean.getIntroduction().size()!=0){
            binding.tvSingername.setText(bean.getIntroduction().get(0).getTi());
            binding.tvDesc.setText(bean.getIntroduction().get(0).getTxt());
        }else {
            binding.tvSingername.setText("");
            binding.tvDesc.setText("");
        }
    }

    @Override
    public void onGetSingerDescFail(String e) {
        hideDialog();
        Log.e(TAG, "onGetSingerDescFail : " + e);
        ToastUtils.showShort(e);
    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {
        hideDialog();
        Log.d(TAG, "onGetSimiSingerSuccess :" + bean + " size:" + bean.getArtists().size());
        simiList.clear();
        simiList.addAll(bean.getArtists());
        addDataToAdapter();
    }

    private void addDataToAdapter() {
        adapter.loadMore(simiList);
    }

    @Override
    public void onGetSimiSingerFail(String e) {
        hideDialog();
        Log.e(TAG, "onGetSimiSingerFail : " + e);
        XToastUtils.error(e);
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
