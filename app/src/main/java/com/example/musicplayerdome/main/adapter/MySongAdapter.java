package com.example.musicplayerdome.main.adapter;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.bean.SongRecommendation;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

public class MySongAdapter extends BaseRecyclerAdapter<SongRecommendation> {
    @Override

    protected int getItemLayoutId(int viewType) {
        return R.layout.song_list_my;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SongRecommendation item) {
        if (item!=null){
            holder.image(R.id.my_song_img,item.getSongUrl());

        }
    }
}
