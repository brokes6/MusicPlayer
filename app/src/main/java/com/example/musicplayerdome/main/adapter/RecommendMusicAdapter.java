package com.example.musicplayerdome.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.main.bean.RecommendsongBean;
import com.example.musicplayerdome.yuncun.adapter.YuncunAdapter;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

/**
 * 主页推荐歌曲适配器
 * RecommendsongBean.resultData
 */
public class RecommendMusicAdapter extends BaseRecyclerAdapter<RecommendsongBean.resultData> {
    Context context;
    TextView M_name,M_author;
    ImageView img,rplay;
    private RecommendMusicItemClickListener listener;
    public RecommendMusicAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.recommend_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, RecommendsongBean.resultData item) {
        rplay = holder.findViewById(R.id.rplay);
        img = holder.findViewById(R.id.rec_img);
        M_name = holder.findViewById(R.id.rec_text);
        M_author = holder.findViewById(R.id.rec_author);
        if (item!=null){
            M_name.setText(item.getName());
            M_author.setText(item.getSong().getArtists().get(0).getName());
            Glide.with(context).load(item.getPicUrl()).transition(new DrawableTransitionOptions().crossFade()).into(img);
            onSetListClickListener(listener,position);
        }
    }

    public void onSetListClickListener(RecommendMusicItemClickListener listener, int i) {
        rplay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayListItemClick(i);
            }
        });
    }

    public interface RecommendMusicItemClickListener {
        void onPlayListItemClick(int position);
    }

    public void setListener(RecommendMusicItemClickListener listener) {
        this.listener = listener;
    }

}
