package com.example.musicplayerdome.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MvContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.MyfragmentBinding;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.other.MvPresenter;
import com.example.musicplayerdome.main.other.WowPresenter;

import java.util.Collection;

import yuncun.adapter.YuncunAdapter;
import yuncun.bean.YuncunReviewBean;

public class YuncunFragment extends BaseFragment<MvPresenter> implements MvContract.View {
    MyfragmentBinding binding;
    private static final String TAG = "YuncunFragment";
    private YuncunAdapter adapter;

    public YuncunFragment() {
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
        mPresenter.getYuncun();
        //垂直方向的2列
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        binding.recyclerView.setLayoutManager(layoutManager);
        final int spanCount = 2;
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int[] first = new int[spanCount];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
        adapter = new YuncunAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        showDialog();
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
    }

    @Override
    public void onGetRecommendMVFail(String e) {

    }

    @Override
    public void onGetYuncunSuccess(YuncunReviewBean bean) {
        hideDialog();
        adapter.loadMore(bean.getData());
    }

    @Override
    public void onGetYuncunFail(String e) {

    }
}
