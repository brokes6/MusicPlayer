package com.example.musicplayerdome.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.bean.SongRecommendation;
import com.example.musicplayerdome.main.bean.PlaylistBean;
import com.example.musicplayerdome.util.XToastUtils;
import com.lzx.starrysky.model.SongInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * 主页推荐歌单
 * PlaylistBean
 */
public class SongListAdapter extends BaseRecyclerAdapter<PlaylistBean> {
    private Context mContext;
    private OnPlayListClickListener listener;
    private int type;

    private LinearLayout linearLayout;
    private RoundCornerImageView imageView;
    private TextView textView;

    public SongListAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.song_list_item;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, PlaylistBean item) {
        linearLayout = holder.findViewById(R.id.home_playlist);
        imageView = holder.findViewById(R.id.song_img);
        textView = holder.findViewById(R.id.song_text);
        if (item!=null){
            setPlayListInfo(mContext, item);
            onSetListClickListener(listener, position,imageView,textView);
        }
    }

    public void setPlayListInfo(Context context, PlaylistBean bean) {
        Glide.with(context)
                .load(bean.getPlaylistCoverUrl())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
        textView.setText(bean.getPlaylistName());
    }

    public void onSetListClickListener(OnPlayListClickListener listener, int position,RoundCornerImageView imageView,TextView textView) {
        linearLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickListener(position,imageView,textView);
            }
        });
    }


    public interface OnPlayListClickListener {
        void onClickListener(int position,RoundCornerImageView imageView,TextView textView);
    }

    public void setListener(OnPlayListClickListener listener) {
        this.listener = listener;
    }
}
