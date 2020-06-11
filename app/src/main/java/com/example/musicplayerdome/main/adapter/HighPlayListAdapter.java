package com.example.musicplayerdome.main.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.main.bean.PlaylistBean;
import com.example.musicplayerdome.main.bean.TopListBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

/**
 * 精品歌单适配器
 * PlaylistBean
 */
public class HighPlayListAdapter extends BaseRecyclerAdapter<PlaylistBean> {
    private Context mContext;
    private OnTopListClickListener listener;
    RoundCornerImageView ivToplistCover;
    RelativeLayout rlToplist;
    TextView tvToplistName;

    public HighPlayListAdapter(Context context){
        mContext = context;
    }

    public void setListener(OnTopListClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_toplist;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, PlaylistBean item) {
        ivToplistCover = holder.findViewById(R.id.iv_toplist);
        rlToplist = holder.findViewById(R.id.rl_toplist);
        tvToplistName = holder.findViewById(R.id.tv_toplist_name);
        if (item!=null){
            setBean(item);
            setOnClickListener(listener,position);
        }
    }

    private void setBean(PlaylistBean bean) {
        Glide.with(mContext)
                .load(bean.getPlaylistCoverUrl())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(ivToplistCover);
        tvToplistName.setText(bean.getPlaylistName());
    }

    private void setOnClickListener(OnTopListClickListener listener, int position) {
        rlToplist.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickTopList(position);
            }
        });
    }

    public interface OnTopListClickListener {
        void onClickTopList(int position);
    }

}
