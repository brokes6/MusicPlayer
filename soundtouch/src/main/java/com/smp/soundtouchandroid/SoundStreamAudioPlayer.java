package com.smp.soundtouchandroid;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;

public class SoundStreamAudioPlayer extends SoundStreamRunnable {
    private static final int BUFFER_SIZE_TRACK = 32768;

    private AudioTrackAudioSink track;

//    public SoundStreamAudioPlayer(int id, String fileName, float tempo, float pitchSemi, MediaCallBack callBack) throws IOException {
//    }

    public SoundStreamAudioPlayer(int id, String path, float tempo, float pitchSemi, MediaCallBack callBack, PreparedListener preparedListener) throws IOException {
        super(id, path, tempo, pitchSemi, callBack, preparedListener);
    }

    public int getSessionId() {
        return track.getAudioSessionId();
    }

    public long getAudioTrackBufferSize() {
        synchronized (sinkLock) {
            long playbackHead = track.getPlaybackHeadPosition() & 0xffffffffL;
            return bytesWritten - playbackHead * DEFAULT_BYTES_PER_SAMPLE * getChannels();
        }
    }

    public void setVolume(float left, float right) {
        synchronized (sinkLock) {
            track.setStereoVolume(left, right);
        }
    }

    public boolean isInitialized() {
        if (track == null) {
            return false;
        }
        return track.getState() == AudioTrack.STATE_INITIALIZED;
    }

    public void seekTo(double percentage, boolean shouldFlush) // 0.0 - 1.0
    {
        long timeInUs = (long) (decoder.getDuration() * percentage);
        seekTo(timeInUs, shouldFlush);
    }

    public void seekTo(long timeInUs, boolean shouldFlush) {
        if (decoder == null) return;
        if (soundTouch == null) return;
        if (track == null) return;
        if (!isInitialized()) return;
        if (timeInUs < 0 || timeInUs > decoder.getDuration())
            throw new SoundStreamRuntimeException("" + timeInUs
                    + " Not a valid seek time.");

        if (shouldFlush) {
            this.pause();
            synchronized (sinkLock) {
                track.flush();
                bytesWritten = 0;
            }
            soundTouch.clearBuffer();
        }
        synchronized (decodeLock) {
            if (decoder == null) return;
            decoder.seek(timeInUs, shouldFlush);
        }
    }

    private volatile boolean isPlaying;
    private volatile boolean isPause;

    public void onPrepare() {
        if (mediaCallBack != null) {
            mediaCallBack.onPrepare();
        }
    }

    @Override
    protected void onPrepared(long duration) {
        Log.d("debug", "soundPlayer:onPrepared");
        if (mediaCallBack != null) {
            mediaCallBack.onPrepared(duration);
        }
    }

    @Override
    public void onStart() {
        synchronized (sinkLock) {
            if (isInitialized()) {
                track.play();
                isPause = false;
                isPlaying = true;
                if (mediaCallBack != null) {
                    mediaCallBack.onPlay();
                }
            }
        }
    }

    public boolean isPause() {
        Log.d("debug", "  isPause=" + isPause);
        return isInitialized() && track.getPlayState() == AudioTrack.PLAYSTATE_PAUSED;
    }

    @Override
    public void onPause() {
        synchronized (sinkLock) {
            if (isInitialized()) {
                track.pause();
                isPause = true;
                isPlaying = false;
                if (mediaCallBack != null) {
                    mediaCallBack.onPause();
                }
            }
        }
    }

    @Override
    public void onStop() {
        isPause = false;
        isPlaying = false;
        if (mediaCallBack != null) {
            mediaCallBack.onStop();
        }
    }

    @Override
    protected void onComplete() {
        isPlaying = false;
        if (mediaCallBack != null) {
            mediaCallBack.onComplete();
        }
    }

    @Override
    protected void onError() {
        if (mediaCallBack != null) {
            mediaCallBack.onError();
        }
    }

    @Override
    public void seekTo(long timeInUs) {
        seekTo(timeInUs, false);
    }

    private void initAudioTrack(int id, float tempo, float pitchSemi)
            throws IOException {
        int channelFormat;

        if (channels == 1)
            channelFormat = AudioFormat.CHANNEL_OUT_MONO;
        else if (channels == 2)
            channelFormat = AudioFormat.CHANNEL_OUT_STEREO;
        else
            throw new SoundStreamRuntimeException(
                    "Valid channel count is 1 or 2");

        track = new AudioTrackAudioSink(AudioManager.STREAM_MUSIC,
                samplingRate, channelFormat, AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE_TRACK, AudioTrack.MODE_STREAM);
    }

    @Override
    protected AudioSink initAudioSink() throws IOException {
        initAudioTrack(getSoundTouchTrackId(), getTempo(), getPitchSemi());
        return track;
    }

    public void release() {
        super.release();
//        if (track != null) {
//            track.close();
//        }
        isPause = false;
        isPlaying = false;

        if (mediaCallBack != null) {
            mediaCallBack.onStop();
        }
    }

    @Override
    public boolean isPlaying() {
        Log.d("debug", "子类：isPlaying");
        return isInitialized() && track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING;
    }

    MediaCallBack mediaCallBack;

    @Override
    public void setMediaCallBack(MediaCallBack mediaCallBack) {
        this.mediaCallBack = mediaCallBack;
    }
}
