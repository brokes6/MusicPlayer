package com.example.musicplayerdome.collection.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.CollectionContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.collection.adapter.CSingersAdapter;
import com.example.musicplayerdome.collection.other.CollectionPresenter;
import com.example.musicplayerdome.databinding.CSingersFragmentBinding;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.song.adapter.MySongListAdapter;

public class CollectSingersFragment extends BaseFragment<CollectionPresenter> implements CollectionContract.View {
    CSingersFragmentBinding binding;
    CSingersAdapter adapter;
    public CollectSingersFragment(){
        setFragmentTitle("收藏的歌手");
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_singers_fragment,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        adapter = new CSingersAdapter(getContext());
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.cSingers.setLayoutManager(lm);
        binding.cSingers.setAdapter(adapter);

        showDialog();
        mPresenter.getArtistSublist();
    }

    @Override
    protected void initView() {

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
        hideDialog();
        adapter.loadMore(bean.getData());
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
