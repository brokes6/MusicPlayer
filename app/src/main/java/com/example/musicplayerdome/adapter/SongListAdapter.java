package com.example.musicplayerdome.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.bean.SongRecommendation;
import com.example.musicplayerdome.util.XToastUtils;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

public class SongListAdapter extends BaseRecyclerAdapter<SongRecommendation> {

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.song_list_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SongRecommendation item) {
        if (item!=null){
            holder.text(R.id.song_text,item.getSongText());
            holder.image(R.id.song_img,item.getSongUrl());
            holder.click(R.id.song_img, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XToastUtils.info("您点击了第"+(position+1)+"个歌单");
                }
            });
        }
    }
}
