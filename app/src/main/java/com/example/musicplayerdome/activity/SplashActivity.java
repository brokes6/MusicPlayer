package com.example.musicplayerdome.activity;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.util.SettingSPUtils;
import com.example.musicplayerdome.util.UserGuideActivity;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;

/**
 * 渐近式启动页
 *
 * @author xuexiang
 * @since 2018/11/27 下午5:24
 */
public class SplashActivity extends BaseSplashActivity {
    private static final String TAG = "SplashActivity";
    public final static String KEY_IS_DISPLAY = "key_is_display";
    public final static String KEY_ENABLE_ALPHA_ANIM = "key_enable_alpha_anim";

    private boolean isDisplay = false;

    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }

    @Override
    public void onCreateActivity() {
        isDisplay = getIntent().getBooleanExtra(KEY_IS_DISPLAY, isDisplay);
        boolean enableAlphaAnim = getIntent().getBooleanExtra(KEY_ENABLE_ALPHA_ANIM, false);
        SettingSPUtils spUtil = new SettingSPUtils(SplashActivity.this);
        if (spUtil.isFirstOpen()) {
            Log.d(TAG, "onCreateActivity: 11111111");
            spUtil.setIsFirstOpen(false);
            Intent intent = new Intent(SplashActivity.this, UserGuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "onCreateActivity: 222222"+spUtil.isFirstOpen());
                initSplashView(R.mipmap.splash);
            startSplash(enableAlphaAnim);
        }
    }

    @Override
    public void onSplashFinished() {
        if (!isDisplay) {
//            if (TokenUtils.hasToken()) {
//                ActivityUtils.startActivity(MainActivity.class);
//            } else {
//                ActivityUtils.startActivity(LoginActivity.class);
            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
//            }
        }
        finish();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }

}
