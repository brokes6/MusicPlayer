package com.example.musicplayerdome.personal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.collection.fragment.CollectMVFragment;
import com.example.musicplayerdome.collection.fragment.CollectSingersFragment;
import com.example.musicplayerdome.databinding.ActivityPersonalBinding;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.main.other.MinePresenter;
import com.example.musicplayerdome.main.view.HomeActivityMusic;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.personal.fragment.PersonalDynamicFragment;
import com.example.musicplayerdome.personal.fragment.PersonalSheetFragment;
import com.example.musicplayerdome.personal.other.UseridEvent;
import com.example.musicplayerdome.search.other.KeywordsEvent;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalActivity extends BaseActivity<MinePresenter> implements MineContract.View {
    ActivityPersonalBinding binding;
    public static final String USER_ID = "user_id";
    private long userid;
    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_personal);

        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .init();

        goDialog();
        userid = getIntent().getLongExtra(USER_ID,0);
        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new PersonalSheetFragment(getIntent().getLongExtra(USER_ID,0)));
        fragments.add(new PersonalDynamicFragment());
        pagerAdapter.init(fragments);
    }

    @Override
    protected MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initModule() {

    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorGray));
        setLeftTitleText(R.string.personal);
        setTitleBG(getString(R.string.colorPrimaryDark));
        getIntentData();

        binding.PContainer.setAdapter(pagerAdapter);
        binding.PContainer.setOffscreenPageLimit(2);
        binding.PContainer.setCurrentItem(0);
        binding.PTab.setViewPager(binding.PContainer);
    }

    private void getIntentData(){
        if (userid!=0){
            showDialog();
            mPresenter.getUserDetail(userid);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        int key = (int) SharedPreferencesUtil.getData("Ykey",0);
        if (key!=3){
            if (SongPlayManager.getInstance().isDisplay()) {
                binding.bottomController.setVisibility(View.VISIBLE);
            } else {
                binding.bottomController.setVisibility(View.GONE);
            }
        }else{
            binding.bottomController.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistFail(String e) {

    }

    @Override
    public void onGetUserPlaylistAgainSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistAgainFail(String e) {

    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {
        hideDialog();
        Glide.with(this).load(bean.getProfile().getAvatarUrl()).into(binding.PUserimg);
        binding.PUsername.setText(bean.getProfile().getNickname());
        binding.PFollows.setText("关注："+bean.getProfile().getFollows());
        binding.PFolloweds.setText("粉丝："+bean.getProfile().getFolloweds());
        binding.PGrade.setText("Lv."+bean.getLevel());
        if (bean.getProfile().getVipType()==0){
            binding.PVip.setText("普通用户");
        }else{
            binding.PVip.setText("黑胶CVIP");
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        date.setTime(bean.getProfile().getBirthday());
        binding.PAge.setText(sdf.format(date));
    }

    @Override
    public void onGetUserDetailFails(String e) {

    }

    @Override
    public void onGetPlayHistoryListSuccess(SonghistoryBean bean) {

    }

    @Override
    public void onGetPlayHistoryListFail(String e) {

    }

    @Override
    public void onGetIntelligenceListSuccess(PlayModeIntelligenceBean bean) {

    }

    @Override
    public void onGetIntelligenceListFail(String e) {

    }

    @Override
    public void onGetMvSublistBeanSuccess(MvSublistBean bean) {

    }

    @Override
    public void onGetMvSublistBeanFail(String e) {

    }

    @Override
    public void onGetArtistSublistBeanSuccess(ArtistSublistBean bean) {

    }

    @Override
    public void onGetArtistSublistBeanFail(String e) {

    }

    @Override
    public void onGetAlbumSublistBeanSuccess(AlbumSublistBean bean) {

    }

    @Override
    public void onGetAlbumSublistBeanFail(String e) {

    }

    @Override
    public void onGetMyFMSuccess(MyFmBean bean) {

    }

    @Override
    public void onGetMyFMFail(String e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}