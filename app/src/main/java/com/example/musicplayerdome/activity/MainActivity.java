package com.example.musicplayerdome.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.OnItemListenter;
import com.example.musicplayerdome.adapter.MainMusicAdapter;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.ActivityMainBinding;
import com.example.musicplayerdome.object.BaseActivity;
import com.example.musicplayerdome.resources.MusicURL;
import com.xuexiang.xui.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private MainMusicAdapter mainMusicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setMusicList();
        initView();
    }
    private void initView(){
        binding.musicLogotext.setOnClickListener(this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(lm);
        binding.recyclerView.setAdapter(mainMusicAdapter = new MainMusicAdapter(this));
        /**
         * 回调监听也行，但没必要，只需要在加个参数用来判断就行了
         * 通过抽象类来回调监听
         * 这边才是真正的方法
         */
        mainMusicAdapter.setOnItemClickListener(new OnItemListenter() {
            @Override
            public void onItemClick(View view, int postionid) {
                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                intent.putExtra ("sid",postionid);
                intent.putExtra ("skey",true);
                startActivity(intent);
            }
        });
        mainMusicAdapter.loadMore(audioList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_logotext:
                ActivityUtils.startActivity(MusicActivity.class);
                break;
        }
    }

    private long firstTime = 0;
    private List<Audio> audioList = new ArrayList<>();
    private List<String> fileArr = new ArrayList<>();
    private void setMusicList() {
        MusicURL musicURL = new MusicURL();
        fileArr = musicURL.getMusicURL();
        for (int i = 0; i < fileArr.size(); i++) {
            Audio audio = new Audio();
            audio.setFileUrl(fileArr.get(i));
            audio.setId(i + 1);
            audio.setType(1);
            audio.setName("音乐" + (i + 1));
            audioList.add(audio);
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
