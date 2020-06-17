package com.example.musicplayerdome.login.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.util.ActivityStarter;
import com.example.musicplayerdome.util.ScreenUtils;
import com.example.musicplayerdome.util.SharePreferenceUtil;


public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.setStatusBarColor(this, Color.parseColor("#Db2C1F"));
    }

    @Override
    protected void initData() {
        startCountDownTime();
    }

    private void startCountDownTime() {
        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                String authToken = SharePreferenceUtil.getInstance(SplashActivity.this).getAuthToken("");
                Log.e(TAG, "onFinish: 获取的结果为"+authToken);
                if (TextUtils.isEmpty(authToken)) {
                    ActivityStarter.getInstance().startLoginActivity(SplashActivity.this);
                } else {
                    ActivityStarter.getInstance().startMainActivity(SplashActivity.this);
                }
                SplashActivity.this.finish();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void initModule() {

    }


    @Override
    public void onClick(View v) {

    }
}
