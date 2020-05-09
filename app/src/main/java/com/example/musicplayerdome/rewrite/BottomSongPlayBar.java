package com.example.musicplayerdome.rewrite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.MyApplication;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.song.MusicPauseEvent;
import com.example.musicplayerdome.song.MusicStartEvent;
import com.example.musicplayerdome.song.SongPlayManager;
import com.example.musicplayerdome.song.StopMusicEvent;
import com.example.musicplayerdome.song.view.SongActivity;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.lzx.starrysky.model.SongInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 界面底部的歌曲遥控器
 * 用EventBus来控制它
 * Created By Rikka on 2019/7/22
 */
public class BottomSongPlayBar extends RelativeLayout {
    private static final String TAG = "BottomSongPlayBar";

    private Context mContext;

    private RelativeLayout rlSongController;
    private RoundImageView ivCover;
    private ImageView ivPlay, ivController,next,pre;
    private RollTextView tvSongName;
    private TextView tvSongSinger;
    private LinearLayout llSongInfo;

    private SongInfo currentSongInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayMusicEvent(MusicStartEvent event) {
        Log.d(TAG, "MusicStartEvent :" + event);
        setSongBean(event.getSongInfo());
        ivPlay.setImageResource(R.mipmap.audio_state_play);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopMusicEvent(StopMusicEvent event) {
        Log.d(TAG, "onStopMusicEvent");
        setSongBean(event.getSongInfo());
        ivPlay.setImageResource(R.mipmap.audio_state_pause);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseMusicEvent(MusicPauseEvent event) {
        Log.d(TAG, "onPauseMusicEvent");
        ivPlay.setImageResource(R.mipmap.audio_state_pause);
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
        ivPlay = rlSongController.findViewById(R.id.btn_custom_play);
//        ivController = rlSongController.findViewById(R.id.iv_bottom_controller);
        tvSongName = rlSongController.findViewById(R.id.tv_songname);
        tvSongSinger = rlSongController.findViewById(R.id.tv_singer);
        llSongInfo = rlSongController.findViewById(R.id.ll_songinfo);
        next = rlSongController.findViewById(R.id.btn_custom_next);
        pre = rlSongController.findViewById(R.id.btn_custom_prev);
    }

    private void initListener() {
        ivCover.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SongActivity.class);
            intent.putExtra(SongActivity.SONG_INFO, currentSongInfo);
            mContext.startActivity(intent);
        });
        next.setOnClickListener(v -> {
            SongPlayManager.getInstance().playNextMusic();
        });
        pre.setOnClickListener(v -> {
            SongPlayManager.getInstance().playPreMusic();

        });
        llSongInfo.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SongActivity.class);
            intent.putExtra(SongActivity.SONG_INFO, currentSongInfo);
            mContext.startActivity(intent);
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
                ivPlay.setImageResource(R.mipmap.audio_state_play);
            }
        }
    }


    public void setSongBean(SongInfo bean) {
        currentSongInfo = bean;
        tvSongName.setText(bean.getSongName());
        tvSongSinger.setText(bean.getArtist());
        if (!TextUtils.isEmpty(bean.getSongCover())) {
            Glide.with(MyApplication.getContext()).load(bean.getSongCover()).into(ivCover);
        }
    }
}