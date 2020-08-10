package com.example.musicplayerdome.song.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SongContract;
import com.example.musicplayerdome.song.adapter.MusicListAdapter;
import com.example.musicplayerdome.main.bean.LikeListBean;
import com.example.musicplayerdome.rewrite.MaxHeightRecyclerView;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.bean.CommentLikeBean;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.bean.LyricBean;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.bean.PlayListCommentBean;
import com.example.musicplayerdome.song.bean.SongDetailBean;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuxinbo on 2020.5.4
 * 音频播列表弹窗
 */
public class SongListDialog extends Dialog implements SongContract.View,View.OnClickListener{
    private Activity mContext;
    private Context scontext;
    private String TAG = "--ErrorCorrectionDialog--";
    private MusicListAdapter adapter;
    private List<SongInfo> songList = new ArrayList<>();
    private View view;
    private MaxHeightRecyclerView recyclerView;
    private TextView tvPlayMode;
    private ImageView ivPlayMode,iv_trash_can;
    private RelativeLayout rl_play_mode;

    public SongListDialog(Context context) {
        super(context, R.style.my_dialog);
        this.scontext = context;
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mContext = (Activity) context;
        //为弹窗绑定视图
        view = mContext.getLayoutInflater().inflate(R.layout.activity_song_list, null);
        recyclerView = view.findViewById(R.id.rv_playlist);
        tvPlayMode = view.findViewById(R.id.tv_play_mode);
        ivPlayMode = view.findViewById(R.id.iv_play_mode);
        iv_trash_can = view.findViewById(R.id.iv_trash_can);
        rl_play_mode = view.findViewById(R.id.rl_play_mode);
        iv_trash_can.setOnClickListener(this);
        rl_play_mode.setOnClickListener(this);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        lp.height = (int)(display.getHeight()*0.5);
        //设置弹窗宽度
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //为弹窗绑定效果
        dialogWindow.setAttributes(lp);
        //绑定适配器
        LinearLayoutManager lin = new LinearLayoutManager(scontext);
        lin.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lin);
        adapter = new MusicListAdapter(scontext);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
        songList = SongPlayManager.getInstance().getSongList();
        adapter.loadMore(songList);

        if (SongPlayManager.getInstance().isPlaying() || SongPlayManager.getInstance().isPaused()) {
            Log.e(TAG, "init: 正在跳转位置;"+"当前index为"+SongPlayManager.getInstance().getCurrentSongIndex());
            recyclerView.scrollToPosition(SongPlayManager.getInstance().getCurrentSongIndex());
        }
        setPlayMode(SongPlayManager.getInstance().getMode());
    }

    private void setPlayMode(int playMode) {
        switch (playMode) {
            case SongPlayManager.MODE_LIST_LOOP_PLAY:
                tvPlayMode.setText("列表循环");
                setPlayModeImageColor(R.drawable.shape_list_cycle_grey);
                break;
            case SongPlayManager.MODE_SINGLE_LOOP_PLAY:
                tvPlayMode.setText("单曲循环");
                setPlayModeImageColor(R.drawable.shape_single_cycle_grey);
                break;
            case SongPlayManager.MODE_RANDOM:
                tvPlayMode.setText("随机播放");
                setPlayModeImageColor(R.drawable.shape_list_random_grey);
                break;
        }
    }
    private void setPlayModeImageColor(int resId) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(scontext.getResources(), resId, scontext.getTheme());
        vectorDrawableCompat.setTint(Color.parseColor("#999999"));
        ivPlayMode.setImageDrawable(vectorDrawableCompat);
    }

    MusicListAdapter.OnSongClickListener listener = new MusicListAdapter.OnSongClickListener() {
        @Override
        public void onMusicClick(int position) {
            SongPlayManager.getInstance().switchMusic(position);
            songList = SongPlayManager.getInstance().getSongList();
            adapter.refresh(songList);
            dismiss();
        }

        @Override
        public void onDelClick(int position) {
            SongPlayManager.getInstance().deleteSong(position);
            songList = SongPlayManager.getInstance().getSongList();
            adapter.refresh(songList);
            dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_play_mode:
                if (SongPlayManager.getInstance().getMode() == SongPlayManager.MODE_LIST_LOOP_PLAY) {
                    XToastUtils.info("已切换到单曲循环");
                    SongPlayManager.getInstance().setMode(SongPlayManager.MODE_SINGLE_LOOP_PLAY);
                } else if (SongPlayManager.getInstance().getMode() == SongPlayManager.MODE_SINGLE_LOOP_PLAY) {
                    XToastUtils.info("已切换到列表随机");
                    SongPlayManager.getInstance().setMode(SongPlayManager.MODE_RANDOM);
                } else if (SongPlayManager.getInstance().getMode() == SongPlayManager.MODE_RANDOM) {
                    XToastUtils.info("已切换到列表循环");
                    SongPlayManager.getInstance().setMode(SongPlayManager.MODE_LIST_LOOP_PLAY);
                }
                setPlayMode(SongPlayManager.getInstance().getMode());
                break;
            case R.id.iv_trash_can:
                SongPlayManager.getInstance().clearSongList();
                songList = SongPlayManager.getInstance().getSongList();
                adapter.refresh(songList);
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
