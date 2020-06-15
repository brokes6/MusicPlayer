package com.example.musicplayerdome.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.rewrite.SearchEditText;
import com.example.musicplayerdome.util.LoadingsDialog;
import com.example.musicplayerdome.util.LocaleManageUtil;
import com.lzx.starrysky.manager.MediaSessionConnection;
import com.lzx.starrysky.model.SongInfo;


/**
 * Created by Rikka on 2019/7/12
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";

    public static final String SONG_URL = "http://music.163.com/song/media/outer/url?id=";

    protected P mPresenter;

    protected LoadingsDialog mDialogs;

    public Context mContext;
    private SongInfo bottomSongInfo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManageUtil.setLocal(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (onCreatePresenter() != null) {
            mPresenter = onCreatePresenter();
        }
        mContext = this;
        onCreateView(savedInstanceState);
        initModule();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        System.gc();
        if (mPresenter != null) {
            mPresenter = null;
        }
//        if (mDialog!=null){
//            mDialog.recycle();
//            mDialog = null;
//        }
        super.onDestroy();
    }

    //
    public void goDialog(){
        if (mDialogs == null) {
            mDialogs = LoadingsDialog.getInstance(this);
        }else{
            return;
        }
    }

    protected abstract void onCreateView(Bundle savedInstanceState);

    protected abstract P onCreatePresenter();

    protected abstract void initModule();

    protected abstract void initData();


    public void startActivity(Class<? extends AppCompatActivity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    public void connectMusicService() {
        MediaSessionConnection.getInstance().connect();
    }

    public void disconnectMusicService() {
        MediaSessionConnection.getInstance().disconnect();
    }

    public void setBackBtn(String color) {
        ImageView backBtn = findViewById(R.id.iv_back);
        backBtn.setVisibility(View.VISIBLE);
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.shape_back, getTheme());
        //你需要改变的颜色
        vectorDrawableCompat.setTint(Color.parseColor(color));
        backBtn.setImageDrawable(vectorDrawableCompat);
        backBtn.setOnClickListener(v -> {
            System.gc();
            onBackPressed();
        });
    }

    public void setLeftTitleText(int resId) {
        TextView leftTitle = findViewById(R.id.tv_left_title);
        leftTitle.setVisibility(View.VISIBLE);
        leftTitle.setText(resId);
    }

    public void setLeftTitleText(String titleText,String textColor) {
        TextView leftTitle = findViewById(R.id.tv_left_title);
        leftTitle.setVisibility(View.VISIBLE);
        leftTitle.setText(titleText);
        leftTitle.setTextColor(Color.parseColor(textColor));
    }

    public void setLeftTitleTextGone() {
        TextView leftTitle = findViewById(R.id.tv_left_title);
        leftTitle.setVisibility(View.GONE);
    }

    public void setLeftTitleTextColorWhite() {
        TextView leftTitle = findViewById(R.id.tv_left_title);
        leftTitle.setTextColor(Color.parseColor("#ffffff"));
    }

    public void setLeftTitleAlpha(float alpha) {
        TextView leftTitle = findViewById(R.id.tv_left_title);
        leftTitle.setVisibility(View.VISIBLE);
        leftTitle.setAlpha(alpha);
    }

    public void setRightSearchButton() {
        TextView btnSearch = findViewById(R.id.btn_search);
        btnSearch.setVisibility(View.VISIBLE);
    }

    public void setEditText(String textColor) {
        SearchEditText etSearch = findViewById(R.id.et_search);
        etSearch.setVisibility(View.VISIBLE);
        etSearch.setEditTextColor(textColor);
    }

    public void setSongInfo(String songName, String singerName) {
        RelativeLayout rlSong = findViewById(R.id.rl_song_info);
        rlSong.setVisibility(View.VISIBLE);
        TextView tvSongName = findViewById(R.id.tv_songname);
        TextView tvSingerName = findViewById(R.id.tv_singername);
        tvSongName.setText(songName);
        tvSingerName.setText(singerName);
    }
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
    /**
     * 用来防止状态栏和控件重叠在一起（设置控件的Margins值）
     * @param v 传递进来最上方的控件
     * @param l 左
     * @param t 上
     * @param r 右
     * @param b 下
     */
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void showDialog() {
        mDialogs.show();
    }

    public void hideDialog() {
        mDialogs.dismiss();
    }
}
