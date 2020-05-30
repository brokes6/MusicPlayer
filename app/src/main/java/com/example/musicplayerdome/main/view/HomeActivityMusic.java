package com.example.musicplayerdome.main.view;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.Constants;
import com.example.musicplayerdome.abstractclass.MainContract;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.ActivityHomeBinding;
import com.example.musicplayerdome.main.fragment.HomeFragment;
import com.example.musicplayerdome.main.fragment.YuncunFragment;
import com.example.musicplayerdome.main.fragment.SongSheetFragment;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.other.MainPresenter;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.ActivityStarter;
import com.example.musicplayerdome.util.GsonUtil;
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
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
        connectMusicService();
        mPagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragmentList.add(new SongSheetFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new YuncunFragment());
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
        setMargins(binding.tabBackground,0,getStatusBarHeight(this),0,0);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private final int A3A3 = 0xFF3A3A3A, While = 0xFFFFFF, red = 0xFFdb2b1c;
    private int text;
    private void initApadter(){
        binding.viewpager.setAdapter(mPagerAdapter);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setOffscreenPageLimit(fragmentList.size()-1);
        mPagerAdapter.getItem(1).setUserVisibleHint(true);
        binding.tablayoutReal.setupWithViewPager(binding.viewpager);
        binding.tablayoutReal.setTabTextColors(Color.parseColor("#C6B3B3"), Color.parseColor("#FFFDFD"));
        binding.tablayoutReal.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectTextBoldAndBig(tab);
                if (tab.getText().equals("云 村")){
                    ImmersionBar.with(HomeActivityMusic.this).statusBarDarkFont(true).init();
                    binding.ivSearch.setImageResource(R.drawable.shape_search_gray);
                    binding.icNav.setImageResource(R.drawable.shape_drawer_gray);
                }else{
                    ImmersionBar.with(HomeActivityMusic.this).statusBarDarkFont(false).init();
                    binding.ivSearch.setImageResource(R.drawable.shape_search);
                    binding.icNav.setImageResource(R.drawable.shape_drawer);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArgbEvaluator evaluator = new ArgbEvaluator(); // ARGB求值器
                int evaluate = red; // 初始默认颜色
                if (position == 0) {
                    evaluate = (Integer) evaluator.evaluate(positionOffset, A3A3, red); // 根据positionOffset和第0页~第1页的颜色转换范围取颜色值
                } else if (position == 1) {
                    evaluate = (Integer) evaluator.evaluate(positionOffset, red, While); // 根据positionOffset和第1页~第2页的颜色转换范围取颜色值
                } else if (position == 2) {
                    evaluate = (Integer) evaluator.evaluate(positionOffset, While, While); // 根据positionOffset和第2页~第3页的颜色转换范围取颜色值
                    ImmersionBar.with(HomeActivityMusic.this).statusBarDarkFont(true);
                } else {
                    evaluate = red; // 最终第3页的颜色
                }
                binding.tablayoutReal.setBackgroundColor(evaluate);// 切换底部导航栏和toolbar的颜色。
                binding.mtop.setBackgroundColor(evaluate);//整体activity的颜色
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setSelectTextBoldAndBig(TabLayout.Tab tab) {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.design_layout_tab_text, null);
        textView.setText(tab.getText());
        textView.setScaleY(1.5f);
        textView.setScaleX(1.5f);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if (tab.getText().equals("云 村")){
            textView.setTextColor(Color.parseColor("#000000"));
        }else{
            textView.setTextColor(Color.parseColor("#FFFDFD"));
        }
        tab.setCustomView(textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int key = (int) SharedPreferencesUtil.getData("Ykey",0);
        if (key!=3){
            if (SongPlayManager.getInstance().isPlaying()) {
                binding.bottomController.setVisibility(View.VISIBLE);
            } else {
                binding.bottomController.setVisibility(View.GONE);
            }
        }else{
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
