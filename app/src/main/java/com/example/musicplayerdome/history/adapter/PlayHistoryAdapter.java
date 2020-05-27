package com.example.musicplayerdome.history.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.personal.bean.PlayListItemBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

public class PlayHistoryAdapter extends BaseRecyclerAdapter<PlayListItemBean> {
    private static final String TAG = "playHistoryAdapter";
    private Context mContext;
    private String nickname;
    private List<PlayListItemBean> list;
    private RoundCornerImageView ivCover;
    private OnPlayListItemClickListener listener;
    private LinearLayout llItem;

    public PlayHistoryAdapter(Context context) {
        mContext = context;
    }
    public void setName(String nickName) {
        this.nickname = nickName;
    }
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_playlist_fragment;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, PlayListItemBean item) {
        llItem = holder.findViewById(R.id.ll_item);
        ivCover = holder.findViewById(R.id.iv_cover);
        if(item!=null) {
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
                holder.text(R.id.tv_playlist_info, "全部已播放的歌曲");
            } else {
                holder.text(R.id.tv_playlist_info, item.getSongNumber() + "首");
            }
        }
        onSetListClickListener(listener,position);
    }
    public void onSetListClickListener(OnPlayListItemClickListener listener, int i) {

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
