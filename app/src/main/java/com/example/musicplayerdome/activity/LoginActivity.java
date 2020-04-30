package com.example.musicplayerdome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.databinding.ActivityLoginBinding;
import com.example.musicplayerdome.object.BaseActivity;
import com.example.musicplayerdome.util.PrivacyUtils;
import com.example.musicplayerdome.util.SettingSPUtils;
import com.example.musicplayerdome.util.XToastUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.login.setOnClickListener(this);
        initView();
    }
    private void initView(){
        SettingSPUtils spUtils = new SettingSPUtils(LoginActivity.this);
        Log.d(TAG, "initView: ================"+spUtils);
        if (!spUtils.isAgreePrivacy()) {
            PrivacyUtils.showPrivacyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                spUtils.setIsAgreePrivacy(true);
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                ActivityUtils.startActivity(HomeActivity.class);
                XToastUtils.success("登录成功");
                break;
        }
    }
    @Override
    public void onBackPressed() {
        AppUtils.exitApp();
    }
}
