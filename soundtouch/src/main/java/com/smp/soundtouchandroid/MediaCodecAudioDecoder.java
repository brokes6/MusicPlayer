package com.smp.soundtouchandroid;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Locale;

@SuppressLint("NewApi")
public class MediaCodecAudioDecoder implements AudioDecoder {
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    private static final long TIMEOUT_US = 0;

    private long durationUs; // track duration in us
    private volatile long currentTimeUs; // total played duration thus far
    private BufferInfo info;
    private MediaCodec codec;
    private MediaExtractor extractor;
    private MediaFormat format;
    private ByteBuffer[] codecInputBuffers;
    private ByteBuffer[] codecOutputBuffers;
    private byte[] lastChunk;
    private volatile boolean sawOutputEOS;

    public int getChannels() throws IOException {
        if (format.containsKey(MediaFormat.KEY_CHANNEL_COUNT))
            return format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        throw new IOException("Not a valid audio file");
    }

    public long getDuration() {
        return durationUs;
    }

    @Override
    public long getPlayedDuration() {
        return currentTimeUs;
    }

    public int getSamplingRate() throws IOException {
        if (format.containsKey(MediaFormat.KEY_SAMPLE_RATE))
            return format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        throw new IOException("Not a valid audio file");
    }

    FileOutputStream fis = null;

    @SuppressLint("NewApi")
    public MediaCodecAudioDecoder(String fullPath) throws IOException {
        Log.d("debug", "  fullPath=" + fullPath);
        initDecoder(fullPath);
    }

    private void initDecoder(String fullPath) throws IOException {
        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(fullPath);
        }
        //Sometimes it works and sometimes it don't....
        catch (IOException e) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            try {
                extractor = new MediaExtractor();
                extractor.setDataSource(fullPath);
            } catch (IOException e1) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                extractor = new MediaExtractor();
                extractor.setDataSource(fullPath);
            }
        }

        format = extractor.getTrackFormat(0);
        String mime = format.getString(MediaFormat.KEY_MIME);
        durationUs = format.getLong(MediaFormat.KEY_DURATION);
        Log.d("debug", "mime=" + mime);
        codec = MediaCodec.createDecoderByType(mime);
        codec.configure(format, null, null, 0);
        codec.start();
        codecInputBuffers = codec.getInputBuffers();
        codecOutputBuffers = codec.getOutputBuffers();

        extractor.selectTrack(0);
        info = new BufferInfo();
    }

    public void setPath(String path) {
        try {
            if (extractor == null) {
                initDecoder(path);
            } else {
                try {
                    extractor.setDataSource(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    extractor.release();
                    extractor = null;
                    extractor = new MediaExtractor();
                    extractor.setDataSource(path);
                }
                format = extractor.getTrackFormat(0);
                durationUs = format.getLong(MediaFormat.KEY_DURATION);
                if (codec == null) {
                    String mime = format.getString(MediaFormat.KEY_MIME);
                    codec = MediaCodec.createDecoderByType(mime);
                    codec.configure(format, null, null, 0);
                    codec.start();
                }
//                codecInputBuffers = codec.getInputBuffers();
//                codecOutputBuffers = codec.getOutputBuffers();
                extractor.selectTrack(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(boolean isPlaying) {
        Log.d("debug", "close start  isPlaying="+isPlaying);
        try {
            if (codec != null) {
                codecInputBuffers = null;
                codecOutputBuffers = null;
                if (isPlaying) {
                    codec.flush();
                    codec.reset();
                    codec.stop();
                    codec.release();
                }
                codec = null;
                Log.d("debug", "close codec");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (codec != null) {
                if (isPlaying) {
                    codec.release();
                }
                codec = null;
            }
        }
        try {
            if (extractor != null) {
                resetEOS();
                extractor.release();
                extractor = null;
                Log.d("debug", "close extractor");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (extractor != null) {
                extractor.release();
                extractor = null;
            }
        }
        Log.d("debug", "close end");
    }

    @Override
    public void reset() {
        codec.flush();
        codec.reset();
//        codec.stop();
//        codec.release();
//        extractor.release();
        resetEOS();
    }

    @Override
    public boolean decodeChunk() {
        boolean newBytes = false;
        advanceInput();

        final int res = codec.dequeueOutputBuffer(info, TIMEOUT_US);
        if (res >= 0) {
            if (info.size - info.offset < 0) {
                Log.d("DECODE", "BURRRRP");
                int outputBufIndex = res;
                ByteBuffer buf = codecOutputBuffers[outputBufIndex];
                if (lastChunk == null || lastChunk.length != info.size) {
                    //this should always be info.size == 0, should the whole thing be refactored out?
                    lastChunk = new byte[info.size];
                }
                buf.get(lastChunk);
                buf.clear();
                codec.releaseOutputBuffer(outputBufIndex, false);
                newBytes = true;
            } else {
                processBytes(res);
                newBytes = true;
            }
        }
        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            currentTimeUs = durationUs;
            sawOutputEOS = true;
        } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            codecOutputBuffers = codec.getOutputBuffers();
        } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            format = codec.getOutputFormat();
            Log.d("MP3", "Output format has changed to " + format);
        }

        return newBytes;
    }

    private void processBytes(int res) {
        int outputBufIndex = res;
        ByteBuffer buf = codecOutputBuffers[outputBufIndex];
        if (lastChunk == null
                || lastChunk.length != info.size - info.offset) {
            lastChunk = new byte[info.size - info.offset];
        }
        if (info.size != 0) currentTimeUs = info.presentationTimeUs;
        buf.position(info.offset);
        buf.get(lastChunk);
        buf.clear();
        codec.releaseOutputBuffer(outputBufIndex, false);
    }

    @Override
    public void resetEOS() {
        sawOutputEOS = false;
        info.flags = 0;
    }

    @Override
    public boolean sawOutputEOS() {
        return sawOutputEOS;
    }

    // not flushing creates distortion
    @Override
    public void seek(long timeInUs, boolean shouldFlush) {
        extractor.seekTo(timeInUs, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        currentTimeUs = timeInUs;
        //Log.d("DECODE", "CURRENT TIME: " + String.valueOf(currentTimeUs));
        //Log.d("DECODE", "total duration: " + String.valueOf(durationUs));
        // if (shouldFlush)
        codec.flush();
    }

    private void advanceInput() {
        boolean sawInputEOS = false;

        int inputBufIndex = codec.dequeueInputBuffer(TIMEOUT_US);
        if (inputBufIndex >= 0) {
            ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];

            int sampleSize = extractor.readSampleData(dstBuf, 0);
            long presentationTimeUs = 0;

            if (sampleSize < 0) {
                sawInputEOS = true;
                sampleSize = 0;
            } else {
                presentationTimeUs = extractor.getSampleTime();
                //currentTimeUs = presentationTimeUs;
            }

            codec.queueInputBuffer(inputBufIndex, 0, sampleSize,
                    presentationTimeUs,
                    sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
            if (!sawInputEOS) {
                extractor.advance();
            }
        }
    }

    @Override
    public byte[] getLastChunk() {
        return lastChunk;
    }

}
