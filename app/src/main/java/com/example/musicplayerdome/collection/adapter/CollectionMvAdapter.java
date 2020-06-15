package com.example.musicplayerdome.collection.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.song.bean.SongMvBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * 歌手MV适配器
 * MvBean
 */
public class CollectionMvAdapter extends BaseRecyclerAdapter<MvSublistBean.DataBean> {
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
    public CollectionMvAdapter(Context context) {
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
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, MvSublistBean.DataBean item) {
        ivCover = holder.findViewById(R.id.iv_cover);
        tvName = holder.findViewById(R.id.tv_name);
        rlFeed = holder.findViewById(R.id.rl_feed);
        tvMv = holder.findViewById(R.id.tv_mv);
        if (item!=null){
            setBean(mContext, item);
            setListener(listener, position);
        }
    }

    public void setBean(Context context, MvSublistBean.DataBean bean) {
        tvName.setText(bean.getTitle());
        Glide.with(context).load(bean.getCoverUrl()).into(ivCover);

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
