package com.example.musicplayerdome.collection.view;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.CollectionContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.collection.fragment.CollectMVFragment;
import com.example.musicplayerdome.collection.fragment.CollectSingersFragment;
import com.example.musicplayerdome.collection.other.CollectionPresenter;
import com.example.musicplayerdome.databinding.ActivityMyCollectionBinding;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏页面
 * 包括了我收藏的歌手，收藏的视频
 */
public class MyCollectionActivity extends BaseActivity<CollectionPresenter> implements CollectionContract.View {
    ActivityMyCollectionBinding binding;
    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_collection);

        ImmersionBar.with(this)
                .statusBarColor(R.color.red)
                .statusBarDarkFont(false)
                .init();

        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new CollectSingersFragment());
        fragments.add(new CollectMVFragment());
        pagerAdapter.init(fragments);
    }

    @Override
    protected CollectionPresenter onCreatePresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleText(getString(R.string.my_collection), getString(R.string.colorWhite));
        setMargins(binding.rlTitle,0,getStatusBarHeight(this)-5,0,0);

        binding.vpContainer.setAdapter(pagerAdapter);
        binding.vpContainer.setOffscreenPageLimit(2);
        binding.vpContainer.setCurrentItem(0);
        binding.tabType.setViewPager(binding.vpContainer);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SongPlayManager.getInstance().isDisplay()) {
            binding.bottomController.setVisibility(View.VISIBLE);
        } else {
            binding.bottomController.setVisibility(View.GONE);
        }
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