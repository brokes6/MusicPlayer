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
import com.example.musicplayerdome.databinding.FragmentRecommendedvideoBinding;
import com.example.musicplayerdome.main.adapter.RecommemdedVideoAdapter;
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

public class RecommendedVideoFtagment extends BaseFragment<RecommendedPresenter> implements RecommendedContract.View {
    private static final String TAG = "RecommendedVideoFtagmen";
    FragmentRecommendedvideoBinding binding;
    RecommemdedVideoAdapter adapter;
    private List<RecommendedVideoBean.DatasData> videobean = new ArrayList<>();
    private List<MusicCommentBean> CommentList = new ArrayList<>();
    private boolean first = true;
    public RecommendedVideoFtagment() {
        setFragmentTitle("推荐");
    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recommendedvideo,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new RecommemdedVideoAdapter(getContext());
        adapter.setListener(itemClickListener);
        binding.RVideo.setAdapter(adapter);
        binding.RVideo.setLayoutManager(layoutManager);
        binding.RVideo.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.R_jz_video);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
        showDialog();
        mPresenter.getRecommendedVideos();

    }
    @Override
    protected void initView() {
        //设置 Header式
        binding.RSwipeRefresh.setRefreshHeader(new MaterialHeader(getContext()));
        //取消Footer
        binding.RSwipeRefresh.setEnableLoadMore(false);
        binding.RSwipeRefresh.setDisableContentWhenRefresh(true);

        binding.RSwipeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: 视频开始刷新");
                first = false;
                mPresenter.getRecommendedVideos();
            }
        });
    }

    RecommemdedVideoAdapter.RecommemdedVideoItemClickListener itemClickListener = new RecommemdedVideoAdapter.RecommemdedVideoItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            Log.e(TAG, "onPlayListItemClick: 当前id为"+videobean.get(position).getVData().getVid() );
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
        hideDialog();
        if (first){
            first = false;
            adapter.loadMore(bean.getDatas());
            videobean.addAll(bean.getDatas());
        }else{
            videobean.clear();
            videobean.addAll(bean.getDatas());
            adapter.refresh(bean.getDatas());
            binding.RSwipeRefresh.finishRefresh(true);
        }

    }

    @Override
    public void onRecommendedVideosFail(String e) {
        hideDialog();
        Log.e(TAG, "获取推荐视频错误"+e );
        XToastUtils.error("网络请求失败，请检查网络再试");
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
        Log.e(TAG, "视频评论获取错误"+e );
    }

    @Override
    public void onGetGroupVideosSuccess(RecommendedVideoBean bean) {

    }

    @Override
    public void onGetGroupVideosFail(String e) {

    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
