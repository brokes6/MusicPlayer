package com.example.musicplayerdome.song.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.search.bean.MvBean;
import com.example.musicplayerdome.song.view.SongMvActivity;
import com.example.musicplayerdome.util.TimeUtil;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 视频/MV适配器
 * 1.搜索出来的视频
 * 2.歌手的MV
 * MvBean
 */
public class FeedAdapter extends BaseRecyclerAdapter<MvBean> {
    private static final String TAG = "FeedAdapter";
    private Context mContext;
    private List<MvBean> list;
    private String keywords;
    private int type;
    private OnSimiSingerClickListener listener;
    private RoundCornerImageView ivCover;
    private TextView tvName;
    private TextView tvCreator, tvMv;
    private RelativeLayout rlFeed;
    public FeedAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_feed_search;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setListener(OnSimiSingerClickListener listener) {
        this.listener = listener;
    }

    //type==1，显示蓝色字体
    //type==2，不显示
    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, MvBean item) {
        ivCover = holder.findViewById(R.id.iv_cover);
        tvName = holder.findViewById(R.id.tv_name);
        tvCreator = holder.findViewById(R.id.tv_creator);
        rlFeed = holder.findViewById(R.id.rl_feed);
        tvMv = holder.findViewById(R.id.tv_mv);
        if (item!=null){
            setBean(mContext, item, keywords, type);
            setListener(listener, position);
        }
    }

    public void setBean(Context context, MvBean videosBean, String keywords, int type) {
        if (!judgeContainsStr(videosBean.getVid())) {
            //不包含字母，则说明是MV
            tvMv.setVisibility(View.VISIBLE);
        } else {
            tvMv.setVisibility(View.GONE);
        }
        if (type == 1) {
            if (videosBean.getTitle().contains(keywords)) {
                int start = videosBean.getTitle().indexOf(keywords);
                int end = start + keywords.length();
                String resString = videosBean.getTitle();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(style);
            } else {
                tvName.setText(videosBean.getTitle());
            }
            if (videosBean.getCreator().get(0).getUserName().contains(keywords)) {
                int start = videosBean.getCreator().get(0).getUserName().indexOf(keywords);
                int end = start + keywords.length();
                String resString = videosBean.getCreator().get(0).getUserName();
                SpannableStringBuilder style = new SpannableStringBuilder(resString);
                style.setSpan(new ForegroundColorSpan(Color.parseColor(context.getString(R.string.colorBlue))), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvCreator.setText(TimeUtil.getTimeNoYMDH(videosBean.getDuration()) + "  by " + style);
            } else {
                tvCreator.setText(TimeUtil.getTimeNoYMDH(videosBean.getDuration()) + "  by " + videosBean.getCreator().get(0).getUserName());
            }
        } else if (type == 2) {
            tvName.setText(videosBean.getTitle());
            tvCreator.setText(TimeUtil.getTimeNoYMDH(videosBean.getDuration()));
        }
        Glide.with(context).load(videosBean.getCoverUrl()).into(ivCover);
    }

    public void setListener(OnSimiSingerClickListener listener, int position) {
        rlFeed.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSimiClick(position);
            }
        });
    }
    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    public interface OnSimiSingerClickListener {
        void onSimiClick(int position);
    }

}
