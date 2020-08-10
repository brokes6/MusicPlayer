package com.example.musicplayerdome.login.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.databinding.ActivitySelectLoginBinding;
import com.example.musicplayerdome.util.ClickUtil;
import com.example.musicplayerdome.util.ScreenUtils;
import com.example.musicplayerdome.util.XToastUtils;

/**
 * 选择登录方式页面
 */
public class SelectLoginActivity extends BaseActivity{
    private static final String TAG = "SelectLoginActivity";
    ActivitySelectLoginBinding binding;

    private long firstTime;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_login);
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        binding.btnPhoneLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.setStatusBarColor(this, Color.parseColor("#Db2C1F"));
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_phone_login:
                startActivity(LoginActivity.class, null, false);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 点击了返回按键
            exitApp(2000);// 退出应用
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用
     * 判断第二次点击退出的时间间隔
     * @param timeInterval
     */
    private void exitApp(long timeInterval) {
        if (System.currentTimeMillis() - firstTime >= timeInterval) {
            XToastUtils.info(R.string.press_exit_again);
            firstTime = System.currentTimeMillis();
        } else {
            finish();// 销毁当前activity
            System.exit(0);// 完全退出应用
        }
    }

}
