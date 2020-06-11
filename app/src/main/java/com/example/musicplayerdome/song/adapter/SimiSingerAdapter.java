package com.example.musicplayerdome.song.adapter;


import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.search.bean.SimiSingerBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 类似歌手适配器（展示类似的歌手）
 * SimiSingerBean.ArtistsBean
 */
public class SimiSingerAdapter extends BaseRecyclerAdapter<SimiSingerBean.ArtistsBean> {
    private static final String TAG = "SimiSingerAdapter";
    private Context mContext;
    private List<SimiSingerBean.ArtistsBean> list = new ArrayList<>();
    private OnSimiSingerClickListener listener;
    private RelativeLayout rlSimi;
    private RoundCornerImageView ivCover;
    private TextView tvName;
    public SimiSingerAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_simi_singer;
    }

    public void setListener(OnSimiSingerClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SimiSingerBean.ArtistsBean item) {
        rlSimi = holder.findViewById(R.id.rl_simi);
        ivCover = holder.findViewById(R.id.iv_cover);
        tvName = holder.findViewById(R.id.tv_name);
        if (item!=null){
            setBean(mContext,item);
            setListener(listener, position);
        }
    }

    public void setBean(Context context, SimiSingerBean.ArtistsBean bean) {
        Glide.with(context).load(bean.getPicUrl()).into(ivCover);
        tvName.setText(bean.getName());
    }


    public void setListener(OnSimiSingerClickListener listener, int position) {
        rlSimi.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSimiClick(position);
            }
        });
    }

    public interface OnSimiSingerClickListener {
        void onSimiClick(int position);
    }

}
