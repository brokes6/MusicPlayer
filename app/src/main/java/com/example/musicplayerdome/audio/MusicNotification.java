package com.example.musicplayerdome.audio;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.activity.MusicActivity;
import com.example.musicplayerdome.util.BitMapUtil;


/**
 * Created by ding on 2016/12/3.
 * 状态栏的音频播放控制器
 */
public class MusicNotification {

    /**
     * 音频播放控制 的  Notification
     * 动态的显示后台的AudioplayService的前台展示
     */
    private static MusicNotification notifyInstance = null;

    // 通知id
    private final int FLAG = PendingIntent.FLAG_UPDATE_CURRENT;
    private final int NOTIFICATION_ID = 0x1213;
    // 通知
    private Notification musicNotifi = null;
    // 管理通知
    private NotificationManager manager = null;
    // 界面实现
    private Notification.Builder builder = null;
    // 上下文
    private Context context;
    MusicController musicController;
    // 布局
    private RemoteViews remoteViews;
    private Intent playsIntent = null;
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
    /**
     * 关闭 按钮点击 ID
     */
    private final static int BUTTON_COSE_ID = 4;

    /**
     * 跳转播放界面 按钮点击 ID
     */
    private final static int BUTTON_JUMP_ID = 5;
    private static final String TAG = "MusicNotification";
    private final static String ACTION_BUTTON = "xinkunic.aifatushu.customviews.MusicNotification.ButtonClick";
    private final static String INTENT_BUTTONID_TAG = "ButtonId";
    private ImageView imageView;
    public ButtonBroadcastReceiver bReceiver;


    private MusicNotification(Context context, MusicController musicController) {
        this.context = context;
        this.musicController = musicController;
        // 初始化操作
        //初始化通知栏样式
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_custom_button);
        //Notification.Builder builder = null;
        builder = new Notification.Builder(context);
        // 初始化控制的Intent
        playsIntent = new Intent();
        playsIntent.setAction(ACTION_BUTTON);
        imageView = new ImageView(context);
        onCreateMusicNotifi(context);
    }

    /**
     * 返回通知实列
     * 每次在重新运行时，都将MusicNotification设置为空，代表着每次都重新构造MusicNotification
     * @return
     */
    public static MusicNotification getMusicNotification(Context context, MusicController musicController) {
        if (notifyInstance == null) {
            synchronized (MusicNotification.class) {
                if (notifyInstance == null) {
                    notifyInstance = new MusicNotification(context, musicController);
                }
            }
        }
        return notifyInstance;
    }

    /**
     * 创建通知
     * 初始化通知
     */
    @SuppressLint("NewApi")
    public void onCreateMusicNotifi(Context context) {
        // 设置点击事件
        // 管理通知
        // private NotificationManager manager = null;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager = NotificationManagerCompat.from(context);
        registerClick();
        Intent intent1 = new Intent(context, MusicActivity.class);
        intent1.putExtra("isHead", true);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        //// 布局
        //private RemoteViews remoteViews;
        builder.setContent(remoteViews).setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        //Android 8.0以后,需要对Notification进行通知管理（这里应该加入判断sdk是否大于24）
            NotificationChannel channel = new NotificationChannel(context.getPackageName(), "音乐播放器", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            manager.createNotificationChannel(channel);
            builder.setChannelId(context.getPackageName());
        musicNotifi = builder.build();
        Log.e(TAG, "(Notification)通知栏创建完毕");
        initButtonReceiver(context);

    }

    private void registerClick() {
        // 1.注册播放或暂停点击事件
        playsIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
        PendingIntent pplay = PendingIntent.getBroadcast(context, 1, playsIntent, FLAG);
        remoteViews.setOnClickPendingIntent(R.id.btn_custom_play, pplay);

        // 2.注册上一首点击事件
        playsIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
        PendingIntent playPre = PendingIntent.getBroadcast(context, 3, playsIntent, FLAG);
        remoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, playPre);

        // 3.注册下一首点击事件
        playsIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
        PendingIntent pnext = PendingIntent.getBroadcast(context, 2, playsIntent, FLAG);
        remoteViews.setOnClickPendingIntent(R.id.btn_custom_next, pnext);

    }

    private void initButtonReceiver(Context context) {
        Log.e(TAG, "广播创建（为了监听通知栏按钮发出的广播）");
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        context.registerReceiver(bReceiver, intentFilter);
    }


    /**
     * 取消通知栏
     */
    public void onCancelMusicNotifi() {
        if (manager == null) return;
        manager.cancel(NOTIFICATION_ID);
        context.unregisterReceiver(bReceiver);
        if (musicController !=null){
            musicController.stop();
            musicController=null;
        }
        isShowing = false;
    }


    private boolean isPlaying = false;
    /**
     * 更新通知
     */
    public void upDataNotifacation(boolean needRefrush, final String name,String faceUrl,boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (remoteViews == null) {
            return;
        }
        if (needRefrush) {
            remoteViews.setTextViewText(R.id.tv_custom_song_singer, name);
        }
        if (faceUrl==null){
            remoteViews.setImageViewResource(R.id.Music_logo,R.mipmap.logo);
        }else{
            BitMapUtil bitMapUtil = new BitMapUtil();
            remoteViews.setImageViewBitmap(R.id.Music_logo,bitMapUtil.getBitmaps(faceUrl));
        }
        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.btn_custom_play, android.R.drawable.ic_media_pause);
        } else {
            remoteViews.setImageViewResource(R.id.btn_custom_play, android.R.drawable.ic_media_play);
        }
        show();
    }
        //获取图片主要颜色方法（暂时弃用）
//        public void getPalette(Bitmap bitmap){
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                public void onGenerated (Palette palette){
//                    if (palette != null) {
//                        Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力的
//                        if (vibrant !=null){
//                            remoteViews.setTextColor(R.id.tv_custom_song_singer,vibrant.getRgb());
//                            Log.d(TAG, "onGenerated: -----颜色为"+vibrant.getRgb());
//                        }
//                    }
//                }
//            });
//        }

    boolean isShowing = false;

    private void show() {
        manager.notify(NOTIFICATION_ID, musicNotifi);
        isShowing = true;
    }


    public class ButtonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PREV_ID://上一首
                        if (musicController != null) {
                            musicController.pre();
                        }
                        break;
                    case BUTTON_PALY_ID://播放或暂停
                        if (musicController != null) {
                            if (musicController.isPlaying()) {
                                musicController.pause();
                            } else {
                                musicController.play();
                            }
                        }
                        break;

                    case BUTTON_NEXT_ID://下一首
                        if (musicController != null) {
                            musicController.next();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
