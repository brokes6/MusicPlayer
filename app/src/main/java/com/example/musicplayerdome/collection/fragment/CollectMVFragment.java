package com.example.musicplayerdome.collection.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.CollectionContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.collection.adapter.CollectionMvAdapter;
import com.example.musicplayerdome.collection.other.CollectionPresenter;
import com.example.musicplayerdome.databinding.CMvFragmentBinding;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.song.adapter.SongMvAdapter;
import com.example.musicplayerdome.song.view.SongMvActivity;

public class CollectMVFragment extends BaseFragment<CollectionPresenter> implements CollectionContract.View {
    CMvFragmentBinding binding;
    private CollectionMvAdapter adapter;
    private MvSublistBean mvSublistBean;
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
        adapter = new CollectionMvAdapter(getContext());
        adapter.setListener(listClickListener);
        binding.CMVRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.CMVRecyclerView.setAdapter(adapter);

        showDialog();
        mPresenter.getMvSublist();
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

    private CollectionMvAdapter.OnSimiSingerClickListener listClickListener = position -> {
        Intent intent = new Intent(getContext(), SongMvActivity.class);
        intent.putExtra(SongMvActivity.MVSONG_INFO, Long.valueOf(mvSublistBean.getData().get(position).getVid()));
        getContext().startActivity(intent);


    };

    @Override
    public void onGetArtistSublistSuccess(ArtistSublistBean bean) {

    }

    @Override
    public void onGetArtistSublistFail(String e) {

    }

    @Override
    public void onGetMvSublistSuccess(MvSublistBean bean) {
        hideDialog();
        mvSublistBean = bean;
        adapter.loadMore(bean.getData());
    }

    @Override
    public void onGetMvSublistFail(String e) {

    }
}
