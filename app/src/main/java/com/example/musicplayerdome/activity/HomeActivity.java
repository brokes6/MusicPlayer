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
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.ActivityHomeBinding;
import com.example.musicplayerdome.fragment.HomeFragment;
import com.example.musicplayerdome.fragment.MyFragment;
import com.example.musicplayerdome.fragment.SongSheetFragment;
import com.example.musicplayerdome.object.BaseActivity;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "HomeActivity";
    ActivityHomeBinding binding;
    private String[] strings = new String[]{"歌 单","主 页","我 的"};
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private long firstTime = 0;
    private int Sid;
    //音频播放类
    private Audio audio;
    private boolean go = false;
    private Intent intent;
    private HomeBroadcastReceiver bReceiver;
    /**
     * 上一首 按钮点击 ID
     */
    private final static int BUTTON_PREV_ID = 1;
    /**
     * 播放/暂停 按钮点击 ID
     */
    private final static int BUTTON_PALY_ID = 2;
    /**
     * 下一首 按钮点击 ID
     */
    private final static int BUTTON_NEXT_ID = 3;
    private final static String INTENT_BUTTONID_TAG = "ButtonId";
    private final static String ACTION_BUTTON = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClick";
    private final static String ACTIONS = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClickS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        fragmentList.add(new SongSheetFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MyFragment());
        initView();
        initApadter();
        initBroadcastReceiver();
    }
    private void initView(){
        binding.btnCustomPlay.setOnClickListener(this);
        binding.btnCustomNext.setOnClickListener(this);
        binding.btnCustomPrev.setOnClickListener(this);
        binding.PlaybackController.setOnClickListener(this);
    }
    private void initApadter(){
        MyAdapter fragmentAdater = new MyAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(fragmentAdater);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setOffscreenPageLimit(fragmentList.size()-1);
        binding.tablayoutReal.setupWithViewPager(binding.viewpager);
        binding.tablayoutReal.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("歌 单")){
                    binding.tabBackground.setBackgroundResource(R.color.A3A3);
                }
                if (tab.getText().equals("主 页")){
                    binding.tabBackground.setBackgroundResource(R.color.white);
                }
                if (tab.getText().equals("我 的")){
                    binding.tabBackground.setBackgroundResource(R.color.BCD4);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void initBroadcastReceiver(){
        bReceiver = new HomeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTIONS);
        registerReceiver(bReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_custom_prev:
                //上一首
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
                sendBroadcast(intent);
                break;
            case R.id.btn_custom_play:
                //播放
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
                sendBroadcast(intent);
                break;
            case R.id.btn_custom_next:
                //下一首
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
                sendBroadcast(intent);
                break;
            case R.id.Playback_controller:
                ActivityUtils.startActivity(MusicActivity.class);
                break;
        }
    }

    /**
     * 显示音频标题
     */
    private void addAudioTitle(String name,String author) {
        if (name == null) return;
        //设置音乐名称
        MyUtil.setText(binding.tvCustomSongSinger, name);
        MyUtil.setText(binding.tvCustomSongAuthor,author);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (go == true)return;
        go = (boolean)SharedPreferencesUtil.getData("go",false);
        Log.e(TAG, "获取成功"+go);
        if (go ==true){
            String name = (String)SharedPreferencesUtil.getData("name","");
            String author = (String)SharedPreferencesUtil.getData("author","");
            addAudioTitle(name,author);
            binding.PlaybackController.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 主页Tab列表适配器
     */
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
    //对设置图片进行方便的封装
    private void setImg(ImageView imageView, int imgRes) {
        if (imageView == null) return;
        imageView.setImageResource(imgRes);
    }
    public class HomeBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTIONS)) {
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                Sid = intent.getIntExtra("sid",0);
                switch (buttonId) {
                    case 1:
                    case 4:
                    case 5:
                        String name = intent.getStringExtra("name");
                        String author = intent.getStringExtra("author");
                        addAudioTitle(name,author);
                        break;
                    case 2://播放或暂停
                        setImg(binding.btnCustomPlay,R.mipmap.audio_state_play);
                        break;
                    case 3:
                        setImg(binding.btnCustomPlay,R.mipmap.audio_state_pause);
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bReceiver);
        super.onDestroy();
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
