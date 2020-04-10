package com.example.musicplayerdome.adapter;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.DialogClickCallBack;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.bean.MusicListIntroduce;
import com.example.musicplayerdome.dialog.MusicList;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends BaseRecyclerAdapter<Audio> {
    private List<Audio> test1s=new ArrayList<>();
    private DialogClickCallBack dialogClickCallBack;
    private static final String TAG = "MusicAdapter";

    public MusicAdapter(DialogClickCallBack callBack){
        this.dialogClickCallBack = callBack;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.music_item;
    }


    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, Audio item) {
        if (item!=null){
            holder.text(R.id.ms_id, (int) item.getId()+".");
            holder.text(R.id.ms_title,item.getName());
            holder.click(R.id.ms_main,new OnTvClickListener((int)item.getId()));

        }
    }

    private class OnTvClickListener implements android.view.View.OnClickListener{
        private int musicid;
        public OnTvClickListener(int id) {
            if (id>0){
                musicid = id-1;
            }else{
                musicid = id;
            }
        }

        @Override
        public void onClick(View arg0) {
            /**把点击的view Id传出去*/
            dialogClickCallBack.viewClick(musicid);
        }
    }
}

