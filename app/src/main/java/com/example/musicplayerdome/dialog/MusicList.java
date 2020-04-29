package com.example.musicplayerdome.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.DialogClickCallBack;
import com.example.musicplayerdome.adapter.MusicAdapter;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.resources.DomeData;

import java.util.ArrayList;
import java.util.List;

public class MusicList extends Dialog {
    private static final String TAG = "MusicList";
    Context mContext;
    private RecyclerView mlist;
    /**TextView*/
    private TextView tvTest;
    /**dialog点击回调*/
    private DialogClickCallBack dialogClickCallBacks;
    int height;
    private MusicAdapter musicAdapter;
    private int sid;
    private List<Audio> PlayList;
    public MusicList(Context context,int height,int id,List<Audio> PlayList){
        super(context, R.style.MyDialog);
        mContext = context;
        this.height = height;
        sid = id;
        this.PlayList = PlayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list);
        mlist = findViewById(R.id.music_list);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mlist.setLayoutManager(lm);
        mlist.setAdapter(musicAdapter = new MusicAdapter(dialogClickCallBacks));
        musicAdapter.loadMore(PlayList);
        musicAdapter.setCurrentIDS(sid-1);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= height;
        layoutParams.dimAmount =0f;
        dialogWindow.getDecorView().setPadding(25, 0, 25, 25);
        dialogWindow.setAttributes(layoutParams);

    }


    //绑定回调
    public void setDialogClickCallBack(DialogClickCallBack callBack){
        dialogClickCallBacks = callBack;
    }
}