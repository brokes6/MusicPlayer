package com.example.musicplayerdome.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.main.bean.PlaylistBean;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.personal.view.PersonalActivity;
import com.example.musicplayerdome.util.JzViewOutlineProvider;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static com.example.musicplayerdome.personal.view.PersonalActivity.USER_ID;

/**
 * 推荐视频
 */
public class RecommemdedVideoAdapter extends BaseRecyclerAdapter<RecommendedVideoBean.DatasData> {
    private static final String TAG = "RecommemdedVideoAdapter";
    private Context mcontext;
    private JzvdStd jzVideo;
    private TextView videoname,username,comment_count;
    private CircleImageView userimg;
    private ImageView icon_comment;
    private RecommemdedVideoItemClickListener listener;
    public RecommemdedVideoAdapter(Context context){
        mcontext = context;
    }
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.rec_video_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, RecommendedVideoBean.DatasData item) {
        jzVideo = holder.findViewById(R.id.R_jz_video);
        jzVideo.setOutlineProvider(new JzViewOutlineProvider(50));
        jzVideo.setClipToOutline(true);
        Jzvd.FULLSCREEN_ORIENTATION= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        comment_count = holder.findViewById(R.id.tv_item_video_comment_count);
        videoname = holder.findViewById(R.id.R_video_name);
        username = holder.findViewById(R.id.R_user_name);
        userimg = holder.findViewById(R.id.R_user_img);
        icon_comment = holder.findViewById(R.id.iv_item_icon_comment);
        if (item!=null){
            setVideoInfo(item,position);
            onSetListClickListener(listener,position);
            userimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, PersonalActivity.class);
                    intent.putExtra(USER_ID,item.getVData().getCreator().getUserId());
                    mcontext.startActivity(intent);
                }
            });
        }
    }
    public void setVideoInfo(RecommendedVideoBean.DatasData item,int position) {
        if (item.getType()==1){
            if(item.getVData().getUrlInfo()!=null){
                jzVideo.setUp(item.getVData().getUrlInfo().getUrl(),item.getVData().getTitle());

            }
            String name = item.getVData().getTitle();
            Glide.with(mcontext).load(item.getVData().getCoverUrl()).into(jzVideo.posterImageView);

            videoname.setText(name);
            comment_count.setText(""+item.getVData().getCommentCount());
            username.setText(item.getVData().getCreator().getNickname());
            Glide.with(mcontext).load(item.getVData().getCreator().getAvatarUrl()).into(userimg);
        }
    }

    public void onSetListClickListener(RecommemdedVideoItemClickListener listener, int i) {
        icon_comment.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayListItemClick(i);
            }
        });
    }

    public interface RecommemdedVideoItemClickListener {
        void onPlayListItemClick(int position);
    }

    public void setListener(RecommemdedVideoItemClickListener listener) {
        this.listener = listener;
    }
}
