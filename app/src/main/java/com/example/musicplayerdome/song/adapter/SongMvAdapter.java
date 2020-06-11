package com.example.musicplayerdome.song.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.search.bean.MvBean;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.example.musicplayerdome.util.TimeUtil;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * 歌手MV适配器
 * MvBean
 */
public class SongMvAdapter extends BaseRecyclerAdapter<SongMvBean.MvData> {
    private static final String TAG = "FeedAdapter";
    private Context mContext;
    private List<SongMvBean> list;
    private String keywords;
    private int type;
    private OnSimiSingerClickListener listener;
    private RoundCornerImageView ivCover;
    private TextView tvName;
    private TextView tvCreator, tvMv;
    private RelativeLayout rlFeed;
    public SongMvAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_feed_search;
    }

    public void setListener(OnSimiSingerClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SongMvBean.MvData item) {
        ivCover = holder.findViewById(R.id.iv_cover);
        tvName = holder.findViewById(R.id.tv_name);
        tvCreator = holder.findViewById(R.id.tv_creator);
        rlFeed = holder.findViewById(R.id.rl_feed);
        tvMv = holder.findViewById(R.id.tv_mv);
        if (item!=null){
            setBean(mContext, item);
            setListener(listener, position);
        }
    }

    public void setBean(Context context, SongMvBean.MvData bean) {
        tvName.setText(bean.getName());
        tvCreator.setText(bean.getPublishTime());
        Glide.with(context).load(bean.getImgurl()).into(ivCover);

    }

    public void setListener(OnSimiSingerClickListener listener, int position) {
        rlFeed.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSimiClick(position);
            }
        });
    }


    public interface OnSimiSingerClickListener {
        void onSimiClick(int position);
    }
}
