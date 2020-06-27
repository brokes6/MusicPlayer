package com.example.musicplayerdome.main.view;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.WowContract;
import com.example.musicplayerdome.databinding.ActivityPlayListBinding;
import com.example.musicplayerdome.databinding.SongPlayListBinding;
import com.example.musicplayerdome.main.bean.CollectionListBean;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.main.dialog.SongSheetDialog;
import com.example.musicplayerdome.personal.view.PersonalActivity;
import com.example.musicplayerdome.song.adapter.MySongListAdapter;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.bean.BannerBean;
import com.example.musicplayerdome.bean.MusicCanPlayBean;
import com.example.musicplayerdome.databinding.SongSheetBinding;
import com.example.musicplayerdome.main.bean.DailyRecommendBean;
import com.example.musicplayerdome.main.bean.HighQualityPlayListBean;
import com.example.musicplayerdome.main.bean.MainRecommendPlayListBean;
import com.example.musicplayerdome.main.bean.PlaylistDetailBean;
import com.example.musicplayerdome.main.bean.RecommendPlayListBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.example.musicplayerdome.main.other.WowPresenter;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.view.CommentActivity;
import com.example.musicplayerdome.song.view.SongActivity;
import com.example.musicplayerdome.util.AppBarStateChangeListener;
import com.example.musicplayerdome.util.DensityUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.lzx.starrysky.model.SongInfo;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_DESCRIPTION;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;
import static com.example.musicplayerdome.personal.view.PersonalActivity.USER_ID;

/**
 * SongSheetActivityMusic 歌单详情页面
 * 展示歌单里的歌曲
 */
public class SongSheetActivityMusic extends BaseActivity<WowPresenter> implements View.OnClickListener, WowContract.View{
//    SongSheetBinding binding;
    ActivityPlayListBinding binding;
//    SongPlayListBinding binding;
    private static final String TAG = "SongSheetActivity";
    private MySongListAdapter adapter;
    private List<PlaylistDetailBean.PlaylistBean.TracksBean> beanList = new ArrayList<>();
    private List<SongInfo> songInfos = new ArrayList<>();
    private long playlistId;
    private boolean go = false;
    private String creatorUrl;
    private String playlistName;
    private String playlistPicUrl;
    private String creatorName;
    int minDistance;
    int deltaDistance;
    int isCollection = 1;
    private ObjectAnimator alphaAnimator;
    private ObjectAnimator coverAlphaAnimator;
    //计算完成后发送的Handler msg
    public static final int COMPLETED = 0;
    LinearLayout SHComment,share;
    TextView songComment;
    private Drawable Sbackg;
    private String description;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                Sbackg = (Drawable) msg.obj;
                binding.background.setBackground((Drawable) msg.obj);
                getAlphaAnimatorBg().start();
                getAlphaAnimatorCover().start();
            }
        }
    };

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_play_list);
        //之前的老布局
//        binding = DataBindingUtil.setContentView(this,R.layout.song_play_list);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        goDialog();
    }

    @Override
    protected void initData() {
        initView();
        setBackBtn(getString(R.string.colorWhite));
        setLeftTitleTextColorWhite();
        setLeftTitleText(R.string.playlist);

        LinearLayoutManager lm = new LinearLayoutManager(SongSheetActivityMusic.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new MySongListAdapter(this);
        adapter.setType(2);
        binding.recyclerView.setLayoutManager(lm);
        binding.recyclerView.setAdapter(adapter);

        showDialog();
        if (getIntent() != null) {
            playlistPicUrl = getIntent().getStringExtra(PLAYLIST_PICURL);
            Glide.with(this).load(playlistPicUrl).into(binding.XLogin);
            playlistName = getIntent().getStringExtra(PLAYLIST_NAME);
            description = getIntent().getStringExtra(PLAYLIST_DESCRIPTION);
            binding.XTitle.setText(playlistName);
            creatorName = getIntent().getStringExtra(PLAYLIST_CREATOR_NICKNAME);
            binding.tvPlaylistName.setText(creatorName);
            creatorUrl = getIntent().getStringExtra(PLAYLIST_CREATOR_AVATARURL);
            Glide.with(this).load(creatorUrl).into(binding.userImg);
            playlistId = getIntent().getLongExtra(PLAYLIST_ID, 0);
            calculateColors(playlistPicUrl);

            mPresenter.getPlaylistDetail(playlistId);
        }
        minDistance = DensityUtil.dp2px(SongSheetActivityMusic.this, 85);
        deltaDistance = DensityUtil.dp2px(this, 300) - minDistance;
    }

    private void initView(){
        SHComment = findViewById(R.id.SH_comment);
        songComment = findViewById(R.id.song_comment);
        share = findViewById(R.id.share);

        binding.rlPlayall.setOnClickListener(this);
        binding.userImg.setOnClickListener(this);
        binding.XLogin.setOnClickListener(this);
        binding.buttonPersonal.setOnClickListener(this);
        share.setOnClickListener(this);
        SHComment.setOnClickListener(this);
        //设置 Header式
        binding.refreshLayout.setRefreshHeader(new MaterialHeader(this));
        //取消Footer
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setDisableContentWhenRefresh(true);

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: 歌单开始刷新");
                mPresenter.getPlaylistDetailAgain(playlistId);
            }
        });
        setMargins(binding.rlTitle,0,getStatusBarHeight(this),0,0);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.SH_comment:
                intent.setClass(this, SongSheetComment.class);
                intent.putExtra(SongSheetComment.ID, playlistId);
                intent.putExtra(SongSheetComment.NAME, playlistName);
                intent.putExtra(SongSheetComment.ARTIST, creatorName);
                intent.putExtra(SongSheetComment.COVER, playlistPicUrl);
                startActivity(intent);
                break;
            case R.id.share:
                showSimpleBottomSheetGrid();
                break;
            case R.id.button_personal:
                mPresenter.CollectionList(isCollection,playlistId);
                break;
            case R.id.XLogin:
                SongSheetDialog songSheetDialog = new SongSheetDialog(this,playlistName,description,playlistPicUrl,Sbackg);
                songSheetDialog.setCanceledOnTouchOutside(true);
                songSheetDialog.show();
                break;
            case R.id.user_img:
                intent.setClass(this, PersonalActivity.class);
                intent.putExtra(USER_ID,Sbean.getPlaylist().getUserId());
                startActivity(intent);
                break;
            case R.id.rl_playall:
                SharedPreferencesUtil.putData("Ykey",2);
                adapter.PlayAll();
                binding.bottomController.setVisibility(View.VISIBLE);
                break;
        }
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
                float alphaPercent = (float) (binding.llPlay.getTop() - minDistance) / (float) deltaDistance;
                binding.ivCoverBg.setAlpha(alphaPercent);
                binding.XLogin.setAlpha(alphaPercent);
                binding.userImg.setAlpha(alphaPercent);
                binding.XTitle.setAlpha(alphaPercent);
                binding.tvPlaylistName.setAlpha(alphaPercent);
            }
        });
    }
    //底部弹出选择列表
    private void showSimpleBottomSheetGrid() {
        BottomSheet.BottomGridSheetBuilder builder = new BottomSheet.BottomGridSheetBuilder(this);
        builder.addItem(R.drawable.icon_more_operation_share_friend, "分享到微信", 0, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_moment, "分享到朋友圈", 1, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_weibo, "分享到微博", 2, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_chat, "分享到私信", 3, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new BottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, BottomSheetItemView itemView) {
                        dialog.dismiss();
                        XToastUtils.toast(itemView.toString());
                    }
                }).build().show();
    }

    private ObjectAnimator getAlphaAnimatorCover() {
        if (coverAlphaAnimator == null) {
            coverAlphaAnimator = ObjectAnimator.ofFloat(binding.ivCoverBg, "alpha", 0f, 0.5f);
            coverAlphaAnimator.setDuration(1500);
            coverAlphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return coverAlphaAnimator;
    }

    private ObjectAnimator getAlphaAnimatorBg() {
        if (alphaAnimator == null) {
            alphaAnimator = ObjectAnimator.ofFloat(binding.background, "alpha", 0f, 0.5f);
            alphaAnimator.setDuration(1500);
            alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return alphaAnimator;
    }

    /**
     * 该方法用url申请一个图片bitmap，并将其压缩成原图1/300，计算上半部分和下半部分颜色RGB平均值
     * 两个RGB去作为渐变色的两个点
     * 还要开子线程去计算...
     */
    public void calculateColors(String url) {
        new Thread(() -> {
            try {
                //渐变色的两个颜色
                URL fileUrl;
                Bitmap bitmap;
                fileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 270;
                bitmap = BitmapFactory.decodeStream(is, new Rect(), opt);

                Message msg = Message.obtain();
                msg.what = COMPLETED;
                msg.obj = new BitmapDrawable(bitmap);
                handler.sendMessage(msg);

                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onGetBannerSuccess(BannerBean bean) {

    }

    @Override
    public void onGetBannerFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListFail(String e) {

    }

    @Override
    public void onGetRecommendPlayListAgainSuccess(MainRecommendPlayListBean bean) {

    }

    @Override
    public void onGetRecommendPlayListAgainFail(String e) {

    }

    @Override
    public void onGetDailyRecommendSuccess(DailyRecommendBean bean) {

    }

    @Override
    public void onGetDailyRecommendFail(String e) {

    }

    @Override
    public void onGetTopListSuccess(TopListBean bean) {

    }

    @Override
    public void onGetTopListFail(String e) {

    }

    @Override
    public void onGetPlayListSuccess(RecommendPlayListBean bean) {

    }

    @Override
    public void onGetPlayListFail(String e) {

    }

    private PlaylistDetailBean Sbean;
    @SuppressLint("SetTextI18n")
    @Override
    public void onGetPlaylistDetailSuccess(PlaylistDetailBean bean) {
        Log.d(TAG, "获取歌单成功 : " + bean);
        Sbean = bean;
        if (bean.getPlaylist().isSubscribed()==false) {
            isCollection = 1;
            binding.buttonPersonal.setText("+收藏");
        }else{
            isCollection = 2;
            binding.buttonPersonal.setText("-取消收藏");
        }
        Glide.with(this).load(bean.getPlaylist().getCreator().getAvatarUrl()).into(binding.userImg);
        songInfos.clear();
        beanList.addAll(bean.getPlaylist().getTracks());
        for (int i = 0; i < beanList.size(); i++) {
            SongInfo beanInfo = new SongInfo();
            beanInfo.setArtist(beanList.get(i).getAr().get(0).getName());
            beanInfo.setSongName(beanList.get(i).getName());
            beanInfo.setSongCover(beanList.get(i).getAl().getPicUrl());
            beanInfo.setSongId(String.valueOf(beanList.get(i).getId()));
            beanInfo.setDuration(beanList.get(i).getDt());
            beanInfo.setSongUrl(SONG_URL + beanList.get(i).getId() + ".mp3");
            beanInfo.setArtistId(String.valueOf(beanList.get(i).getAr().get(0).getId()));
            beanInfo.setArtistKey(beanList.get(i).getAl().getPicUrl());
            songInfos.add(beanInfo);
        }
        Log.e(TAG, "当前歌曲id为"+ bean.getPlaylist().getId());
        songComment.setText(""+bean.getPlaylist().getCommentCount());
        adapter.setList(songInfos);
        adapter.loadMore(songInfos);
        hideDialog();
    }

    @Override
    public void onGetPlaylistDetailFail(String e) {
        hideDialog();
        XToastUtils.error("网络请求失败，请检查网络再试");
    }

    @Override
    public void onGetPlaylistDetailAgainSuccess(PlaylistDetailBean bean) {
        List<PlaylistDetailBean.PlaylistBean.TracksBean> beanList = new ArrayList<>();
        List<SongInfo> songInfos = new ArrayList<>();

        beanList.addAll(bean.getPlaylist().getTracks());
        songInfos.clear();
        for (int i = 0; i < beanList.size(); i++) {
            SongInfo beanInfo = new SongInfo();
            beanInfo.setArtist(beanList.get(i).getAr().get(0).getName());
            beanInfo.setSongName(beanList.get(i).getName());
            beanInfo.setSongCover(beanList.get(i).getAl().getPicUrl());
            beanInfo.setSongId(String.valueOf(beanList.get(i).getId()));
            beanInfo.setDuration(beanList.get(i).getDt());
            beanInfo.setSongUrl(SONG_URL + beanList.get(i).getId() + ".mp3");
            beanInfo.setArtistId(String.valueOf(beanList.get(i).getAr().get(0).getId()));
            beanInfo.setArtistKey(beanList.get(i).getAl().getPicUrl());
            songInfos.add(beanInfo);
        }
        Log.e(TAG, "歌单刷新成功");
        adapter.refresh(songInfos);
        binding.refreshLayout.finishRefresh(true);
    }

    @Override
    public void onGetPlaylistDetailAgainFail(String e) {
        XToastUtils.error("刷新失败，请检查网络再试");
        binding.refreshLayout.finishRefresh(true);
    }

    @Override
    public void onGetMusicCanPlaySuccess(MusicCanPlayBean bean) {

    }

    @Override
    public void onGetMusicCanPlayFail(String e) {

    }

    @Override
    public void onGetHighQualitySuccess(HighQualityPlayListBean bean) {

    }

    @Override
    public void onGetHighQualityFail(String e) {

    }

    @Override
    public void onGetRecommendsongSuccess(RecommendsongBean bean) {

    }

    @Override
    public void onGetRecommendsongFail(String e) {

    }

    @Override
    public void onGetCollectionListSuccess(CollectionListBean bean) {
        if (isCollection==1){
            isCollection = 2;
            binding.buttonPersonal.setText("已收藏");
            XToastUtils.success("收藏成功");
        }else{
            isCollection = 1;
            binding.buttonPersonal.setText("收藏");
            XToastUtils.success("取消收藏成功");
        }
    }

    @Override
    public void onGetCollectionListFail(String e) {
        XToastUtils.error(e);
    }

    @Override
    protected WowPresenter onCreatePresenter() {
        return new WowPresenter(this);
    }

    @Override
    protected void initModule() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
