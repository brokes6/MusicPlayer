package com.example.musicplayerdome.collection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.rewrite.RoundImageView;
import com.example.musicplayerdome.song.view.SingerActivity;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

public class CSingersAdapter extends BaseRecyclerAdapter<ArtistSublistBean.DataBean> {
    private RoundImageView iv_cover;
    private TextView tv_singer;
    private Context mContext;
    public CSingersAdapter(Context context){
        mContext = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.cmv_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, ArtistSublistBean.DataBean item) {
        iv_cover = holder.findViewById(R.id.iv_cover);
        tv_singer = holder.findViewById(R.id.tv_singer);
        if (item!=null){
            Glide.with(mContext).load(item.getPicUrl()).into(iv_cover);
            tv_singer.setText(item.getName());
        }
        holder.click(R.id.tv_singer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, SingerActivity.class);
                intent.putExtra(SingerActivity.SINGER_NAME, item.getName());
                intent.putExtra(SingerActivity.SINGER_ID, item.getId());
                intent.putExtra(SingerActivity.ISCOLLECTION, true);
                mContext.startActivity(intent);
            }
        });
    }
}
