package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.RecommendedContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.FragmentUnionTranscendenceBinding;
import com.example.musicplayerdome.main.adapter.GroupVideoAdapter;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.main.other.RecommendedPresenter;
import com.example.musicplayerdome.search.dialog.VideoReviewDialog;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.util.XToastUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class UnionTranscendenceFragment extends BaseFragment<RecommendedPresenter> implements RecommendedContract.View {
    private static final String TAG = "UnionTranscendenceFragm";
    FragmentUnionTranscendenceBinding binding;
    private GroupVideoAdapter adapter;
    private List<RecommendedVideoBean.DatasData> videobean = new ArrayList<>();
    private List<MusicCommentBean> CommentList = new ArrayList<>();
    private boolean first = true;
    public UnionTranscendenceFragment(){
        setFragmentTitle("超然联盟");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_union_transcendence,container,false);
        return binding.getRoot();
    }
    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new GroupVideoAdapter(getContext());
        adapter.setListener(listener);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.R_jz_video);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null && jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
        showDialog();
        mPresenter.getGroupVideos(259129);
    }
    @Override
    protected void initView() {
        //设置 Header式
        binding.smartrafresh.setRefreshHeader(new MaterialHeader(getContext()));
        //取消Footer
        binding.smartrafresh.setEnableLoadMore(false);
        binding.smartrafresh.setDisableContentWhenRefresh(true);

        binding.smartrafresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: 视频开始刷新");
                mPresenter.getGroupVideos(259129);
            }
        });
    }

    GroupVideoAdapter.GroupVideoItemClickListener listener = new GroupVideoAdapter.GroupVideoItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            mPresenter.getVideoComment(videobean.get(position).getVData().getVid());
        }
    };

    @Override
    public RecommendedPresenter onCreatePresenter() {
        return new RecommendedPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRecommendedVideosSuccess(RecommendedVideoBean bean) {

    }

    @Override
    public void onRecommendedVideosFail(String e) {

    }

    @Override
    public void onGetVideoCommentSuccess(MusicCommentBean bean) {
        CommentList.clear();
        CommentList.add(bean);
        VideoReviewDialog reviewDialog = new VideoReviewDialog(getContext(),CommentList);
        reviewDialog.setCanceledOnTouchOutside(true);
        reviewDialog.show();
    }

    @Override
    public void onGetVideoCommentFail(String e) {
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

    @Override
    public void onGetGroupVideosSuccess(RecommendedVideoBean bean) {
        hideDialog();
        if (first){
            first = false;
            adapter.loadMore(bean.getDatas());
            videobean.addAll(bean.getDatas());
        }else{
            videobean.clear();
            videobean.addAll(bean.getDatas());
            adapter.refresh(bean.getDatas());
            binding.smartrafresh.finishRefresh(true);
        }
    }

    @Override
    public void onGetGroupVideosFail(String e) {
        hideDialog();
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

}
