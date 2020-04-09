package com.smp.soundtouchandroid;

import android.media.MediaExtractor;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public abstract class SoundStreamRunnable implements Runnable {
    private static final long NOT_SET = Long.MIN_VALUE;
    protected static final int DEFAULT_BYTES_PER_SAMPLE = 2;
    private static final long MAX_OUTPUT_BUFFER_SIZE = 1024;

    protected Object pauseLock;
    protected Object sinkLock;
    protected Object decodeLock;

    protected volatile long bytesWritten;

    protected SoundTouch soundTouch;
    protected volatile AudioDecoder decoder;

    protected Handler handler;

    private String fileName;
    private boolean bypassSoundTouch;

    private volatile long loopStart = NOT_SET;
    private volatile long loopEnd = NOT_SET;
    private volatile AudioSink audioSink;
    private volatile OnProgressChangedListener progressListener;

    private volatile boolean paused;
    protected volatile boolean finished;

    protected int channels;
    protected int samplingRate;

    public void release() {
        if (!isPaused()) {
            stop();
        }
        if (soundTouch != null) {
            soundTouch.clearBuffer();
            soundTouch.finish();
            soundTouch = null;
        }
        if (decoder != null) {
            decoder.close(isPlaying());
            decoder = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (progressListener != null) {
            progressListener = null;
        }
//        pauseLock = null;
//        sinkLock = null;
//        decodeLock = null;
        try {
            if (audioSink != null) {
                audioSink.close(isPlaying());
                audioSink = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            soundTouch = null;
            audioSink = null;
        }
    }

    protected abstract boolean isPlaying();

    protected abstract AudioSink initAudioSink() throws IOException;

    private void initSoundTouch(int id, float tempo, float pitchSemi) {
        soundTouch = new SoundTouch(id, channels, samplingRate,
                DEFAULT_BYTES_PER_SAMPLE, tempo, pitchSemi);
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public int getChannels() {
        return soundTouch.getChannels();
    }

    public long getDuration() {
        if (decoder != null)
            return decoder.getDuration();
        else
            return NOT_SET;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getPlayedDuration() {
        if (decodeLock == null) return NOT_SET;
        synchronized (decodeLock) {
            if (decoder != null)
                return decoder.getPlayedDuration();
            else
                return NOT_SET;
        }
    }

    public float getPitchSemi() {
        return soundTouch.getPitchSemi();
    }

    public long getPlaybackLimit() {
        return loopEnd;
    }

    public int getSamplingRate() {
        return soundTouch.getSamplingRate();
    }

    public long getSoundTouchBufferSize() {
        return soundTouch.getOutputBufferSize();
    }

    protected int getSoundTouchTrackId() {
        return soundTouch.getTrackId();
    }

    public float getTempo() {
        return soundTouch.getTempo();
    }

    public float getRate() {
        return soundTouch.getRate();
    }

    public long getLoopEnd() {
        return loopEnd;
    }

    public long getLoopStart() {
        return loopStart;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isLooping() {
        return loopStart != NOT_SET && loopEnd != NOT_SET;
    }

    public boolean isPaused() {
        Log.d("debug", " paused=" + paused);
        return paused;
    }

    public void setBypassSoundTouch(boolean bypassSoundTouch) {
        this.bypassSoundTouch = bypassSoundTouch;
    }

    public void setLoopEnd(long loopEnd) {
        long pd = decoder.getPlayedDuration();
        if (loopStart != NOT_SET && pd <= loopStart)
            throw new SoundStreamRuntimeException(
                    "Invalid Loop Time, loop start must be < loop end");
        this.loopEnd = loopEnd;
    }

    public void setLoopStart(long loopStart) {
        long pd = decoder.getPlayedDuration();
        if (loopEnd != NOT_SET && pd >= loopEnd)
            throw new SoundStreamRuntimeException(
                    "Invalid Loop Time, loop start must be < loop end");
        this.loopStart = loopStart;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setPitchSemi(float pitchSemi) {
        if (soundTouch == null) return;
        soundTouch.setPitchSemi(pitchSemi);
    }

    public void setTempo(float tempo) {
        if (soundTouch == null) return;
        soundTouch.setTempo(tempo);
    }

    public void setTempoChange(float tempoChange) {
        soundTouch.setTempoChange(tempoChange);
    }

    public void setRate(float rate) {
        if (soundTouch == null) return;
        soundTouch.setRate(rate);
    }

    public void setRateChange(float rate) {
        if (soundTouch == null) return;
        soundTouch.setRateChange(rate);
    }


    public void setChannels(int channels) {
        if (soundTouch == null) return;
        soundTouch.setChannels(channels);
    }

    public SoundStreamRunnable(final int id, final String fileName, final float tempo, final float pitchSemi, final MediaCallBack callBack, final PreparedListener preparedListener) throws IOException {
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    init(id, fileName, tempo, pitchSemi, callBack, preparedListener);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError();
                }
            }
        }).start();
    }

    public void setData(final String url, final PreparedListener preparedListener) {
        if (handler == null) {
            handler = new Handler();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean b = Thread.currentThread() == Looper.getMainLooper().getThread();
                    Log.d("debug", "是否在主线程：" + b);
                    onPrepare();
                    if (decoder == null) {
                        initDecoder(url);
                    } else {
                        decoder.reset();
                        decoder.setPath(url);
                        channels = decoder.getChannels();
                        samplingRate = decoder.getSamplingRate();
                    }
                    if (soundTouch == null) {
                        initSoundTouch(0, getTempo(), getPitchSemi());
                    }
                    if (audioSink == null) {
                        audioSink = initAudioSink();
                    }
                    setFileName(fileName);
                    if (pauseLock == null) {
                        pauseLock = new Object();
                    }
                    if (sinkLock == null) {
                        sinkLock = new Object();
                    }
                    if (decodeLock == null) {
                        decodeLock = new Object();
                    }
                    paused = true;
                    finished = false;
                    onPrepared(getDuration());
                    if (preparedListener != null) {
                        preparedListener.onPrepared(getDuration());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onError();
                }
            }
        }).start();
    }

    protected abstract void onError();

    private void init(int id, String fileName, float tempo, float pitchSemi, MediaCallBack callBack, PreparedListener preparedListener) throws IOException {
        boolean b = Thread.currentThread() == Looper.getMainLooper().getThread();
        Log.d("debug", "是否在主线程：" + b);
        setMediaCallBack(callBack);
        onPrepare();
        initDecoder(fileName);
        initSoundTouch(id, tempo, pitchSemi);
        audioSink = initAudioSink();
        this.fileName = fileName;
        pauseLock = new Object();
        sinkLock = new Object();
        decodeLock = new Object();

        paused = true;
        finished = false;
        onPrepared(getDuration());
        if (preparedListener != null) {
            preparedListener.onPrepared(getDuration());
        }
    }

    public SoundStreamRunnable(int id, File file, float tempo, float pitchSemi) throws IOException {
        this(id, file.getAbsolutePath(), tempo, pitchSemi, null, null);
    }

    private void initDecoder(String fileName) throws IOException {
        if (Build.VERSION.SDK_INT >= 16) {
            decoder = new MediaCodecAudioDecoder(fileName);
        } else {
            throw new SoundStreamRuntimeException(
                    "Only API level >= 16 supported.");
        }

        channels = decoder.getChannels();
        samplingRate = decoder.getSamplingRate();
    }

    @Override
    public void run() {
        if (soundTouch == null || decoder == null || audioSink == null) return;
        //does it do anything?
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        try {
            while (!finished) {
                processFile();
                paused = true;
                if (progressListener != null && !finished && soundTouch != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (soundTouch != null) {
                                synchronized (soundTouch) {
                                    if (soundTouch != null) {
                                        progressListener.onTrackEnd(soundTouch.getTrackId());
                                        onComplete();
                                    }
                                }
                            }
                        }
                    });
                }
                if (decodeLock != null) {
                    synchronized (decodeLock) {
                        if (decoder !=null){
                            decoder.resetEOS();
                        }
                    }
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            if (progressListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressListener.onExceptionThrown(Log.getStackTraceString(e));
                    }
                });
        } catch (final SoundStreamDecoderException e) {
            if (progressListener != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressListener.onExceptionThrown(Log.getStackTraceString(e));
                    }
                });
        } finally {
            finished = true;
            if (soundTouch != null) {
                soundTouch.clearBuffer();
            }
            if (sinkLock != null) {
                synchronized (sinkLock) {
                    try {
                        if (audioSink != null) {
                            audioSink.close(false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    audioSink = null;
                }
            }
            if (decodeLock != null) {
                synchronized (decodeLock) {
                    if (decoder != null) {
                        decoder.close(false);
                        decoder = null;
                    }
                }
            }
        }
    }

    public void clearLoopPoints() {
        loopStart = NOT_SET;
        loopEnd = NOT_SET;
    }

    protected abstract void onPrepare();

    protected abstract void onPrepared(long duration);

    protected abstract void onStart();

    protected abstract void onPause();

    protected abstract void onStop();

    protected abstract void onComplete();

    public abstract void setMediaCallBack(MediaCallBack mediaCallBack);

    protected void seekTo(long timeInUs) {
    }

    public void start() {
        if (pauseLock == null) return;
        synchronized (pauseLock) {
            onStart();
            paused = false;
            finished = false;
            pauseLock.notifyAll();
        }
    }

    public void pause() {
        if (pauseLock == null) return;
        synchronized (pauseLock) {
            onPause();
            paused = true;
        }
    }

    public void stop() {
        if (paused) {
            if (pauseLock != null) {
                synchronized (pauseLock) {
                    onStop();
                    paused = false;
                    pauseLock.notifyAll();
                }
            }
        }
        finished = true;
    }

    private void pauseWait() {
        if (pauseLock != null) {
            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private void processFile() throws IOException, SoundStreamDecoderException {
        int bytesReceived = 0;
        do {
            pauseWait();
            if (finished)
                break;

            if (isLooping() && decoder.getPlayedDuration() >= loopEnd) {
                //Log.d("DECODE", "TEST TIME: " + String.valueOf(decoder.getTestTime()));
                //Log.d("DECODE", "PLAYED: " +  String.valueOf(decoder.getPlayedDuration()));
                seekTo(loopStart);
            }

            if (soundTouch != null && soundTouch.getOutputBufferSize() <= MAX_OUTPUT_BUFFER_SIZE) {
                boolean newBytes;
                if (decodeLock != null) {
                    synchronized (decodeLock) {
                        try {
                            newBytes = decoder.decodeChunk();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new SoundStreamDecoderException(
                                    "Buggy google decoder");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            throw new SoundStreamDecoderException(
                                    "Buggy google decoder");
                        }
                    }
                    if (newBytes) {
                        sendProgressUpdate();
                        processChunk(decoder.getLastChunk(), true);
                    }
                }
            } else {
                if (decoder != null) {
                    // avoiding an extra allocation.
                    processChunk(decoder.getLastChunk(), false);
                }
            }
        }
        while (decoder != null && !decoder.sawOutputEOS() && getPlayedDuration() < getDuration());
        if (soundTouch != null) {
            soundTouch.finish();
        }
        if (!bypassSoundTouch) {
            do {
                if (finished)
                    break;
                bytesReceived = processChunk(null, false);
            }
            while (bytesReceived > 0);
        }
    }

    private void sendProgressUpdate() {
        if (progressListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!finished) {
                        long pd = decoder.getPlayedDuration();
                        long d = decoder.getDuration();
                        double cp = pd == 0 ? 0 : (double) pd / (double) d;
                        progressListener.onProgressChanged(
                                soundTouch.getTrackId(), cp, pd);
                    }
                }
            });
        }
    }

    private int processChunk(final byte[] input, boolean putBytes)
            throws IOException {
        int bytesReceived = 0;

        if (input != null) {
            if (bypassSoundTouch) {
                bytesReceived = input.length;
            } else {
                if (putBytes)
                    soundTouch.putBytes(input);
                // Log.d("bytes", String.valueOf(input.length));
                bytesReceived = soundTouch.getBytes(input);
            }
            if (bytesReceived > 0) {
                if (sinkLock != null) {
                    synchronized (sinkLock) {
                        bytesWritten += audioSink.write(input, 0, bytesReceived);
                    }
                }
            }
        }
        return bytesReceived;
    }

}
