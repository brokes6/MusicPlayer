package com.example.musicplayerdome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.activity.MusicActivity;
import com.example.musicplayerdome.activity.SongSheetActivity;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.bean.RecommendMusicBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

public class RecommendMusicAdapter extends BaseRecyclerAdapter<Audio> {
    Context context;

    public RecommendMusicAdapter(Context context) {
        this.context = context;
    }
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.recommend_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, Audio item) {
        if(item!=null){
            holder.text(R.id.rec_text,item.getName());
            holder.text(R.id.rec_author,"-"+item.getAuthor());
            holder.text(R.id.rec_introduce,item.getAbstractInfo());
            holder.image(R.id.rec_img,item.getFaceUrl());
            holder.click(R.id.rplay, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MusicActivity.class);
                    intent.putExtra ("rid",(position+1));
                    intent.putExtra ("rkey",true);
                    context.startActivity(intent);
                }
            });
        }
    }
}
