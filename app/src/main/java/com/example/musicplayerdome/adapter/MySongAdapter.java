package com.example.musicplayerdome.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.activity.SongSheetActivityMusic;
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
            holder.click(R.id.my_song_img, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.startActivity(SongSheetActivityMusic.class);
                }
            });
        }
    }
}
