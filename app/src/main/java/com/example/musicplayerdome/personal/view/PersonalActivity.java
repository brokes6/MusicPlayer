package com.example.musicplayerdome.personal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.AcitvityPosBinding;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.main.other.MinePresenter;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.personal.fragment.PersonalDynamicFragment;
import com.example.musicplayerdome.personal.fragment.PersonalSheetFragment;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.AppBarStateChangeListener;
import com.example.musicplayerdome.util.DensityUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends BaseActivity<MinePresenter> implements MineContract.View {
    private static final String TAG = "PersonalActivity";
    AcitvityPosBinding binding;
    public static final String USER_ID = "user_id";
    private long userid;
    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;
    private UserDetailBean beans;
    int minDistance;
    int deltaDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.acitvity_pos);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();

        goDialog();
        userid = getIntent().getLongExtra(USER_ID,0);
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
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleText(R.string.personal);
        setLeftTitleTextColorWhite();

        showDialog();
        if (userid!=0){
            showDialog();
            mPresenter.getUserDetail(userid);
        }
        setMargins(binding.toolbar,0,getStatusBarHeight(this),0,0);
        minDistance = DensityUtil.dp2px(this, 85);
        deltaDistance = DensityUtil.dp2px(this, 300) - minDistance;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SongPlayManager.getInstance().isDisplay()) {
            binding.bottomController.setVisibility(View.VISIBLE);
        } else {
            binding.bottomController.setVisibility(View.GONE);
        }
        binding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {

            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (float) (binding.rlUserDetailBottom.getTop() - minDistance) / (float) deltaDistance;
                binding.ivUserDetailBackground.setAlpha(alphaPercent);
                binding.ivUserDetailBackgroundCover.setAlpha(alphaPercent);
                binding.ivUserDetailAvatar.setAlpha(alphaPercent);
                binding.tvUserDetailName.setAlpha(alphaPercent);
            }
        });
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
        beans = bean;
        //只能在这里来进行初始化，因为要等到获取UserDetailBean
        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new PersonalSheetFragment(beans));
        fragments.add(new PersonalDynamicFragment());
        pagerAdapter.init(fragments);
        binding.PContainer.setAdapter(pagerAdapter);
        binding.PContainer.setOffscreenPageLimit(2);
        binding.PContainer.setCurrentItem(0);
        binding.PTab.setViewPager(binding.PContainer);

        hideDialog();
        binding.tvUserDetailName.setText(bean.getProfile().getNickname());
        Glide.with(this).load(bean.getProfile().getBackgroundUrl()).into(binding.ivUserDetailBackgroundCover);
        Glide.with(this).load(bean.getProfile().getBackgroundUrl()).into(binding.ivUserDetailBackground);
        Glide.with(this).load(bean.getProfile().getAvatarUrl()).into(binding.ivUserDetailAvatar);
        int followed = bean.getProfile().getFolloweds();
        int follower = bean.getProfile().getFollows();
        binding.tvUserDetailSub.setText("关注 " + follower + "  粉丝 " + followed);
        //显示关注或者已关注
        boolean isFollowed = bean.getProfile().isFollowed();
        if(isFollowed){
            binding.flUserDetailFollowed.setVisibility(View.VISIBLE);
        }else{
            binding.llUserDetailFollow.setVisibility(View.VISIBLE);
        }
        if (bean.getProfile().getVipType()==0){
            binding.ivUserDetailVip.setText("普通用户");
        }else{
            binding.ivUserDetailVip.setText("黑胶CVIP");
        }
        binding.tvUserLevel.setText("Lv." + bean.getLevel());
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