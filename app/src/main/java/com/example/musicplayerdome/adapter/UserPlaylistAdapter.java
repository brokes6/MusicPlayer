package com.example.musicplayerdome.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.personal.bean.PlayListItemBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

public class UserPlaylistAdapter extends BaseRecyclerAdapter<PlayListItemBean> {
    private static final String TAG = "UserPlaylistAdapter";
    private Context mContext;
    private List<PlayListItemBean> list;
    private OnPlayListItemClickListener listener;
    private String nickname;
    private boolean isShowSmartPlay = false;
    private RoundCornerImageView ivCover;
    private RelativeLayout rlSmartPlay;
    private LinearLayout llItem;

    public UserPlaylistAdapter(Context context) {
        mContext = context;
    }

    public void setShowSmartPlay(boolean showSmartPlay) {
        isShowSmartPlay = showSmartPlay;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_playlist_fragment;
    }

    public void setName(String nickName) {
        this.nickname = nickName;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, PlayListItemBean item) {
        if(item!=null) {
            llItem = holder.findViewById(R.id.ll_item);
            ivCover = holder.findViewById(R.id.iv_cover);
            Glide.with(mContext).load(item.getCoverUrl()).transition(new DrawableTransitionOptions().crossFade()).into(ivCover);
            holder.text(R.id.tv_playlist_name, item.getPlayListName());
            long playcount = item.getPlayCount();
            String count;
            if (playcount >= 10000) {
                playcount = playcount / 10000;
                count = playcount + "万次";
            } else {
                count = playcount + "次";
            }
            if (nickname.equals(item.getPlaylistCreator())) {
                holder.text(R.id.tv_playlist_info, item.getSongNumber() + "首，播放" + count);
            } else {
                holder.text(R.id.tv_playlist_info, item.getSongNumber() + "首");
                // + item.getPlaylistCreator() + "，播放" + count
            }
//            if (isShowSmartPlay && position == 0) {
//                holder.visible(R.id.rl_heart_play, View.VISIBLE);
//            } else {
//                holder.visible(R.id.rl_heart_play, View.GONE);
//            }
        }
        onSetListClickListener(listener,position);
    }
    public void onSetListClickListener(OnPlayListItemClickListener listener, int i) {
//        rlSmartPlay.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onSmartPlayClick(getSelectPosition());
//            }
//        });

        llItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayListItemClick(i);
            }
        });
    }
    public interface OnPlayListItemClickListener {
        void onPlayListItemClick(int position);

        void onSmartPlayClick(int position);
    }

    public void setListener(OnPlayListItemClickListener listener) {
        this.listener = listener;
    }
}
