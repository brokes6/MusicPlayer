package com.example.musicplayerdome.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.databinding.ActivityHomeBinding;
import com.example.musicplayerdome.fragment.HomeFragment;
import com.example.musicplayerdome.fragment.MyFragment;
import com.example.musicplayerdome.fragment.SongSheetFragment;
import com.example.musicplayerdome.object.BaseActivity;
import com.xuexiang.xui.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "HomeActivity";
    ActivityHomeBinding binding;
    private String[] strings = new String[]{"歌 单","主 页","我 的"};
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        fragmentList.add(new SongSheetFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MyFragment());
        initView();
        initApadter();
    }
    private void initView(){
        binding.btnCustomPlay.setOnClickListener(this);
        binding.btnCustomNext.setOnClickListener(this);
        binding.btnCustomPrev.setOnClickListener(this);
    }
    private void initApadter(){
        MyAdapter fragmentAdater = new MyAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(fragmentAdater);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setOffscreenPageLimit(fragmentList.size()-1);
        binding.tablayoutReal.setupWithViewPager(binding.viewpager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_custom_prev:
                //上一首

                break;
            case R.id.btn_custom_play:
                //播放

                break;
            case R.id.btn_custom_next:
                //下一首

                break;
        }
    }


    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return strings[position];
        }
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            SnackbarUtils.Short(binding.mainL, "再按一次退出").info().show();
            firstTime = secondTime;
        } else{
            ActivityUtils.finishAllActivities();
        }
    }
}
