package com.example.musicplayerdome.song.view;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongMvContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivitySongMvBinding;
import com.example.musicplayerdome.song.adapter.CommentAdapter;
import com.example.musicplayerdome.song.bean.CollectionMVBean;
import com.example.musicplayerdome.song.bean.MVDetailBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.SongMvDataBean;
import com.example.musicplayerdome.song.other.MvPersenter;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SongMvActivity extends BaseActivity<MvPersenter> implements SongMvContract.View{
    private static final String TAG = "SongMvActivity";
    ActivitySongMvBinding binding;
    public static final String MVSONG_INFO = "mvsongInfo";
    private MVDetailBean.MVData mvDetailBean;
    private long sid;
    private boolean open = false;
    private String MVurl;
    private CommentAdapter hotAdapter, newAdapter;
    private List<MusicCommentBean.CommentsBean> hotCommentList = new ArrayList<>();
    private List<MusicCommentBean.CommentsBean> newCommentList = new ArrayList<>();

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_song_mv);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .init();
    }

    @Override
    protected MvPersenter onCreatePresenter() {
        return new MvPersenter(this);
    }

    @Override
    protected void initData() {
        showDialog();
        getMvIntent();
        if (SongPlayManager.getInstance().isDisplay()){
            SongPlayManager.getInstance().pauseMusic();
        }
    }

    //初始化视频播放器
    @Override
    protected void initView(){
        //不保存播放进度
        Jzvd.SAVE_PROGRESS = false;
        //自动播放
//        binding.jzVideo.startVideo();
        //重力感应
        Jzvd.FULLSCREEN_ORIENTATION=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        binding.rvHotComment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNewComment.setLayoutManager(new LinearLayoutManager(this));
        hotAdapter = new CommentAdapter(this);
        newAdapter = new CommentAdapter(this);
        binding.rvHotComment.setAdapter(hotAdapter);
        binding.rvNewComment.setAdapter(newAdapter);
        binding.SMOpen.setOnClickListener(this);
        binding.MVCollection.setOnClickListener(this);
        binding.MVShare.setOnClickListener(this);
        binding.userImg.setOnClickListener(this);
    }

    private void getMvIntent(){
        Intent intent = getIntent();
        if (intent!=null){
            sid = intent.getLongExtra(MVSONG_INFO,-1);
            Log.e(TAG, "getMvIntent: 视频id为"+sid );
            mPresenter.getSongMv(sid);
            mPresenter.getSongMvComment(sid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SM_open:
                if (open==false){
                    open=true;
                    OpenMore(open);
                }else{
                    open=false;
                    OpenMore(open);
                }
                break;
            case R.id.MV_collection:
                mPresenter.CollectionMV(sid,1);
                break;
            case R.id.MV_share:
                showSimpleBottomSheetGrid();
                break;
            case R.id.user_img:
                Intent intent = new Intent();
                intent.setClass(mContext, SingerActivity.class);
                intent.putExtra(SingerActivity.SINGER_NAME, mvDetailBean.getArtistName());
                intent.putExtra(SingerActivity.SINGER_ID, mvDetailBean.getArtistId());
                mContext.startActivity(intent);
                break;
        }
    }
    private void OpenMore(boolean y){
        if (y==false){
            binding.SMDetails.setVisibility(View.GONE);
        }else{
            binding.SMDetails.setVisibility(View.VISIBLE);
        }
    }

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
    public void onGetgetSongMvSuccess(SongMvDataBean bean) {
        MVurl = bean.getData().getUrl();
        mPresenter.getMVDetail(sid);
    }

    @Override
    public void onGetgetSongMvFail(String e) {
        hideDialog();
        XToastUtils.error("网络异常");
        Log.e(TAG, "获取MV播放地址错误"+e);
    }

    @Override
    public void onGetSongMvCommentSuccess(MusicCommentBean bean) {
        notifyList(bean.getHotComments(), bean.getComments());
        binding.SMComment.setText(""+bean.getTotal());
    }

    private void notifyList(List<MusicCommentBean.CommentsBean> hotComments, List<MusicCommentBean.CommentsBean> comments) {
        hotCommentList.clear();
        newCommentList.clear();
        if (hotComments != null) {
            hotCommentList = hotComments;
        }
        if (comments != null) {
            newCommentList = comments;
        }
        hotAdapter.loadMore(hotCommentList);
        newAdapter.loadMore(newCommentList);
    }

    @Override
    public void onGetSongMvCommentFail(String e) {
        Log.d(TAG, "评论错误 : " + e);
    }

    @Override
    public void onGetMVDetailSuccess(MVDetailBean bean) {
        binding.jzVideo.setUp(MVurl,bean.getData().getName());
        Glide.with(this).load(bean.getData().getCover()).into(binding.jzVideo.posterImageView);
        hideDialog();
        binding.userName.setText(bean.getData().getArtistName());
        binding.SMTitle.setText(bean.getData().getName());
        binding.SMDetails.setText(bean.getData().getDesc());
        binding.SMShare.setText(""+bean.getData().getShareCount());
        binding.SMCollect.setText(""+bean.getData().getSubCount());
        binding.SMNumber.setText(bean.getData().getPlayCount()+"次观看");
        Glide.with(this).load(bean.getData().getArtists().get(0).getImg1v1Url()).into(binding.userImg);

        mvDetailBean = bean.getData();
    }

    @Override
    public void onGetMVDetailFail(String e) {
        hideDialog();
        Log.e(TAG, "获取MV详情错误"+e);
    }

    @Override
    public void onCollectionMvSuccess(CollectionMVBean bean) {
        XToastUtils.success(bean.getMessage());
    }

    @Override
    public void onCollectionMvFail(String e) {
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
        //     Jzvd.clearSavedProgress(this, null);
        //home back
        JzvdStd.goOnPlayOnPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
