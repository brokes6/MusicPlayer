package com.example.musicplayerdome.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivitySheetCommentBinding;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.song.adapter.CommentAdapter;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.other.SongPresenter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * SongSheetActivityMusic.SongSheetComment 歌单评论
 * 展示歌单的评论
 */
public class SongSheetComment extends BaseActivity<SongPresenter> implements SongContract.View{
    ActivitySheetCommentBinding binding;
    private static final String TAG = "SongSheetComment";
    public static final String COVER = "cover";
    public static final String NAME = "name";
    public static final String ARTIST = "artist";
    public static final String ID = "id";
    private CommentAdapter hotAdapter, newAdapter;
    private long id;
    private List<MusicCommentBean.CommentsBean> hotCommentList = new ArrayList<>();
    private List<MusicCommentBean.CommentsBean> newCommentList = new ArrayList<>();


    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sheet_comment);

        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .init();
    }

    @Override
    protected SongPresenter onCreatePresenter() {
        return new SongPresenter(this);
    }

    @Override
    protected void initData() {
        binding.rvHotComment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNewComment.setLayoutManager(new LinearLayoutManager(this));
        hotAdapter = new CommentAdapter(this);
        newAdapter = new CommentAdapter(this);
        binding.rvHotComment.setAdapter(hotAdapter);
        binding.rvNewComment.setAdapter(newAdapter);

        showDialog();
        mPresenter.getPlaylistComment(id);
    }

    @Override
    protected void initView() {
        setBackBtn(getString(R.string.colorBlack));
        setTitleBG(getString(R.string.colorWhite));

        Intent intent = getIntent();
        id = intent.getLongExtra(ID, 0);
        Glide.with(this).load(intent.getStringExtra(COVER)).into(binding.ivCover);
        binding.tvMusicName.setText(intent.getStringExtra(NAME));
        binding.tvArtist.setText("by."+intent.getStringExtra(ARTIST));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetSongDetailSuccess(SongDetailBean bean) {

    }

    @Override
    public void onGetSongDetailFail(String e) {

    }

    @Override
    public void onLikeMusicSuccess(LikeMusicBean bean) {

    }

    @Override
    public void onNoLikeMusicSuccess(LikeMusicBean bean) {

    }

    @Override
    public void onLikeMusicFail(String e) {

    }

    @Override
    public void onGetLikeListSuccess(LikeListBean bean) {

    }

    @Override
    public void onGetLikeListFail(String e) {

    }

    @Override
    public void onGetMusicCommentSuccess(MusicCommentBean bean) {

    }

    @Override
    public void onGetMusicCommentFail(String e) {

    }

    @Override
    public void onLikeCommentSuccess(CommentLikeBean bean) {

    }

    @Override
    public void onLikeCommentFail(String e) {

    }

    @Override
    public void onGetLyricSuccess(LyricBean bean) {

    }

    @Override
    public void onGetLyricFail(String e) {

    }

    @Override
    public void onGetPlaylistCommentSuccess(PlayListCommentBean bean) {
        setLeftTitleText(getString(R.string.comment) + "(" + bean.getTotal() + ")", getString(R.string.colorBlack));
        notifyList(bean.getHotComments(), bean.getComments());
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
        hideDialog();
    }

    @Override
    public void onGetPlaylistCommentFail(String e) {

    }
}
