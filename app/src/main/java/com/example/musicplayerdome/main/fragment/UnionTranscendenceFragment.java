package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
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
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.scwang.smartrefresh.header.MaterialHeader;

import cn.jzvd.Jzvd;

public class UnionTranscendenceFragment extends BaseFragment<RecommendedPresenter> implements RecommendedContract.View {
    FragmentUnionTranscendenceBinding binding;
    private GroupVideoAdapter adapter;
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
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
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
        //设置 Header式
        binding.smartrafresh.setRefreshHeader(new MaterialHeader(getContext()));
        //取消Footer
        binding.smartrafresh.setEnableLoadMore(false);
        binding.smartrafresh.setDisableContentWhenRefresh(true);

        showDialog();
        mPresenter.getGroupVideos(259129);
    }

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

    }

    @Override
    public void onGetVideoCommentFail(String e) {

    }

    @Override
    public void onGetGroupVideosSuccess(RecommendedVideoBean bean) {
        hideDialog();
        adapter.loadMore(bean.getDatas());
    }

    @Override
    public void onGetGroupVideosFail(String e) {

    }

}
