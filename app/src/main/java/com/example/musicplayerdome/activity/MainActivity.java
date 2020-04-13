package com.example.musicplayerdome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.databinding.ActivityMainBinding;
import com.example.musicplayerdome.object.BaseActivity;
import com.xuexiang.xui.utils.SnackbarUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        initView();
    }
    private void initView(){
        binding.musicLogotext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_logotext:
                ActivityUtils.startActivity(MusicActivity.class);
                break;
        }
    }

    public void initSharedPreferences(boolean k){
        SharedPreferences sharedPreferences= getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("connect",k);
        editor.commit();
    }
    public boolean getSharedPreferences(){
        SharedPreferences sharedPreferences= getSharedPreferences("key", Context .MODE_PRIVATE);
        Boolean connect = sharedPreferences.getBoolean("connect",false);
        return connect;
    }

    private long firstTime = 0;


//    @Override
//    public void onBackPressed() {
//        long secondTime = System.currentTimeMillis();
//        if (secondTime - firstTime > 2000) {
//            SnackbarUtils.Short(binding.mainL, "再按一次退出").info().show();
//            firstTime = secondTime;
//        } else{
//            ActivityUtils.finishAllActivities();
//        }
//    }
}
