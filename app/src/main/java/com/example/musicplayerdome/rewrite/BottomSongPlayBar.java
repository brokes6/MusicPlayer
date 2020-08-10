package com.example.musicplayerdome.rewrite;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.MyApplication;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.api.ApiService;
import com.example.musicplayerdome.song.bean.LikeMusicBean;
import com.example.musicplayerdome.song.other.MusicPauseEvent;
import com.example.musicplayerdome.song.other.MusicStartEvent;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.other.SongPresenter;
import com.example.musicplayerdome.song.other.StopMusicEvent;
import com.example.musicplayerdome.song.view.FMSongActivity;
import com.example.musicplayerdome.song.view.SongActivity;
import com.example.musicplayerdome.song.dialog.SongListDialog;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 界面底部的歌曲遥控器
 * 用EventBus来控制它
 * 分为两种模式（1.普通音乐，2.FM私人音乐）
 * Created By fuxinbo on 2020/5/22
 */
public class BottomSongPlayBar extends RelativeLayout {
    private static final String TAG = "BottomSongPlayBar";

    private Context mContext;

    private RelativeLayout rlSongController;
    private RoundImageView ivCover;
    private ImageView ivPlay, ivController,songlsit,iv_Like;
    private RollTextView tvSongName;
    private TextView tvSongSinger;
    private LinearLayout llSongInfo;
    private SongListDialog songListDialog;
    private SongInfo currentSongInfo;
    private int key;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayMusicEvent(MusicStartEvent event) {
        Log.d(TAG, "MusicStartEvent :" + event);
        setSongBean(event.getSongInfo());
        ivPlay.setImageResource(R.drawable.shape_pause_black);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopMusicEvent(StopMusicEvent event) {
        Log.d(TAG, "onStopMusicEvent");
        setSongBean(event.getSongInfo());
        ivPlay.setImageResource(R.drawable.shape_play_black);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseMusicEvent(MusicPauseEvent event) {
        Log.d(TAG, "onPauseMusicEvent");
        ivPlay.setImageResource(R.drawable.shape_play_black);
    }

    public BottomSongPlayBar(Context context) {
        this(context, null);
    }

    public BottomSongPlayBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSongPlayBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);
        mContext = context;
        initView();
        initListener();
        initSongInfo();
    }

    private void initView() {
        rlSongController = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_songplay_control, this, true);
        ivCover = rlSongController.findViewById(R.id.iv_cover);
        iv_Like = rlSongController.findViewById(R.id.iv_Like);
        ivPlay = rlSongController.findViewById(R.id.btn_custom_play);
//        ivController = rlSongController.findViewById(R.id.iv_bottom_controller);
        tvSongName = rlSongController.findViewById(R.id.tv_songname);
        tvSongSinger = rlSongController.findViewById(R.id.tv_singer);
        llSongInfo = rlSongController.findViewById(R.id.ll_songinfo);
        songlsit = rlSongController.findViewById(R.id.btn_custom_list);
        key = (int) SharedPreferencesUtil.getData("Ykey",0);
        switch (key){
            case 1:
                iv_Like.setVisibility(View.VISIBLE);
                songlsit.setVisibility(View.GONE);
                break;
            case 2:
                iv_Like.setVisibility(View.GONE);
                songlsit.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initListener() {
        ivCover.setOnClickListener(v -> {
            switch (key){
                case 1:
                    Intent intent = new Intent(mContext, FMSongActivity.class);
                    intent.putExtra(FMSongActivity.SONG_INFO, currentSongInfo);
                    mContext.startActivity(intent);
                    break;
                case 2:
                    Intent intent2 = new Intent(mContext, SongActivity.class);
                    intent2.putExtra(SongActivity.SONG_INFO, currentSongInfo);
                    mContext.startActivity(intent2);
                    break;
            }

        });
        iv_Like.setOnClickListener(v -> {
            iv_Like.setImageResource(R.drawable.shape_like_white);
//            ApiService service = ApiEngine.getInstance().getApiService();
//            service.likeMusice(SongPlayManager.getInstance().getsongID())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<LikeMusicBean>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(LikeMusicBean likeMusicBean) {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
        });
        songlsit.setOnClickListener(v -> {
            songListDialog = new SongListDialog(mContext);
            songListDialog.setCanceledOnTouchOutside(true);
            songListDialog.show();
        });
        llSongInfo.setOnClickListener(v -> {
            switch (key){
                case 1:
                    Intent intent = new Intent(mContext, FMSongActivity.class);
                    intent.putExtra(FMSongActivity.SONG_INFO, currentSongInfo);
                    mContext.startActivity(intent);
                    break;
                case 2:
                    Intent intent2 = new Intent(mContext, SongActivity.class);
                    intent2.putExtra(SongActivity.SONG_INFO, currentSongInfo);
                    mContext.startActivity(intent2);
                    break;
            }
        });

        ivPlay.setOnClickListener(v -> {
            Log.d(TAG, "ivPlay OnClick");
            if (!SongPlayManager.getInstance().isPlaying()) {
                Log.d(TAG, "playMusic");
                SongPlayManager.getInstance().clickBottomContrllerPlay(currentSongInfo);
            } else {
                Log.d(TAG, "pauseMusic");
                SongPlayManager.getInstance().pauseMusic();
            }
        });

//        ivController.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, SongListActivity.class);
//            mContext.startActivity(intent);
//            ((Activity) mContext).overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
//        });
    }

    private void initSongInfo() {
        //初始化的时候，要从本地拿最近一次听的歌曲
        currentSongInfo = SharePreferenceUtil.getInstance(MyApplication.getContext()).getLatestSong();
        if (currentSongInfo != null) {
            setSongBean(currentSongInfo);
            Log.d(TAG, "isPlaying " + SongPlayManager.getInstance().isPlaying());
            if (SongPlayManager.getInstance().isPlaying()) {
                ivPlay.setImageResource(R.drawable.shape_pause_black);
            }
        }
    }


    public void setSongBean(SongInfo bean) {
        currentSongInfo = bean;
        key = (int) SharedPreferencesUtil.getData("Ykey",0);
        tvSongName.setText(bean.getSongName());
        tvSongSinger.setText(bean.getArtist());
        if (!TextUtils.isEmpty(bean.getSongCover())) {
            Glide.with(MyApplication.getContext())
                    .load(bean.getSongCover())
                    .placeholder(R.drawable.shape_record)
                    .into(ivCover);
        }
        switch (key){
            case 1:
                iv_Like.setVisibility(View.VISIBLE);
                songlsit.setVisibility(View.GONE);
                break;
            case 2:
                iv_Like.setVisibility(View.GONE);
                songlsit.setVisibility(View.VISIBLE);
                break;
        }
    }
}