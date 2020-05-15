package com.example.musicplayerdome.song.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.lzx.starrysky.model.SongInfo;

public class SongDetailDialog extends Dialog implements SongContract.View,View.OnClickListener{
    private Context context;
    private Activity mContext;
    ImageView ivCover;
    TextView tvSongName;
    TextView tvSinger;
    MusicDrawerItemView mdSinger;
    View sview;
    //SongActivity来的
    private long songId;
    private SongInfo songInfo;
    private String singerName;
    private String singerId;
    private String singerPicUrl;
    private View view;

    public SongDetailDialog(@NonNull Context context,SongInfo songInfo) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.songInfo = songInfo;
        initView();
        init();
    }

    private void initView(){
        mContext = (Activity) context;
        view = mContext.getLayoutInflater().inflate(R.layout.activity_song_detail, null);
        ivCover = view.findViewById(R.id.iv_cover);
        tvSongName = view.findViewById(R.id.tv_songname);
        tvSinger = view.findViewById(R.id.tv_singer);
        mdSinger = view.findViewById(R.id.md_singer);
        sview = view.findViewById(R.id.sview);
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
        singerId = songInfo.getArtistId();
        singerName = songInfo.getArtist();
        singerPicUrl = songInfo.getArtistKey();
        Glide.with(context).load(songInfo.getSongCover()).into(ivCover);
        tvSongName.setText("歌名："+ songInfo.getSongName());
        mdSinger.setText("歌手：" + singerName);
        tvSinger.setText(singerName);
        songId = Long.parseLong(songInfo.getSongId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sview:
                dismiss();
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

}