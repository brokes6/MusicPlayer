package com.example.musicplayerdome.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayerdome.R;
import com.gyf.immersionbar.ImmersionBar;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by dingxingxiang
 * 基本Activity 继承类
 * 所有的activity都应继承它，方便管理
 */
public abstract class MusicBaseActivity extends AppCompatActivity{
    private static final String TAG = "BaseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .init();
    }


    private KProgressHUD dialog;
    //页面是否处于前台
    boolean isFront = false;
    int Rid;

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    protected void showDialog() {
        if (!isFront) return;
        try {
            if (dialog == null) {
                dialog = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
            } else {
                dialog.setLabel(null);
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
