package com.example.musicplayerdome.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.OnItemListenter;
import com.example.musicplayerdome.activity.MusicActivity;
import com.example.musicplayerdome.bean.Audio;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

public class MainMusicAdapter extends BaseRecyclerAdapter<Audio> {
    private static final String TAG = "MainMusicAdapter";
    private Context context;
    private OnItemListenter mItemClickListener;
    public MainMusicAdapter(Context context){
        this.context = context;
    }
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.music_item;
    }

    /**
     *通过activity那边设置监听（获取实例）来回调
     */
    public void setOnItemClickListener(OnItemListenter mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, final Audio item) {
        if (item!=null){
            holder.text(R.id.ms_id, (int) item.getId()+"");
            holder.text(R.id.ms_title,item.getName());
            holder.text(R.id.ms_author,item.getAuthor());
            holder.click(R.id.ms_main, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 回调
                     */
                    mItemClickListener.onItemClick (v,(int) item.getId());
                }
            });
        }
    }
}
