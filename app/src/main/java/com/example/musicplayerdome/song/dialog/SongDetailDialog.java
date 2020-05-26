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
import com.lzx.starrysky.model.SongInfo;

public class SongDetailDialog extends Dialog implements SongContract.View,View.OnClickListener{
    private static final String TAG = "SongDetailDialog";
    private Context context;
    private Activity mContext;
    ImageView ivCover;
    TextView tvSongName;
    TextView tvSinger;
    MusicDrawerItemView mdSinger,md_commend,md_singer;
    View sview;
    //SongActivity来的
    private long songId;
    private SongInfo msongInfo;
    private String singerName;
    private long singerId;
    private String singerPicUrl;
    private View view;

    public SongDetailDialog(@NonNull Context context,SongInfo songInfo) {
        super(context, R.style.my_dialog);
        this.context = context;
        msongInfo = songInfo;
        initView();
        init();
    }

    private void initView(){
        mContext = (Activity) context;
        view = mContext.getLayoutInflater().inflate(R.layout.activity_song_detail, null);
        ivCover = view.findViewById(R.id.iv_cover);
        md_singer = view.findViewById(R.id.md_singer);
        md_commend = view.findViewById(R.id.md_commend);
        tvSongName = view.findViewById(R.id.tv_songname);
        tvSinger = view.findViewById(R.id.tv_singer);
        mdSinger = view.findViewById(R.id.md_singer);
        sview = view.findViewById(R.id.sview);
        md_singer.setOnClickListener(this);
        md_commend.setOnClickListener(this);
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
        songId = Long.parseLong(msongInfo.getSongId());
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
