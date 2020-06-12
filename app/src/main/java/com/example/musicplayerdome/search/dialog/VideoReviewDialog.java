package com.example.musicplayerdome.search.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.song.adapter.CommentAdapter;
import com.example.musicplayerdome.song.bean.MusicCommentBean;

import java.util.ArrayList;
import java.util.List;

public class VideoReviewDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = "VideoReviewDialog";
    private Activity mContext;
    private Context mcontext;
    private View view;
    private List<MusicCommentBean.CommentsBean> hotCommentList = new ArrayList<>();
    private List<MusicCommentBean> CommentList = new ArrayList<>();
    private CommentAdapter hotAdapter,newAdapter;
    private RecyclerView recyclerView,rv_new_comment;


    public VideoReviewDialog(@NonNull Context context, List<MusicCommentBean> Comments) {
        super(context, R.style.my_dialog);
        this.mcontext = context;
        this.CommentList.addAll(Comments);
        init();
    }

    private void init() {
        mContext = (Activity) mcontext;
        view = mContext.getLayoutInflater().inflate(R.layout.videoreview_dialog, null);
        recyclerView = view.findViewById(R.id.v_hot_comment);
        rv_new_comment = view.findViewById(R.id.rv_new_comment);

        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        lp.height = (int)(display.getHeight()*0.8);
        //设置弹窗宽度
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //为弹窗绑定效果
        dialogWindow.setAttributes(lp);

        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        rv_new_comment.setLayoutManager(new LinearLayoutManager(mcontext));
        hotAdapter = new CommentAdapter(mcontext);
        newAdapter = new CommentAdapter(mcontext);
        recyclerView.setAdapter(hotAdapter);
        rv_new_comment.setAdapter(newAdapter);
        hotAdapter.loadMore(CommentList.get(0).getHotComments());
        newAdapter.loadMore(CommentList.get(0).getComments());

    }

    @Override
    public void onClick(View v) {

    }

    private float startY;
    private float moveY = 0;
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY() - startY;
                view.scrollBy(0, -(int) moveY);
                startY = ev.getY();
                if (view.getScrollY() > 0) {
                    view.scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (view.getScrollY() < -this.getWindow().getAttributes().height / 4 && moveY > 0) {
                    this.dismiss();

                }
                view.scrollTo(0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

}
