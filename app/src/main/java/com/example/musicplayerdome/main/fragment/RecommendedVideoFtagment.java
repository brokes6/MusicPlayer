package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.RecommendedContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.databinding.FragmentRecommendedvideoBinding;
import com.example.musicplayerdome.main.adapter.RecommemdedVideoAdapter;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.main.other.RecommendedPresenter;

public class RecommendedVideoFtagment extends BaseFragment<RecommendedPresenter> implements RecommendedContract.View {
    FragmentRecommendedvideoBinding binding;
    RecommemdedVideoAdapter adapter;
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
        showDialog();
        //这里初始化适配器
        //这里请求数据
    }

    @Override
    public RecommendedPresenter onCreatePresenter() {
        //这里绑定请求接口
        return new RecommendedPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {
        //这个不用管，继承自带的方法
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRecommendedVideosSuccess(RecommendedVideoBean bean) {
        hideDialog();
        //这里是请求成功之后返回给你结果
        //你需要把返回的数据，用adapter.loadMore()放入适配器中
    }

    @Override
    public void onRecommendedVideosFail(String e) {
        hideDialog();
        //这里是请求失败的地方，会返回给你错误
    }
}
