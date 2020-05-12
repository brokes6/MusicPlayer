package com.example.musicplayerdome.song.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.activity.MusicActivityMusic;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.rewrite.MusicDrawerItemView;
import com.example.musicplayerdome.song.SongPresenter;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.util.ClickUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 歌曲详情界面
 * Created By Rikka on 2019/7/24
 */
public class SongDetailActivity extends BaseActivity<SongPresenter> implements SongContract.View {

    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_songname)
    TextView tvSongName;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.md_singer)
    MusicDrawerItemView mdSinger;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.fl_view)
    FrameLayout flView;

    //SongActivity来的
    private long songId;

    private SongInfo songInfo;
    private String singerName;
    private long singerId;
    private String singerPicUrl;
    private Animation toTranslateIn;
    private Animation toTranslateOut;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_song_detail);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (toTranslateIn == null) {
            toTranslateIn = AnimationUtils.loadAnimation(this, R.anim.view_to_translate_in);
            toTranslateIn.setFillAfter(true);
            toTranslateIn.setStartOffset(200);
        }
        if (toTranslateOut == null) {
            toTranslateOut = AnimationUtils.loadAnimation(this, R.anim.view_to_translate_out);
            toTranslateOut.setFillAfter(true);
        }
    }

    @Override
    protected SongPresenter onCreatePresenter() {
        return new SongPresenter(this);
    }

    @Override
    protected void initModule() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        view.startAnimation(toTranslateIn);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            songInfo = intent.getParcelableExtra(MusicActivityMusic.SONG_INFO);
            singerId = Long.parseLong(songInfo.getArtistId());
            singerName = songInfo.getArtist();
            singerPicUrl = songInfo.getArtistKey();

            Glide.with(this).load(songInfo.getSongCover()).into(ivCover);
            tvSongName.setText(getString(R.string.songanme) + songInfo.getSongName());
            mdSinger.setText(getString(R.string.singer) + singerName);
            tvSinger.setText(singerName);
            songId = Long.parseLong(songInfo.getSongId());
        }
    }

    @Override
    @OnClick({R.id.view, R.id.md_nextplay, R.id.md_collect, R.id.md_download,
            R.id.md_commend, R.id.md_share, R.id.md_singer, R.id.md_video})
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.view:
                finish();
                break;
            case R.id.md_nextplay:
                XToastUtils.info("下一首播放咯");
                break;
            case R.id.md_collect:
                XToastUtils.info("收藏歌曲");
                break;
            case R.id.md_download:
                XToastUtils.info("下载");
                break;
            case R.id.md_commend:
                XToastUtils.info("评论");
                break;
            case R.id.md_share:
                XToastUtils.info("分享");
                break;
            case R.id.md_singer:
//                intent.setClass(SongDetailActivity.this, SingerActivity.class);
//                intent.putExtra(SingerActivity.SINGER_NAME, singerName);
//                intent.putExtra(SingerActivity.SINGER_ID, singerId);
//                intent.putExtra(SingerActivity.SINGER_PICURL, singerPicUrl);
//                startActivity(intent);
                break;
            case R.id.md_video:
                XToastUtils.info("查看MV");
        }
    }

    @Override
    public void finish() {
        super.finish();
        view.startAnimation(toTranslateOut);
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
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

    }

    @Override
    public void onGetPlaylistCommentFail(String e) {

    }
}
