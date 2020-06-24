package com.example.musicplayerdome.song.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.rewrite.RoundImageView;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.song.dialog.SongDetailDialog;
import com.example.musicplayerdome.song.view.SongActivity;
import com.lzx.starrysky.model.SongInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * 歌曲展示多功能适配器
 * 1.日推列表
 * 2.歌单列表
 * 3.搜索列表
 * 4.本地音乐（已取消）
 * SongInfo
 */
public class MySongListAdapter extends BaseRecyclerAdapter<SongInfo> {
    private static final String TAG = "MySongListAdapter";
    public List<SongInfo> list;
    private Context mContext;
    private int type;
    public String keywords;
    TextView tvName;
    TextView tvSinger;
    TextView tvSongNumber;
    ImageView ivSongDetail;
    LinearLayout llSong;
    ImageView ivPhone;
    RelativeLayout rlSong;
    RoundImageView ivCover;

    public MySongListAdapter(Context context) {
        mContext = context;
    }


    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_songlist;
    }

    /**
     * type=1：有封面 ,用于日推列表
     * type=2：有序号  用于歌单列表
     * type=3：无序号、无封面  用于搜索列表
     * type=4：无序号、无封面，有手机icon 用于本地音乐
     */
    public void setType(int type) {
        this.type = type;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public void setList(List<SongInfo> dataList){
        list = dataList;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, SongInfo item) {
        tvName = holder.findViewById(R.id.tv_songname);
        tvSinger = holder.findViewById(R.id.tv_singer);
        tvSongNumber = holder.findViewById(R.id.iv_songnumber);
        ivCover = holder.findViewById(R.id.iv_songcover);
        ivSongDetail = holder.findViewById(R.id.iv_songdetail);
        llSong = holder.findViewById(R.id.ll_song);
        ivPhone = holder.findViewById(R.id.iv_phone);
        rlSong = holder.findViewById(R.id.rl_song);
        if (item!=null){
            if (type == 3) {
                setSongInfo(mContext, item, keywords);
            } else {
                setSongInfo(mContext, item, position, type);
            }
            setSongClick(item, position);
        }
    }
    public void setSongInfo(Context context, SongInfo bean, int position, int type) {
        tvName.setText(bean.getSongName());
        tvSinger.setText(bean.getArtist());
        if (type == 1) {
            ivCover.setVisibility(View.VISIBLE);
            Glide.with(context).load(bean.getSongCover()).transition(new DrawableTransitionOptions().crossFade()).into(ivCover);
        } else if (type == 2) {
            tvSongNumber.setVisibility(View.VISIBLE);
            tvSongNumber.setText((position + 1) + "");
        } else if (type == 4) {
            ivPhone.setVisibility(View.VISIBLE);
            ivSongDetail.setVisibility(View.GONE);
        }
    }
    public void setSongInfo(Context context, SongInfo bean, String keywords) {
        if (bean.getSongName().contains(keywords)) {
            int start = bean.getSongName().indexOf(keywords);
            int end = start + keywords.length();
            String resString = bean.getSongName();
            SpannableStringBuilder style = new SpannableStringBuilder(resString);
            style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvName.setText(style);
        } else {
            tvName.setText(bean.getSongName());
        }
        if (bean.getArtist().contains(keywords)) {
            int start = bean.getArtist().indexOf(keywords);
            int end = start + keywords.length();
            String resString = bean.getArtist();
            SpannableStringBuilder style = new SpannableStringBuilder(resString);
            style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSinger.setText(style);
        } else {
            tvSinger.setText(bean.getArtist());
        }
    }
    public void setSongClick(SongInfo songInfo, int position) {
        rlSong.setOnClickListener(v -> {
            if (type == 3) {
                SongPlayManager.getInstance().clickASong(songInfo);
            } else {
                SongPlayManager.getInstance().clickPlayAll(list, position);
            }
            Intent intent = new Intent(mContext, SongActivity.class);
            intent.putExtra(SongActivity.SONG_INFO, songInfo);
            mContext.startActivity(intent);
        });

        ivSongDetail.setOnClickListener(v -> {
            SongDetailDialog songDetailDialog = new SongDetailDialog(mContext,songInfo,Long.valueOf(songInfo.getSongId()));
            songDetailDialog.setCanceledOnTouchOutside(true);
            songDetailDialog.show();
        });
    }
    public void PlayAll(){
        if (list!=null){
            SongPlayManager.getInstance().clickPlayAll(list, 0);
        }
    }
}
