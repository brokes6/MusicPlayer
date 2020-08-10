package com.example.musicplayerdome.song.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.databinding.ActivityCommentBinding;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.song.adapter.CommentAdapter;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.other.SongPresenter;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌曲、歌单、专辑等的评论界面
 */
public class CommentActivity extends BaseActivity<SongPresenter> implements SongContract.View {
    ActivityCommentBinding binding;
    private static final String TAG = "CommentActivity";
    public static final String COVER = "cover";
    public static final String NAME = "name";
    public static final String ARTIST = "artist";
    public static final String ID = "id";
    public static final String FROM = "from";
    public static final int SONG_COMMENT = 0x001;
    public static final int PLAYLIST_COMMENT = 0x002;
    public static final int ALBUM_COMMENT = 0x003;
    private int from;
    private CommentAdapter hotAdapter, newAdapter;
    private long id;
    private List<MusicCommentBean.CommentsBean> hotCommentList = new ArrayList<>();
    private List<MusicCommentBean.CommentsBean> newCommentList = new ArrayList<>();

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);

        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.red)
                .init();
    }

    @Override
    protected SongPresenter onCreatePresenter() {
        return new SongPresenter(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));

        Intent intent = getIntent();
        id = intent.getLongExtra(ID, 0);
        Log.e(TAG, "initData: 当前id为"+id );
        Glide.with(this).load(intent.getStringExtra(COVER)).into(binding.ivCover);
        binding.tvMusicName.setText(intent.getStringExtra(NAME));
        binding.tvArtist.setText(intent.getStringExtra(ARTIST));
        from = intent.getIntExtra(FROM, 0x001);

        binding.rvHotComment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNewComment.setLayoutManager(new LinearLayoutManager(this));
        hotAdapter = new CommentAdapter(this);
        newAdapter = new CommentAdapter(this);
        binding.rvHotComment.setAdapter(hotAdapter);
        binding.rvNewComment.setAdapter(newAdapter);

        showDialog();
        switch (from) {
            case SONG_COMMENT:
                mPresenter.getMusicComment(id);
                break;
            case PLAYLIST_COMMENT:
                mPresenter.getPlaylistComment(id);
                break;
        }
    }

    @Override
    protected void initView() {

    }


    CommentAdapter.OnLikeCommentListener hotListener = position -> { };

    CommentAdapter.OnLikeCommentListener newListener = position -> { };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        hideDialog();
        setLeftTitleText(getString(R.string.comment) + "(" + bean.getTotal() + ")", getString(R.string.colorWhite));
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
    }

    @Override
    public void onGetMusicCommentFail(String e) {
        hideDialog();
        Log.d(TAG, "onGetMusicCommentFail : " + e);
        XToastUtils.warning(e);
    }

    @Override
    public void onLikeCommentSuccess(CommentLikeBean bean) {
        Log.d(TAG, "onLikeCommentSuccess :" + bean);
        if (bean.getCode() == 200) {
            mPresenter.getMusicComment(id);
        } else {
            XToastUtils.info(bean.getCode());
        }
    }

    @Override
    public void onLikeCommentFail(String e) {
        Log.d(TAG, "onLikeCommentFail :" + e);
        XToastUtils.info(e);
    }

    @Override
    public void onGetLyricSuccess(LyricBean bean) {

    }

    @Override
    public void onGetLyricFail(String e) {

    }

    @Override
    public void onGetPlaylistCommentSuccess(PlayListCommentBean bean) {
        hideDialog();
        Log.d(TAG, "onGetPlaylistCommentSuccess : " + bean);
        setLeftTitleText(getString(R.string.comment) + "(" + bean.getTotal() + ")", getString(R.string.colorWhite));
        notifyList(bean.getHotComments(), bean.getComments());
    }

    @Override
    public void onGetPlaylistCommentFail(String e) {
        hideDialog();
        Log.d(TAG, "onGetPlaylistCommentFail : " + e);
        XToastUtils.warning(e);
    }
}
