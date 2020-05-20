package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MvContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.MyfragmentBinding;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.other.MvPresenter;
import com.example.musicplayerdome.main.other.WowPresenter;

public class MyFragment extends BaseFragment<MvPresenter> implements MvContract.View {
    MyfragmentBinding binding;
    private static final String TAG = "MyFragment";

    public MyFragment() {
        setFragmentTitle("云 村");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.myfragment,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        mPresenter.getRecommendMV();
    }

    @Override
    public MvPresenter onCreatePresenter() {
        return new MvPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetRecommendMVSuccess(MvSublistBean bean) {
        Log.e(TAG, "onGetRecommendMVSuccess: 推荐mv数据为"+bean);
    }

    @Override
    public void onGetRecommendMVFail(String e) {

    }
}
