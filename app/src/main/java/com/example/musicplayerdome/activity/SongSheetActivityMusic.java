package com.example.musicplayerdome.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.OnItemListenter;
import com.example.musicplayerdome.adapter.MainMusicAdapter;
import com.example.musicplayerdome.databinding.SongSheetBinding;
import com.example.musicplayerdome.base.MusicBaseActivity;
import com.example.musicplayerdome.resources.DomeData;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.gyf.immersionbar.ImmersionBar;

public class SongSheetActivityMusic extends MusicBaseActivity implements View.OnClickListener{
    SongSheetBinding binding;
    private static final String TAG = "SongSheetActivity";
    private MainMusicAdapter mainMusicAdapter;
    private Intent intent;
    private SomeBroadcastReceiver bReceiver;
    private int Sid;
    private boolean go = false;
    /**
     * 上一首 按钮点击 ID
     */
    private final static int BUTTON_PREV_ID = 1;
    /**
     * 播放/暂停 按钮点击 ID
     */
    private final static int BUTTON_PALY_ID = 2;
    /**
     * 下一首 按钮点击 ID
     */
    private final static int BUTTON_NEXT_ID = 3;
    private final static String INTENT_BUTTONID_TAG = "ButtonId";
    private final static String ACTION_BUTTON = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClick";
    private final static String ACTIONS = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClickS";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.A3A3)
                .init();
        binding = DataBindingUtil.setContentView(this,R.layout.song_sheet);
        initView();
        initBroadcastReceiver();
    }
    private void initView(){
        LinearLayoutManager lm = new LinearLayoutManager(SongSheetActivityMusic.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(lm);
        binding.recyclerView.setAdapter(mainMusicAdapter = new MainMusicAdapter(SongSheetActivityMusic.this));
        /**
         * 回调监听也行，但没必要，只需要在加个参数用来判断就行了
         * 通过抽象类来回调监听
         * 这边才是真正的方法
         */
        mainMusicAdapter.setOnItemClickListener(new OnItemListenter() {
            @Override
            public void onItemClick(View view, int postionid) {
                Intent intent = new Intent(SongSheetActivityMusic.this, MusicActivityMusic.class);
                intent.putExtra ("id",postionid);
                intent.putExtra ("key",1);
                startActivity(intent);
            }
        });
        mainMusicAdapter.loadMore(DomeData.getAudioMusic());

        binding.Pback.setOnClickListener(this);
        binding.btnCustomPlay.setOnClickListener(this);
        binding.btnCustomNext.setOnClickListener(this);
        binding.btnCustomPrev.setOnClickListener(this);
        binding.PlaybackController.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Pback:
                finish();
                break;
            case R.id.btn_custom_prev:
                //上一首
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
                sendBroadcast(intent);
                break;
            case R.id.btn_custom_play:
                //播放
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
                sendBroadcast(intent);
                break;
            case R.id.btn_custom_next:
                //下一首
                intent = new Intent();
                intent.setAction(ACTION_BUTTON);
                intent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
                sendBroadcast(intent);
                break;
            case R.id.Playback_controller:
                ActivityUtils.startActivity(MusicActivityMusic.class);
                break;
        }
    }

    private void initBroadcastReceiver(){
        bReceiver = new SomeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTIONS);
        registerReceiver(bReceiver, intentFilter);
    }

    /**
     * 显示音频标题
     */
    private void addAudioTitle(String name,String author,String img) {
        if (name == null) return;
        //设置音乐名称
        binding.tvCustomSongSinger.setText(name);
        MyUtil.setText(binding.tvCustomSongAuthor,author);
        binding.Mimg.setImageURL(img);
    }
    private void setImg(ImageView imageView, int imgRes) {
        if (imageView == null) return;
        imageView.setImageResource(imgRes);
    }
    
    @Override
    protected void onResume() {
        Log.e(TAG, "onResume: 运行？"+go);
        super.onResume();
        if (go == true)return;
        go = (boolean) SharedPreferencesUtil.getData("go",false);
        Log.e(TAG, "获取成功"+go);
        if (go ==true){
            String name = (String)SharedPreferencesUtil.getData("Mname","");
            String author = (String)SharedPreferencesUtil.getData("Mauthor","");
            String img = (String)SharedPreferencesUtil.getData("Mimg","");
            addAudioTitle(name,author,img);
            binding.PlaybackController.setVisibility(View.VISIBLE);
        }
    }
    public class SomeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTIONS)) {
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                Sid = intent.getIntExtra("sid",0);
                switch (buttonId) {
                    case 1:
                    case 4:
                    case 5:
                        String name = (String)SharedPreferencesUtil.getData("Mname","");
                        String author = (String)SharedPreferencesUtil.getData("Mauthor","");
                        String img = (String)SharedPreferencesUtil.getData("Mimg","");
                        addAudioTitle(name,author,img);
                        break;
                    case 2://播放或暂停
                        setImg(binding.btnCustomPlay,R.mipmap.audio_state_play);
                        break;
                    case 3:
                        setImg(binding.btnCustomPlay,R.mipmap.audio_state_pause);
                        break;
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: 歌单页面广播已注销");
        unregisterReceiver(bReceiver);
        super.onDestroy();
    }
}
