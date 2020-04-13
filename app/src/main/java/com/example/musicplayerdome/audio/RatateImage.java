package com.example.musicplayerdome.audio;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * 播放动画
 */

public final class RatateImage {
    private final ValueAnimator rotateAnim;
    private boolean isSpin = false;

    public RatateImage(Context context, final ImageView view) {
        rotateAnim = ObjectAnimator.ofFloat(0, 360);
        rotateAnim.setDuration(45 * 1000);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setInterpolator(new LinearInterpolator());
        view.setRotation(0.0f);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setRotation(value);
            }
        });
    }

    public void startSpin() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (rotateAnim.isPaused()) {
                rotateAnim.resume();
            } else {
                rotateAnim.start();
            }
        } else {
            rotateAnim.start();
        }

        isSpin = true;
    }

    public void stopSpin() {

        if (rotateAnim.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                rotateAnim.pause();
            } else {
                rotateAnim.cancel();
            }
            isSpin = false;
        }
    }
    public void initSpin(){
        if (rotateAnim!=null){
            rotateAnim.cancel();
        }
    }

    public boolean isSpin() {
        return isSpin;
    }

}
