package com.example.musicplayerdome.yuncun.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.personal.view.PersonalActivity;
import com.example.musicplayerdome.rewrite.RoundImageView;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import yuncun.bean.YuncunReviewBean;

import static com.example.musicplayerdome.personal.view.PersonalActivity.USER_ID;

/**
 * 云村瀑布流适配器
 * YuncunReviewBean.UserData
 */
public class YuncunAdapter extends BaseRecyclerAdapter<YuncunReviewBean.UserData> {
    private static final String TAG = "YuncunAdapter";
    private Context mcontext;
    RoundCornerImageView yuncun_img;
    RoundImageView user_img;
    TextView yuncun_wz,user_name;
    private int Pheight;
    private OnYuncunListItemClickListener listener;

    public YuncunAdapter(Context context,int Pheight){
        mcontext = context;
        this.Pheight = Pheight;
    }
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.activity_yuncun;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, YuncunReviewBean.UserData item) {
        yuncun_img = holder.findViewById(R.id.yuncun_img);
        user_img = holder.findViewById(R.id.user_img);
        user_name = holder.findViewById(R.id.tv_playlist_name);
        yuncun_wz = holder.findViewById(R.id.yuncun_wz);
        if (item!=null){
            setData(position,item);
            onSetListClickListener(listener,position);
            user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, PersonalActivity.class);
                    intent.putExtra(USER_ID,item.getSimpleUserInfo().getUserId());
                    mcontext.startActivity(intent);
                }
            });
        }
    }

    public void setData(int position, YuncunReviewBean.UserData bean) {
        if (bean != null) {
            yuncun_wz.setText(bean.getContent());
            user_name.setText(bean.getSimpleUserInfo().getNickname());
            Glide.with(mcontext).load(bean.getSimpleUserInfo().getAvatar()).transition(new DrawableTransitionOptions().crossFade()).into(user_img);
            Glide.with(mcontext).load(bean.getSimpleResourceInfo().getSongCoverUrl()).transition(new DrawableTransitionOptions().crossFade()).into(yuncun_img);
            //需要Item高度不同才能出现瀑布流的效果，此处简单粗暴地设置一下高度
            if (position % 2 == 0) {
                yuncun_img.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (Pheight/2)-100));
            } else {
                yuncun_img.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (Pheight/2)));
            }
        }
    }

    public void onSetListClickListener(OnYuncunListItemClickListener listener, int i) {
        yuncun_img.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayListItemClick(i);
            }
        });
    }

    public interface OnYuncunListItemClickListener {
        void onPlayListItemClick(int position);

        void onSmartPlayClick(int position);
    }

    public void setListener(OnYuncunListItemClickListener listener) {
        this.listener = listener;
    }

}
