package com.example.musicplayerdome.song.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.song.other.MusicStartEvent;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.lzx.starrysky.model.SongInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 歌手热门歌曲适配器
 * SongInfo
 */
public class MusicListAdapter extends BaseRecyclerAdapter<SongInfo> {
    private static final String TAG = "MusicListAdapter";
    private OnSongClickListener listener;
    TextView tvSongName, tvSingerName, tvLink;
    ImageView ivDel, ivHorn;
    RelativeLayout rlSongPlay;

    public MusicListAdapter(Context context) {
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicStartEvent(MusicStartEvent event) {
        refresh(SongPlayManager.getInstance().getSongList());
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_song_list;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SongInfo item) {
        tvSongName = holder.findViewById(R.id.tv_music_name);
        tvSingerName = holder.findViewById(R.id.tv_artist_name);
        tvLink = holder.findViewById(R.id.tv_link);
        ivDel = holder.findViewById(R.id.iv_del);
        ivHorn = holder.findViewById(R.id.iv_horn);
        rlSongPlay = holder.findViewById(R.id.rl_song_play);
        if (item!=null){
            setBean(item);
            setListener(listener,position);
        }
    }

    public void setBean(SongInfo songInfo) {
        tvSongName.setText(songInfo.getSongName());
        tvSingerName.setText(songInfo.getArtist());
        if (SongPlayManager.getInstance().isCurMusicPlaying(songInfo.getSongId())) {
            ivHorn.setVisibility(View.VISIBLE);
            tvSongName.setTextColor(Color.parseColor("#D53B32"));
            tvLink.setTextColor(Color.parseColor("#D53B32"));
            tvSingerName.setTextColor(Color.parseColor("#D53B32"));
        } else {
            ivHorn.setVisibility(View.GONE);
            tvSongName.setTextColor(Color.parseColor("#333333"));
            tvLink.setTextColor(Color.parseColor("#808080"));
            tvSingerName.setTextColor(Color.parseColor("#808080"));
        }
    }


    public void setListener(OnSongClickListener listener, int i) {
        rlSongPlay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMusicClick(i);
            }
        });

        ivDel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelClick(i);
            }
        });
    }

    public interface OnSongClickListener {
        void onMusicClick(int position);

        void onDelClick(int position);
    }

    public void setListener(OnSongClickListener listener) {
        this.listener = listener;
    }
}
