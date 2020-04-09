package com.example.musicplayerdome.object;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.utils.StatusBarUtils;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarLightMode(this);
    }
}
