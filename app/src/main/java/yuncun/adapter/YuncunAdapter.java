package yuncun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.rewrite.RoundImageView;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import yuncun.bean.YuncunReviewBean;

public class YuncunAdapter extends BaseRecyclerAdapter<YuncunReviewBean.UserData> {
    private static final String TAG = "YuncunAdapter";
    private Context mcontext;
    private YuncunReviewBean.UserData beans;
    RoundCornerImageView yuncun_img;
    RoundImageView user_img;
    TextView yuncun_wz,user_name;
    public YuncunAdapter(Context context){
        mcontext = context;
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
            beans = item;
            setData(position, (YuncunReviewBean.UserData.simpleUserInfoBean) item.getSimpleUserInfo());
        }
    }

    public void setData(int position, YuncunReviewBean.UserData.simpleUserInfoBean bean) {
        if (bean != null) {
            yuncun_wz.setText(beans.getContent());
            user_name.setText(bean.getNickname());
            Glide.with(mcontext).load(bean.getAvatar()).transition(new DrawableTransitionOptions().crossFade()).into(user_img);
            Glide.with(mcontext).load(beans.getSimpleResourceInfo().getSongCoverUrl()).transition(new DrawableTransitionOptions().crossFade()).into(yuncun_img);
            //需要Item高度不同才能出现瀑布流的效果，此处简单粗暴地设置一下高度
            if (position % 2 == 0) {
                yuncun_img.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 760));
            } else {
                yuncun_img.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 860));
            }
        }
    }

}
