package com.smp.soundtouchandroid;

public interface MediaCallBack {
    void onPrepare();

    void onPrepared(long duration);

    void onPlay();

    void onStop();

    void onPause();

    void onComplete();

    void onChange(int state);

    void onError();
}
