package com.example.musicplayerdome.personal.view;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.api.ApiService;
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
import com.example.musicplayerdome.personal.bean.FollowUserBean;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.personal.fragment.PersonalDynamicFragment;
import com.example.musicplayerdome.personal.fragment.PersonalSheetFragment;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.AppBarStateChangeListener;
import com.example.musicplayerdome.util.DensityUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户的个人界面
 * 分为主页和动态
 */
public class PersonalActivity extends BaseActivity<MinePresenter> implements MineContract.View {
    private static final String TAG = "PersonalActivity";
    private AcitvityPosBinding binding;
    public static final String USER_ID = "user_id";
    private long userid;
    private List<BaseFragment> fragments = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;
    private UserDetailBean beans;
    private int minDistance,deltaDistance;
    private boolean isFollowed;

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

        userid = getIntent().getLongExtra(USER_ID,0);
    }

    @Override
    protected MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initData() {
        if (userid!=0){
            showDialog();
            mPresenter.getUserDetail(userid);
        }
    }

    @Override
    protected void initView() {
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleText(R.string.personal);
        setLeftTitleTextColorWhite();

        binding.llUserDetailFollow.setOnClickListener(this);
        binding.flUserDetailFollowed.setOnClickListener(this);

        setMargins(binding.toolbar,0,getStatusBarHeight(this),0,0);
        minDistance = DensityUtil.dp2px(this, 85);
        deltaDistance = DensityUtil.dp2px(this, 300) - minDistance;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_user_detail_follow:
                followUser(userid,1);
                break;
            case R.id.fl_user_detail_followed:
                XToastUtils.info("暂时不可取消关注");
                break;
        }
    }

    private void followUser(long id,int t){
        ApiService service = ApiEngine.getInstance().getApiService();
        service.followUser(id,t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FollowUserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FollowUserBean followUserBean) {
                        XToastUtils.success(followUserBean.getFollowContent());
                        isFollowed = followUserBean.getUser().getFollowed();
                        if(isFollowed){
                            binding.flUserDetailFollowed.setVisibility(View.VISIBLE);
                            binding.llUserDetailFollow.setVisibility(View.GONE);
                        }else{
                            binding.flUserDetailFollowed.setVisibility(View.GONE);
                            binding.llUserDetailFollow.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        XToastUtils.error(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        isFollowed = bean.getProfile().isFollowed();
        Log.e(TAG, "onGetUserDetailSuccess: 当前用户是否关注"+ isFollowed);
        if(isFollowed){
            binding.flUserDetailFollowed.setVisibility(View.VISIBLE);
            binding.llUserDetailFollow.setVisibility(View.GONE);
        }else{
            binding.flUserDetailFollowed.setVisibility(View.GONE);
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