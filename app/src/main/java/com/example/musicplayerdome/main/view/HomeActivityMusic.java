package com.example.musicplayerdome.main.view;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.Constants;
import com.example.musicplayerdome.abstractclass.MainContract;
import com.example.musicplayerdome.databinding.SidebarMainBinding;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.main.fragment.HomeFragment;
import com.example.musicplayerdome.main.fragment.VideoFragment;
import com.example.musicplayerdome.main.fragment.YuncunFragment;
import com.example.musicplayerdome.main.fragment.SongSheetFragment;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.main.other.MainPresenter;
import com.example.musicplayerdome.personal.view.PersonalActivity;
import com.example.musicplayerdome.rewrite.BottomSongPlayBar;
import com.example.musicplayerdome.search.view.SearchActivity;
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

import static com.example.musicplayerdome.personal.view.PersonalActivity.USER_ID;

/**
 * 也就是MainAcitiviy，主页面
 * 分为3个Tab，展示3个不同页面
 */
public class HomeActivityMusic extends BaseActivity<MainPresenter> implements MainContract.View{
    private static final String TAG = "HomeActivity";
    private SidebarMainBinding Sbinding;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private MultiFragmentPagerAdapter mPagerAdapter;
    private long firstTime = 0;
    private ImageView icNav,ivSearch;
    private ViewPager viewpager;
    private BottomSongPlayBar bottomController;
    private TabLayout tablayoutReal;
    private LinearLayout tabBackground,mainL;
    //用户信息
    private LoginBean loginBean;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        Sbinding = DataBindingUtil.setContentView(this,R.layout.sidebar_main);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
        connectMusicService();
        mPagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragmentList.add(new SongSheetFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new YuncunFragment());
        fragmentList.add(new VideoFragment());
        mPagerAdapter.init(fragmentList);
    }
    @Override
    protected void initData() {
        String userLoginInfo = SharePreferenceUtil.getInstance(this).getUserInfo("");
        loginBean = GsonUtil.fromJSON(userLoginInfo, LoginBean.class);

        initSidebar(loginBean);
        initApadter();
        Log.e(TAG, "当前用户id为："+loginBean.getAccount().getId());
        mPresenter.getLikeList(loginBean.getAccount().getId());
    }

    @Override
    protected void initView(){
        icNav = findViewById(R.id.ic_nav);
        ivSearch = findViewById(R.id.iv_search);
        viewpager = findViewById(R.id.viewpager);
        bottomController = findViewById(R.id.bottom_controller);
        tablayoutReal = findViewById(R.id.tablayoutReal);
        tabBackground = findViewById(R.id.tabBackground);
        mainL = findViewById(R.id.mainL);

        Sbinding.ivAvatar.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        icNav.setOnClickListener(this);
        Sbinding.rlLogout.setOnClickListener(this);
        setMargins(tabBackground,0,getStatusBarHeight(this),0,0);
    }
    private void initSidebar(LoginBean bean){
        if (bean.getProfile().getAvatarUrl() != null) {
            String avatarUrl = bean.getProfile().getAvatarUrl();
            Glide.with(this).load(avatarUrl).into(Sbinding.ivAvatar);
        }
        if (bean.getProfile().getNickname() != null) {
            Sbinding.tvUsername.setText(bean.getProfile().getNickname());
        }
    }

    private final int A3A3 = 0xFF3A3A3A, While = 0xFFFFFF, red = 0xFFdb2b1c;
    private void initApadter(){
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setCurrentItem(1);
        viewpager.setOffscreenPageLimit(fragmentList.size()-1);
        mPagerAdapter.getItem(1).setUserVisibleHint(true);
        tablayoutReal.setupWithViewPager(viewpager);
        tablayoutReal.setTabTextColors(Color.parseColor("#C6B3B3"), Color.parseColor("#FFFDFD"));
        tablayoutReal.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectTextBoldAndBig(tab);
                if (tab.getText().equals("云 村") || tab.getText().equals("视 频")){
                    ImmersionBar.with(HomeActivityMusic.this).statusBarDarkFont(true).init();
                    ivSearch.setImageResource(R.drawable.shape_search_gray);
                    icNav.setImageResource(R.drawable.shape_drawer_gray);
                }else{
                    ImmersionBar.with(HomeActivityMusic.this).statusBarDarkFont(false).init();
                    ivSearch.setImageResource(R.drawable.shape_search);
                    icNav.setImageResource(R.drawable.shape_drawer);
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
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    evaluate = While; // 最终第3页的颜色
                }
                tabBackground.setBackgroundColor(evaluate);// 切换底部导航栏和toolbar的颜色。
                mainL.setBackgroundColor(evaluate);//整体activity的颜色
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
        if (tab.getText().equals("云 村") || tab.getText().equals("视 频")){
            textView.setTextColor(Color.parseColor("#000000"));
        }else{
            textView.setTextColor(Color.parseColor("#FFFDFD"));
        }
        tab.setCustomView(textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_nav:
                Sbinding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.rl_logout:
                showDialog();
                if (SongPlayManager.getInstance().isPlaying()) {
                    SongPlayManager.getInstance().cancelPlay();
                }
                mPresenter.logout();
                break;
            case R.id.iv_search:
                ActivityUtils.startActivity(SearchActivity.class);
                break;
            case R.id.iv_avatar:
                Intent intent2 = new Intent(this, PersonalActivity.class);
                intent2.putExtra(USER_ID,loginBean.getAccount().getId());
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int key = (int) SharedPreferencesUtil.getData("Ykey",0);
        if (key!=3){
            if (SongPlayManager.getInstance().isDisplay()) {
                bottomController.setVisibility(View.VISIBLE);
            } else {
                bottomController.setVisibility(View.GONE);
            }
        }else{
            bottomController.setVisibility(View.GONE);
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
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            SnackbarUtils.Short(mainL, "再按一次退出").info().show();
            firstTime = secondTime;
        } else{
//            ActivityUtils.finishAllActivities();
            System.exit(0);// 完全退出应用
        }
    }
}
