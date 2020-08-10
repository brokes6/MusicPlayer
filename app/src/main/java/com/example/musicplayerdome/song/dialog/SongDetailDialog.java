package com.example.musicplayerdome.song.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.rewrite.MusicDrawerItemView;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.song.view.CommentActivity;
import com.example.musicplayerdome.song.view.SingerActivity;
import com.example.musicplayerdome.song.view.SongActivity;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;

/**
 * Created by fuxinbo on 2020.5.4
 * 音频功能弹窗列表
 */
public class SongDetailDialog extends Dialog implements SongContract.View,View.OnClickListener{
    private static final String TAG = "SongDetailDialog";
    private Context context;
    private Activity mContext;
    private ImageView ivCover;
    private TextView tvSongName;
    private TextView tvSinger;
    private MusicDrawerItemView mdSinger,md_commend,md_singer,md_nextplay,md_collect,md_download,md_share,md_video;
    private View sview;
    //SongActivity来的
    private long songId;
    private SongInfo msongInfo;
    private String singerName;
    private long singerId;
    private String singerPicUrl;
    private View view;

    public SongDetailDialog(@NonNull Context context,SongInfo songInfo,long id) {
        super(context, R.style.my_dialog);
        this.context = context;
        songId = id;
        msongInfo = songInfo;
        initView();
        init();
    }

    private void initView(){
        mContext = (Activity) context;
        view = mContext.getLayoutInflater().inflate(R.layout.activity_song_detail, null);
        ivCover = view.findViewById(R.id.iv_cover);
        md_nextplay = view.findViewById(R.id.md_nextplay);
        md_collect = view.findViewById(R.id.md_collect);
        md_download = view.findViewById(R.id.md_download);
        md_share = view.findViewById(R.id.md_share);
        md_video = view.findViewById(R.id.md_video);
        md_singer = view.findViewById(R.id.md_singer);
        md_commend = view.findViewById(R.id.md_commend);
        tvSongName = view.findViewById(R.id.tv_songname);
        tvSinger = view.findViewById(R.id.tv_singer);
        mdSinger = view.findViewById(R.id.md_singer);
        sview = view.findViewById(R.id.sview);

        md_nextplay.setOnClickListener(this);
        md_download.setOnClickListener(this);
        md_share.setOnClickListener(this);
        md_video.setOnClickListener(this);
        md_singer.setOnClickListener(this);
        md_commend.setOnClickListener(this);
        md_collect.setOnClickListener(this);
        sview.setOnClickListener(this);

        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置弹窗宽度
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //为弹窗绑定效果
        dialogWindow.setAttributes(lp);
    }

    private void init() {
        singerId = Long.parseLong(msongInfo.getArtistId());
        Log.e(TAG, "init: id数据为"+singerId);
        singerName = msongInfo.getArtist();
        singerPicUrl = msongInfo.getArtistKey();
        Glide.with(context).load(msongInfo.getSongCover()).into(ivCover);
        tvSongName.setText("歌名："+ msongInfo.getSongName());
        mdSinger.setText("歌手：" + singerName);
        tvSinger.setText(singerName);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.sview:
                dismiss();
                break;
            case R.id.md_commend:
                intent.setClass(mContext, CommentActivity.class);
                intent.putExtra(CommentActivity.ID, songId);
                intent.putExtra(CommentActivity.NAME, msongInfo.getSongName());
                intent.putExtra(CommentActivity.ARTIST, singerName);
                intent.putExtra(CommentActivity.COVER,msongInfo.getSongCover());
                intent.putExtra(CommentActivity.FROM, CommentActivity.SONG_COMMENT);
                mContext.startActivity(intent);
                dismiss();
                break;
            case R.id.md_singer:
                intent.setClass(mContext, SingerActivity.class);
                intent.putExtra(SingerActivity.SINGER_NAME, singerName);
                intent.putExtra(SingerActivity.SINGER_ID, singerId);
                intent.putExtra(SingerActivity.SINGER_PICURL, singerPicUrl);
                mContext.startActivity(intent);
                dismiss();
                break;
            case R.id.md_nextplay:
            case R.id.md_collect:
            case R.id.md_download:
            case R.id.md_share:
            case R.id.md_video:
                XToastUtils.info("抱歉,目前只能查看评论和歌手");
                break;
        }
    }
    private float startY;
    private float moveY = 0;
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY() - startY;
                view.scrollBy(0, -(int) moveY);
                startY = ev.getY();
                if (view.getScrollY() > 0) {
                    view.scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (view.getScrollY() < -this.getWindow().getAttributes().height / 4 && moveY > 0) {
                    this.dismiss();

                }
                view.scrollTo(0, 0);
                break;
        }
        return super.onTouchEvent(ev);
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

    /**
     * 重写show方法，将高度设置为全屏高度，取消谈起虚拟按键
     */
    public void show() {
        if (this.getWindow() != null) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            super.show();
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }
}
