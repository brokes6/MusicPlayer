package com.example.musicplayerdome.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MvContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.MyfragmentBinding;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.other.MvPresenter;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.DisplayUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.example.musicplayerdome.yuncun.adapter.YuncunAdapter;
import com.example.musicplayerdome.yuncun.view.YuncunSongActivity;
import com.lzx.starrysky.model.SongInfo;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import yuncun.bean.YuncunReviewBean;

/**
 * HomeActivityMusic.YuncunFragment（Tab之一，云村）
 * 展示云村热评
 */
public class YuncunFragment extends BaseFragment<MvPresenter> implements MvContract.View {
    MyfragmentBinding binding;
    private static final String TAG = "YuncunFragment";
    private YuncunAdapter adapter;
    private List<YuncunReviewBean.UserData> userData = new ArrayList<>();
    private SongInfo msongInfo;

    public YuncunFragment() {
        setFragmentTitle("云 村");
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.myfragment,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        mPresenter.getYuncun();
        //垂直方向的2列
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        binding.recyclerView.setLayoutManager(layoutManager);
        final int spanCount = 2;
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int[] first = new int[spanCount];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
        adapter = new YuncunAdapter(getContext(), DisplayUtil.getScreenHeight(getActivity()));
        adapter.setListener(listener);
        binding.recyclerView.setAdapter(adapter);
        showDialog();
    }

    @Override
    protected void initView(){
        //设置 Header式
        binding.refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        //取消Footer
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setDisableContentWhenRefresh(true);
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "云村: 开始刷新");
                mPresenter.getYuncunAgain();
            }
        });
    }

    @Override
    public MvPresenter onCreatePresenter() {
        return new MvPresenter(this);
    }

    YuncunAdapter.OnYuncunListItemClickListener listener = new YuncunAdapter.OnYuncunListItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            msongInfo = new SongInfo();
            msongInfo.setSongId(String.valueOf(userData.get(position).getSimpleResourceInfo().getSongId()));
            msongInfo.setSongName(userData.get(position).getSimpleResourceInfo().getName());
            msongInfo.setSongUrl(SONG_URL + userData.get(position).getSimpleResourceInfo().getSongId() + ".mp3");
            msongInfo.setArtist(userData.get(position).getSimpleResourceInfo().getArtists().get(0).getName());
            msongInfo.setSongCover(userData.get(position).getSimpleResourceInfo().getSongCoverUrl());
            msongInfo.setDescription(userData.get(position).getContent());
            msongInfo.setAlbumArtist(userData.get(position).getSimpleUserInfo().getNickname());
            msongInfo.setAlbumCover(userData.get(position).getSimpleUserInfo().getAvatar());

            SongPlayManager.getInstance().clickASong(msongInfo);
            Log.e(TAG, "onPlayListItemClick: 当前id为"+msongInfo.getSongId());
            Intent intent = new Intent(getContext(), YuncunSongActivity.class);
            intent.putExtra(YuncunSongActivity.YUNSONG_INFO, msongInfo);
            getContext().startActivity(intent);
        }

        @Override
        public void onSmartPlayClick(int position) {

        }
    };

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetRecommendMVSuccess(MvSublistBean bean) {
    }

    @Override
    public void onGetRecommendMVFail(String e) {

    }

    @Override
    public void onGetYuncunSuccess(YuncunReviewBean bean) {
        hideDialog();
        adapter.loadMore(bean.getData());
        userData.clear();
        userData.addAll(bean.getData());
    }

    @Override
    public void onGetYuncunFail(String e) {
        hideDialog();
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

    @Override
    public void onGetgetYuncunAgainSuccess(YuncunReviewBean bean) {
        Log.e(TAG, "云村: 刷新成功");
        adapter.refresh(bean.getData());
        binding.refreshLayout.finishRefresh(true);
        userData.clear();
        userData.addAll(bean.getData());
    }

    @Override
    public void onGetYuncunAgainFail(String e) {
        XToastUtils.error("刷新求失败，请检查网络再试");
        binding.refreshLayout.finishRefresh(true);
    }
}
