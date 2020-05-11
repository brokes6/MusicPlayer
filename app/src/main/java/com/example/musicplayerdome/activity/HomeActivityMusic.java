package com.example.musicplayerdome.activity;

import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.Constants;
import com.example.musicplayerdome.abstractclass.MainContract;
import com.example.musicplayerdome.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.ActivityHomeBinding;
import com.example.musicplayerdome.fragment.HomeFragment;
import com.example.musicplayerdome.fragment.MyFragment;
import com.example.musicplayerdome.fragment.SongSheetFragment;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.presenter.MainPresenter;
import com.example.musicplayerdome.song.SongPlayManager;
import com.example.musicplayerdome.util.ActivityStarter;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.xuexiang.xui.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityMusic extends BaseActivity<MainPresenter> implements View.OnClickListener, MainContract.View{
    private static final String TAG = "HomeActivity";
    ActivityHomeBinding binding;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private MultiFragmentPagerAdapter mPagerAdapter;
    private long firstTime = 0;
    //用户信息
    private LoginBean loginBean;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);

        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.red)
                .init();
        connectMusicService();
        mPagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragmentList.add(new SongSheetFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MyFragment());
        mPagerAdapter.init(fragmentList);
        initView();
    }
    @Override
    protected void initData() {
        String userLoginInfo = SharePreferenceUtil.getInstance(this).getUserInfo("");
        loginBean = GsonUtil.fromJSON(userLoginInfo, LoginBean.class);

        initApadter();
        mPresenter.getLikeList(loginBean.getAccount().getId());
    }
    private void initView(){

    }
    private void initApadter(){
        binding.viewpager.setAdapter(mPagerAdapter);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setOffscreenPageLimit(fragmentList.size()-1);
        mPagerAdapter.getItem(1).setUserVisibleHint(true);
        binding.tablayoutReal.setupWithViewPager(binding.viewpager);
        binding.tablayoutReal.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("歌 单")){
                    binding.tabBackground.setBackgroundResource(R.color.A3A3);
                    ImmersionBar.with(HomeActivityMusic.this)
                            .statusBarDarkFont(false)
                            .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                            .statusBarColor(R.color.A3A3)
                            .init();
                }
                if (tab.getText().equals("主 页")){
                    binding.tabBackground.setBackgroundResource(R.color.red);
                    ImmersionBar.with(HomeActivityMusic.this)
                            .statusBarDarkFont(false)
                            .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                            .statusBarColor(R.color.red)
                            .init();
                }
//                if (tab.getText().equals("我 的")){
//                    binding.tabBackground.setBackgroundResource(R.color.BCD4);
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SongPlayManager.getInstance().isPlaying()) {
            binding.bottomController.setVisibility(View.VISIBLE);
        } else {
            binding.bottomController.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLogoutSuccess() {
        hideDialog();
        SharePreferenceUtil.getInstance(this).remove(Constants.SpKey.AUTH_TOKEN);
        ActivityStarter.getInstance().startLoginActivity(this);
        this.finish();
    }

    @Override
    public void onLogoutFail(String e) {
        hideDialog();
    }

    @Override
    public void onGetLikeListSuccess(LikeListBean bean) {
        List<Long> idsList = bean.getIds();
        List<String> likeList = new ArrayList<>();
        for (int i = 0; i < idsList.size(); i++) {
            String ids = String.valueOf(idsList.get(i));
            likeList.add(ids);
        }
        SharePreferenceUtil.getInstance(this).saveLikeList(likeList);
    }

    @Override
    public void onGetLikeListFail(String e) {

    }

    @Override
    protected void onDestroy() {
        disconnectMusicService();
        super.onDestroy();
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void initModule() {

    }


    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            SnackbarUtils.Short(binding.mainL, "再按一次退出").info().show();
            firstTime = secondTime;
        } else{
//            ActivityUtils.finishAllActivities();
            System.exit(0);// 完全退出应用
        }
    }
}
