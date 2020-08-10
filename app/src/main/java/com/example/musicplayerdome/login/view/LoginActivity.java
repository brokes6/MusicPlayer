package com.example.musicplayerdome.login.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.LoginContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivityLoginBinding;
import com.example.musicplayerdome.login.other.LoginPresenter;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.util.ActivityStarter;
import com.example.musicplayerdome.util.ClickUtil;
import com.example.musicplayerdome.util.InputUtil;
import com.example.musicplayerdome.util.PrivacyUtils;
import com.example.musicplayerdome.util.SettingSPUtils;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 登录界面
 * 只能手机号登录
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    String phoneNumber;
    String password;


    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
    }

    @Override
    protected void initView(){
        setMargins(binding.title,0,getStatusBarHeight(this),0,0);
        binding.btnLogin.setOnClickListener(this);
        binding.register.setOnClickListener(this);
        binding.forgetPwd.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.register.setOnClickListener(this);
        binding.forgetPwd.setOnClickListener(this);
        SettingSPUtils spUtils = new SettingSPUtils(LoginActivity.this);
        if (!spUtils.isAgreePrivacy()) {
            PrivacyUtils.showPrivacyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                spUtils.setIsAgreePrivacy(true);
            });
        }
    }
    @Override
    protected LoginPresenter onCreatePresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorBlack));
        setLeftTitleText(R.string.login_phone_number);
        if (!TextUtils.isEmpty(SharePreferenceUtil.getInstance(this).getAccountNum())) {
            phoneNumber = SharePreferenceUtil.getInstance(this).getAccountNum();
            binding.etPhone.setText(phoneNumber);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        //设置状态栏为白底黑字
        ImmersionBar.with(LoginActivity.this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_login:
                phoneNumber = binding.etPhone.getText().toString().trim();
                password = binding.etPwd.getText().toString().trim();
                if (InputUtil.checkMobileLegal(phoneNumber) && InputUtil.checkPasswordLegal(password)) {
                    showDialog();
                    Log.d(TAG, "login账号密码 : " + phoneNumber + " ," + password);
                    mPresenter.login(phoneNumber, password);
                }
                break;
            case R.id.register:
            case R.id.forget_pwd:
                XToastUtils.info(R.string.in_developing);
                break;
        }
    }

    @Override
    public void onLoginSuccess(LoginBean bean) {
        hideDialog();
        Log.d(TAG, "onLoginSuccess : " + bean);
        SharePreferenceUtil.getInstance(this).saveUserInfo(bean, phoneNumber);
        ActivityStarter.getInstance().startMainActivity(this);
        this.finish();
    }

    @Override
    public void onLoginFail(String error) {
        hideDialog();
        Log.w(TAG, "bean : " + error);
        if (error.equals("HTTP 502 Bad Gateway")) {
            XToastUtils.error(R.string.enter_correct_password);
        } else {
            XToastUtils.error(error);
        }
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }
}
