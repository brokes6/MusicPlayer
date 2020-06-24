package com.example.musicplayerdome.personal.adapter;

import android.content.Context;
import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistEntity;

import java.util.ArrayList;

public class UserHomePagePlayListAdapter extends GroupedRecyclerViewAdapter {
    private final int TYPE_CREATE = 0;
    private final int TYPE_SUBSCRIBE = 1;
    private ArrayList<UserPlaylistEntity> mData;
    private RoundCornerImageView songlistimg;
    UserPlaylistBean userPlaylistBean;


    public UserHomePagePlayListAdapter(ArrayList<UserPlaylistEntity> entities, Context context) {
        super(context);
        this.mData = entities;
    }

    public void setData(UserPlaylistBean bean){
        userPlaylistBean = bean;
    }
    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).getPlaylist().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.item_user_info_playlist_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.item_more_info;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_mine_gedan_content;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        holder.setText(R.id.tv_user_info_create_right, mData.get(groupPosition).getHeader());
        if(groupPosition == TYPE_CREATE){
            holder.setText(R.id.tv_user_info_create_left, "创建的歌单");
        }else{
            holder.setText(R.id.tv_user_info_create_left, "收藏的歌单");
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        holder.setText(R.id.tv_more_info, mData.get(groupPosition).getFooter());
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        holder.setVisible(R.id.iv_item_gedan_more, false);

        UserPlaylistBean.PlaylistBean item = mData.get(groupPosition).getPlaylist().get(childPosition);
        //歌单名
        holder.setText(R.id.tv_item_gedan_content_toptext, item.getName());
        //歌单图片
        songlistimg = holder.get(R.id.iv_item_gedan_content_img);
        Glide.with(mContext).load(item.getCoverImgUrl()).into(songlistimg);
        if(groupPosition == TYPE_SUBSCRIBE){
            holder.setText(R.id.tv_item_gedan_content_bottomtext, item.getTrackCount() + "首,  by "+ item.getCreator().getNickname() +"  播放" + item.getPlayCount() + "次");
        }else if(groupPosition == TYPE_CREATE){
            holder.setText(R.id.tv_item_gedan_content_bottomtext, item.getTrackCount() + "首,  播放" + item.getPlayCount() + "次");

        }
    }
}
