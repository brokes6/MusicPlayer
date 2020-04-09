package com.smp.soundtouchandroid;

import java.io.IOException;

public interface AudioSink {
    int write(byte[] input, int offSetInBytes, int sizeInBytes) throws IOException;

    void close(Boolean ... isPlaying) throws IOException;
}
