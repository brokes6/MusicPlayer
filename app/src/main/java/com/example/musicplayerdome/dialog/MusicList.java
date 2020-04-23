package com.example.musicplayerdome.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.DialogClickCallBack;
import com.example.musicplayerdome.adapter.MusicAdapter;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.resources.MusicURL;

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
    public MusicList(Context context,int height,int id){
        super(context, R.style.MyDialog);
        mContext = context;
        this.height = height;
        sid = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list);
        setMusicList();
        mlist = findViewById(R.id.music_list);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mlist.setLayoutManager(lm);
        mlist.setAdapter(musicAdapter = new MusicAdapter(dialogClickCallBacks));
        musicAdapter.loadMore(audioList);
        musicAdapter.setCurrentIDS(sid-1);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= height;
        layoutParams.dimAmount =0f;
        getWindow().getDecorView().setPadding(25, 0, 25, 25);
        getWindow().setAttributes(layoutParams);

    }


    //绑定回调
    public void setDialogClickCallBack(DialogClickCallBack callBack){
        dialogClickCallBacks = callBack;
    }
    private List<Audio> audioList = new ArrayList<>();
    private List<String> fileArr = new ArrayList<>();
    private void setMusicList() {
        MusicURL musicURL = new MusicURL();
        fileArr = musicURL.getMusicURL();
        for (int i = 0; i < fileArr.size(); i++) {
            Audio audio = new Audio();
            audio.setFileUrl(fileArr.get(i));
            audio.setId(i + 1);
            audio.setType(1);
            audio.setName("音乐" + (i + 1));
            audioList.add(audio);
        }
    }
}