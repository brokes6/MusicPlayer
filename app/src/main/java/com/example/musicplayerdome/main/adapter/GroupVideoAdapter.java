package com.example.musicplayerdome.main.adapter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.api.ApiEngine;
import com.example.musicplayerdome.api.ApiService;
import com.example.musicplayerdome.main.bean.RecommendedVideoBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.util.JzViewOutlineProvider;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 推荐视频中的 超然联盟
 */
public class GroupVideoAdapter extends BaseRecyclerAdapter<RecommendedVideoBean.DatasData> {
    private static final String TAG = "RecommemdedVideoAdapter";
    private Context mcontext;
    private JzvdStd jzVideo;
    private TextView videoname,username,comment_count;
    private CircleImageView userimg;
    private ImageView icon_comment;
    private GroupVideoItemClickListener listener;
    public GroupVideoAdapter(Context context){
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
            getUrl(item.getVData().getVid(),item);
            onSetListClickListener(listener,position);
        }
    }
    public void setVideoInfo(RecommendedVideoBean.DatasData item,VideoUrlBean Bean) {
            jzVideo.setUp(Bean.getUrls().get(0).getUrl(),item.getVData().getTitle());
            Glide.with(mcontext).load(item.getVData().getCoverUrl()).into(jzVideo.posterImageView);

            videoname.setText( item.getVData().getTitle());
            comment_count.setText(""+item.getVData().getCommentCount());
            username.setText(item.getVData().getCreator().getNickname());
            Glide.with(mcontext).load(item.getVData().getCreator().getAvatarUrl()).into(userimg);

    }

    private void getUrl(String id,RecommendedVideoBean.DatasData Datas){
        ApiService service = ApiEngine.getInstance().getApiService();
        service.getVideoData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoUrlBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoUrlBean videoUrlBean) {
                        setVideoInfo(Datas,videoUrlBean);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: 获取地址失败"+e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onSetListClickListener(GroupVideoItemClickListener listener, int i) {
        icon_comment.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayListItemClick(i);
            }
        });
    }

    public interface GroupVideoItemClickListener {
        void onPlayListItemClick(int position);
    }

    public void setListener(GroupVideoItemClickListener listener) {
        this.listener = listener;
    }
}
