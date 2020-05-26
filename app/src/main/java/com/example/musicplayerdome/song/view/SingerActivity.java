package com.example.musicplayerdome.song.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SingerContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.ActivitySingerBinding;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.example.musicplayerdome.search.bean.SingerAblumSearchBean;
import com.example.musicplayerdome.search.bean.SingerDescriptionBean;
import com.example.musicplayerdome.search.bean.SingerSongSearchBean;
import com.example.musicplayerdome.song.fragment.SingerAlbumSearchFragment;
import com.example.musicplayerdome.song.fragment.SingerFeedSearchFragment;
import com.example.musicplayerdome.song.fragment.SingerInfoSearchFragment;
import com.example.musicplayerdome.song.fragment.SingerSongSearchFragment;
import com.example.musicplayerdome.song.other.SingIdEvent;
import com.example.musicplayerdome.song.other.SingerPresenter;
import com.example.musicplayerdome.util.AppBarStateChangeListener;
import com.example.musicplayerdome.util.DensityUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 歌手界面，下面有4个Fragment，分别是单曲、专辑、视频和个人信息
 */
public class SingerActivity extends BaseActivity<SingerPresenter> implements SingerContract.View {
    ActivitySingerBinding binding;
    private static final String TAG = "SingerActivity";
    public static final String SINGER_ID = "singerId";
    public static final String SINGER_PICURL = "singerPicUrl";
    public static final String SINGER_NAME = "singerName";
    private List<BaseFragment> fragments = new ArrayList<>();
    private long singId;
    private float minDistance, deltaDistance;
    private MultiFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_singer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        goDialog();

        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new SingerSongSearchFragment());
        fragments.add(new SingerAlbumSearchFragment());
        fragments.add(new SingerFeedSearchFragment());
        fragments.add(new SingerInfoSearchFragment());
        pagerAdapter.init(fragments);
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
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    protected SingerPresenter onCreatePresenter() {
        return new SingerPresenter(this);
    }

    @Override
    protected void initModule() {
//        setMargins(binding.Top,0,getStatusBarHeight(this),0,0);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));

        if (getIntent() != null) {
            Glide.with(this).load(getIntent().getStringExtra(SINGER_PICURL)).transition(new DrawableTransitionOptions().crossFade()).into(binding.ivSingerCover);
            Glide.with(this)
                    .load(getIntent().getStringExtra(SINGER_PICURL))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                    .into(binding.ivSinger);
            binding.tvName.setText(getIntent().getStringExtra(SINGER_NAME));
            singId = getIntent().getLongExtra(SINGER_ID, -1);
            setLeftTitleText(getIntent().getStringExtra(SINGER_NAME), getString(R.string.colorWhite));
            setLeftTitleTextColorWhite();
            if (singId != -1) {
                EventBus.getDefault().postSticky(new SingIdEvent(singId, binding.tvName.getText().toString().trim()));
            }
            binding.vpContainer.setAdapter(pagerAdapter);
            binding.vpContainer.setOffscreenPageLimit(3);
            binding.vpContainer.setCurrentItem(0);
            pagerAdapter.getItem(0).setUserVisibleHint(true);
            binding.tabTitle.setViewPager(binding.vpContainer);

            minDistance = DensityUtil.dp2px(SingerActivity.this, 85);
            deltaDistance = DensityUtil.dp2px(SingerActivity.this, 250) - minDistance;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if (state == State.COLLAPSED) {
                    setLeftTitleAlpha(255f);
                } else if (state == State.EXPANDED) {
                    binding.tvName.setAlpha(1f);
                }
            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout) {
                float alphaPercent = (binding.rlInfo.getTop() - minDistance) / deltaDistance;
                binding.tvName.setAlpha(alphaPercent);
                binding.ivSingerCover.setImageAlpha((int) (alphaPercent * 255));
                if (alphaPercent < 0.2f) {
                    float leftTitleAlpha = (1.0f - alphaPercent / 0.2f);
                    setLeftTitleAlpha(leftTitleAlpha);
                } else {
                    setLeftTitleAlpha(0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetSingerHotSongSuccess(SingerSongSearchBean bean) {

    }

    @Override
    public void onGetSingerHotSongFail(String e) {

    }

    @Override
    public void onGetSingerAlbumSuccess(SingerAblumSearchBean bean) {

    }

    @Override
    public void onGetSingerAlbumFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerDescSuccess(SingerDescriptionBean bean) {

    }

    @Override
    public void onGetSingerDescFail(String e) {

    }

    @Override
    public void onGetSimiSingerSuccess(SimiSingerBean bean) {

    }

    @Override
    public void onGetSimiSingerFail(String e) {

    }
}
