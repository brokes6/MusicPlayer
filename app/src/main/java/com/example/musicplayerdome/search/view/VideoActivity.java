package com.example.musicplayerdome.search.view;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivityVideoBinding;
import com.example.musicplayerdome.search.dialog.VideoReviewDialog;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.PlayListSearchBean;
import com.example.musicplayerdome.search.bean.RadioSearchBean;
import com.example.musicplayerdome.search.bean.SingerSearchBean;
import com.example.musicplayerdome.search.bean.SongSearchBean;
import com.example.musicplayerdome.search.bean.SynthesisSearchBean;
import com.example.musicplayerdome.search.bean.UserSearchBean;
import com.example.musicplayerdome.search.bean.VideoDataBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.search.other.SearchPresenter;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * VideoActivity 视频详情页面
 * 播放视频（后期预计可以想抖音一样上滑切换）
 */
public class VideoActivity extends BaseActivity<SearchPresenter> implements SearchContract.View,View.OnClickListener {
    private static final String TAG = "VideoActivity";
    private ActivityVideoBinding binding;
    private String title,coverUrl,vid,userName;
    private List<MusicCommentBean> CommentList = new ArrayList<>();
    private boolean isLike = false;


    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .navigationBarDarkIcon(false)
                .navigationBarColor(R.color.black)
                .init();
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initView(){
        binding.VComment.setOnClickListener(this);
        binding.VShare.setOnClickListener(this);

        //不保存播放进度
        Jzvd.SAVE_PROGRESS = false;
        //重力感应
        Jzvd.FULLSCREEN_ORIENTATION= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setMargins(binding.VTop,0,getStatusBarHeight(this),0,0);
    }

    @Override
    protected void initData() {
        showDialog();
        setBackBtn(getString(R.string.colorWhite));
        getMvIntent();
        if (SongPlayManager.getInstance().isPlaying()){
            SongPlayManager.getInstance().pauseMusic();
        }
    }

    private void getMvIntent(){
        Intent intent = getIntent();
        if (intent!=null){
            title = intent.getStringExtra("Vtitle");
            coverUrl = intent.getStringExtra("VcoverUrl");
            vid = intent.getStringExtra("Vid");
            Log.e(TAG, "getMvIntent: 当前视频id为"+vid );
            userName = intent.getStringExtra("userName");
            setSongInfo(title,userName);
            Glide.with(this).load(coverUrl).into(binding.jzVideo.posterImageView);

            mPresenter.getVideoData(vid);
            mPresenter.getVideoDetails(vid);
            mPresenter.getVideoComment(vid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.V_comment:
                VideoReviewDialog reviewDialog = new VideoReviewDialog(mContext,CommentList);
                reviewDialog.setCanceledOnTouchOutside(true);
                reviewDialog.show();
                break;
            case R.id.V_share:
                showSimpleBottomSheetGrid();
                break;
        }
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

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {

    }

    @Override
    public void onGetHotSearchDetailFail(String e) {

    }

    @Override
    public void onGetSongSearchSuccess(SongSearchBean bean) {

    }

    @Override
    public void onGetSongSearchFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {

    }

    @Override
    public void onGetSingerSearchFail(String e) {

    }

    @Override
    public void onGetAlbumSearchSuccess(AlbumSearchBean bean) {

    }

    @Override
    public void onGetAlbumSearchFail(String e) {

    }

    @Override
    public void onGetPlayListSearchSuccess(PlayListSearchBean bean) {

    }

    @Override
    public void onGetPlayListSearchFail(String e) {

    }

    @Override
    public void onGetRadioSearchSuccess(RadioSearchBean bean) {

    }

    @Override
    public void onGetRadioSearchFail(String e) {

    }

    @Override
    public void onGetUserSearchSuccess(UserSearchBean bean) {

    }

    @Override
    public void onGetUserSearchFail(String e) {

    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }

    private List<VideoUrlBean.urlsData> videoList = new ArrayList<>();
    @Override
    public void onGetVideoDataSuccess(VideoUrlBean bean) {
        hideDialog();
        videoList.addAll(bean.getUrls());

        binding.jzVideo.setUp(videoList.get(0).getUrl(),title);
        binding.jzVideo.startVideo();
    }

    @Override
    public void onGetVideoDataFail(String e) {

    }

    @Override
    public void onGetVideoCommentSuccess(MusicCommentBean bean) {
        CommentList.add(bean);
    }

    @Override
    public void onGetVideoCommentFail(String e) {

    }

    @Override
    public void onGetVideoDetailsSuccess(VideoDataBean bean) {
        isLike = bean.isLiked();
        if (isLike) {
            binding.ivLike.setImageResource(R.drawable.shape_like_white);
        } else {
            binding.ivLike.setImageResource(R.drawable.shape_not_like);
        }
        binding.VThumbsUpNum.setText(""+bean.getLikedCount());
        binding.VCommentNum.setText(""+bean.getCommentCount());
        binding.VShareNum.setText(""+bean.getShareCount());
    }

    @Override
    public void onGetVideoDetailsFail(String e) {
        XToastUtils.error(e);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JzvdStd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JzvdStd.goOnPlayOnPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}