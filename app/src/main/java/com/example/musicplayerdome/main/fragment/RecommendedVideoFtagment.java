package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.databinding.FragmentRecommendedvideoBinding;

public class RecommendedVideoFtagment extends BaseFragment {
    FragmentRecommendedvideoBinding binding;
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

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}
