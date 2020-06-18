package com.example.musicplayerdome.main.adapter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.util.JzViewOutlineProvider;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class RecommemdedVideoAdapter extends BaseRecyclerAdapter<RecommendedVideoBean> {
    private Context mcontext;
    private JzvdStd jzVideo;
    private TextView videoname,username;
    private CircleImageView vidoeima,userimg;
    public RecommemdedVideoAdapter(Context context){
        mcontext = context;
    }
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.rec_video_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, RecommendedVideoBean item) {
        jzVideo = holder.findViewById(R.id.jz_video);
        jzVideo.setOutlineProvider(new JzViewOutlineProvider(50));
        jzVideo.setClipToOutline(true);
        Jzvd.FULLSCREEN_ORIENTATION= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        videoname = holder.findViewById(R.id.R_video_name);
        username = holder.findViewById(R.id.R_user_name);
        vidoeima = holder.findViewById(R.id.R_videouser_img);
        userimg = holder.findViewById(R.id.R_user_img);
        if (item!=null){
            String url = item.getDatas().get(position).getVData().getUrlInfo().getUrl();
            String name = item.getDatas().get(position).getVData().getTitle();
            jzVideo.setUp(url,name);
            Glide.with(mcontext).load(item.getDatas().get(position).getVData().getCoverUrl()).into(jzVideo.posterImageView);

            videoname.setText(name);

            username.setText(item.getDatas().get(position).getVData().getCreator().getNickname());
            Glide.with(mcontext).load(item.getDatas().get(position).getVData().getCreator().getAvatarUrl()).into(userimg);

        }
    }
}
