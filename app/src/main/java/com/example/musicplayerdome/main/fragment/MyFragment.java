package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.MyfragmentBinding;
import com.example.musicplayerdome.main.other.WowPresenter;

public class MyFragment extends BaseFragment<WowPresenter> {
    MyfragmentBinding binding;

    public MyFragment() {
        setFragmentTitle("更 多");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.myfragment,container,false);
        return binding.getRoot();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    public WowPresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}
