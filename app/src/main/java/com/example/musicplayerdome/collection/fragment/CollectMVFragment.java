package com.example.musicplayerdome.collection.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.CollectionContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.collection.other.CollectionPresenter;
import com.example.musicplayerdome.databinding.CMvFragmentBinding;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;

public class CollectMVFragment extends BaseFragment<CollectionPresenter> implements CollectionContract.View {
    CMvFragmentBinding binding;

    public CollectMVFragment(){
        setFragmentTitle("收藏的MV");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_mv_fragment,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        mPresenter.getMvSublist();
    }

    @Override
    public CollectionPresenter onCreatePresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetArtistSublistSuccess(ArtistSublistBean bean) {

    }

    @Override
    public void onGetArtistSublistFail(String e) {

    }

    @Override
    public void onGetMvSublistSuccess(MvSublistBean bean) {

    }

    @Override
    public void onGetMvSublistFail(String e) {

    }
}
