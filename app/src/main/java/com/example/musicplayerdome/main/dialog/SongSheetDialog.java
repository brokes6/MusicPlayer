package com.example.musicplayerdome.main.dialog;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.example.musicplayerdome.R;

/**
 * 歌单详情dialog，用来展示歌单详情文字和图片
 */
public class SongSheetDialog extends Dialog implements View.OnClickListener{
    private Context mcontext;
    private Activity Context;
    private View view;
    private RelativeLayout Smain;
    private ImageView Sbackground,Sback;
    private RoundCornerImageView Scover;
    private TextView Stitle,Sintroduce;
    private String title,introduce,cover;
    private Drawable Sbg;
    private ObjectAnimator coverAlphaAnimator;
    public SongSheetDialog(@NonNull Context context, String title, String introduce, String cover, Drawable Sbg) {
        super(context, R.style.dialog_FadeIn);
        mcontext = context;
        this.title = title;
        this.introduce = introduce;
        this.cover = cover;
        this.Sbg = Sbg;
        init(mcontext);
    }

    private void init(Context context){
        Context = (Activity) context;
        //为弹窗绑定视图
        view = Context.getLayoutInflater().inflate(R.layout.dialog_songsheet, null);
        Smain = view.findViewById(R.id.S_main);
        Sbackground = view.findViewById(R.id.S_background);
        Sback = view.findViewById(R.id.S_back);
        Scover = view.findViewById(R.id.S_cover);
        Stitle = view.findViewById(R.id.S_title);
        Sintroduce = view.findViewById(R.id.S_introduce);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = Context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        //设置弹窗宽度
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //为弹窗绑定效果
        dialogWindow.setAttributes(lp);
        Sbackground.setBackground(Sbg);
        getAlphaAnimatorCover().start();
        Stitle.setText(title);
        Sintroduce.setText(introduce);
        Glide.with(context).load(cover).into(Scover);
        Sback.setOnClickListener(this);
        Smain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.S_back:
            case R.id.S_main:
                dismiss();
                break;
        }
    }
    private ObjectAnimator getAlphaAnimatorCover() {
        if (coverAlphaAnimator == null) {
            coverAlphaAnimator = ObjectAnimator.ofFloat(Sbackground, "alpha", 0f, 1.0f);
            coverAlphaAnimator.setDuration(1000);
            coverAlphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return coverAlphaAnimator;
    }

}
