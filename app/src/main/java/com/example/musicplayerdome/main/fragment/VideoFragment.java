package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.databinding.FragmentVideoBinding;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends BaseFragment {
    FragmentVideoBinding binding;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private MultiFragmentPagerAdapter mPagerAdapter;
    public VideoFragment() {
        setFragmentTitle("视 频");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video,container,false);
        mPagerAdapter = new MultiFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        fragmentList.add(new RecommendedVideoFtagment());
        fragmentList.add(new UnionTranscendenceFragment());
        mPagerAdapter.init(fragmentList);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        binding.viewPager.setAdapter(mPagerAdapter);
        binding.viewPager.setCurrentItem(0);
        binding.viewPager.setOffscreenPageLimit(fragmentList.size()-1);
        binding.tablayout.setViewPager(binding.viewPager);
    }

    @Override
    protected void initView() {

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
