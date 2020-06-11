package com.example.musicplayerdome.song.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.rewrite.RikkaRoundRectView;
import com.example.musicplayerdome.search.bean.AlbumAdapterBean;
import com.example.musicplayerdome.util.TimeUtil;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * 专辑展示适配器
 * AlbumAdapterBean
 */
public class AlbumAdapter extends BaseRecyclerAdapter<AlbumAdapterBean> {
    private static final String TAG = "AlbumAdapter";
    private Context mContext;
    private List<AlbumAdapterBean> list;
    private String keywords;
    private OnAlbumClickListener listener;
    private int type;
    private RikkaRoundRectView ivCover;
    private TextView tvName, tvDescription, tvSinger;
    private RelativeLayout rlAlbum;
    public AlbumAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_album_search;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setListener(OnAlbumClickListener listener) {
        this.listener = listener;
    }
    //type == 1 显示singgername、创建时间
    //type == 2 显示歌曲数目、创建时间
    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, AlbumAdapterBean item) {
        ivCover = holder.findViewById(R.id.iv_cover);
        tvName = holder.findViewById(R.id.tv_name);
        tvDescription = holder.findViewById(R.id.tv_description);
        rlAlbum = holder.findViewById(R.id.rl_album);
        tvSinger = holder.findViewById(R.id.tv_singer);
        if(item!=null){
            setBean(mContext, item, keywords, type);
            setListener(listener, position);
        }
    }
    void setBean(Context context, AlbumAdapterBean bean, String keywords, int type) {
        Glide.with(context)
                .load(bean.getAlbumCoverUrl())
                .placeholder(R.drawable.shape_album)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(ivCover);
        if (type == 1) {
            tvSinger.setVisibility(View.VISIBLE);
            if (bean.getAlbumName().contains(keywords)) {
                int start = bean.getAlbumName().indexOf(keywords);
                int end = start + keywords.length();
                String resString = bean.getAlbumName();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(style);
            } else {
                tvName.setText(bean.getAlbumName());
            }
            String artistName = bean.getSinger();
            Log.d(TAG, "artistName :" + artistName + " keywords:" + keywords);
            if (artistName.contains(keywords)) {
                int start = artistName.indexOf(keywords);
                int end = start + keywords.length();
                Log.d(TAG, "start :" + start + " end:" + end);
                SpannableStringBuilder style = new SpannableStringBuilder(artistName);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSinger.setText(style);
            } else {
                tvSinger.setText(artistName);
            }
            tvDescription.setText(TimeUtil.getTimeStandardOnlyYMD(bean.getCreateTime()));
        } else if (type == 2) {
            tvName.setText(bean.getAlbumName());
            tvDescription.setText(TimeUtil.getTimeStandardOnlyYMD(bean.getCreateTime()) + " 歌曲 " + bean.getSongCount());
        }
    }

    public void setListener(OnAlbumClickListener listener, int i) {
        rlAlbum.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlbumClick(i);
            }
        });
    }
    public interface OnAlbumClickListener {
        void onAlbumClick(int position);

    }
}
