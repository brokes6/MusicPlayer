package com.example.musicplayerdome.audio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MusicController;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.util.AudioPlayerConstant;


/**
 * Created by DuanJiaNing on 2017/8/19.
 * * 参考文章 http://www.jianshu.com/p/bc2f779a5400;
 */

public class MediaSessionManager {

    private static final String TAG = "MediaSessionManager";

    private static final long MEDIA_SESSION_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SEEK_TO;

    private final MusicController control;
    private final Context context;
    private MediaSessionCompat mMediaSession;

    public MediaSessionManager(Context context, MusicController control) {
        this.context = context;
        this.control = control;
        setupMediaSession();
    }

    /**
     * 初始化并激活MediaSession
     */
    private void setupMediaSession() {
        mMediaSession = new MediaSessionCompat(context, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        //设置播放监听
        mMediaSession.setCallback(callback);
        mMediaSession.setActive(true);
    }

    /**
     * 更新播放状态，播放/暂停/拖动进度条时调用
     */
    public void updatePlaybackState() {
        int state = isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        mMediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, getCurrentPosition(), 1)
                        .build());
    }

    private long getCurrentPosition() {
        try {
            return control.getPlayedDuration();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isPlaying() {
        try {
            return control.getState() == AudioPlayerConstant.ACITION_AUDIO_PLAYER_PLAY;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新正在播放的音乐信息，切换歌曲时调用
     */
    public void updateMetaData(Audio audio) {
        if (audio == null) {
            mMediaSession.setMetadata(null);
            return;
        }

        Audio info = control.getAudio();
        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, info.getName())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, info.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, info.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, info.getArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, control.getDuration());
//                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, getCoverBitmap(info))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getCount());
        }

        mMediaSession.setMetadata(metaData.build());
    }

    private long getCount() {
        try {
            return control.getPlayList() == null ? 0 : control.getPlayList().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Bitmap getCoverBitmap(Audio info) {
        if (!TextUtils.isEmpty(info.getFaceUrl())) {
            return BitmapFactory.decodeFile(info.getFaceUrl());
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
    }

    /**
     * 释放MediaSession，退出播放器时调用
     */
    public void release() {
        mMediaSession.setCallback(null);
        mMediaSession.setActive(false);
        mMediaSession.release();
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            try {
                control.resume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPause() {
            try {
                control.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSkipToNext() {
            try {
                control.next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSkipToPrevious() {
            try {
                control.pre();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStop() {
            try {
                control.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSeekTo(long pos) {
            try {
                control.seekTo((int) pos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
