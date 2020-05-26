package com.example.musicplayerdome.rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.musicplayerdome.R;


/**
 * 歌单封面的view,是一个圆角矩形
 */
public class RikkaRoundRectView extends androidx.appcompat.widget.AppCompatImageView {

    private float roundRatio = 16f;
    private Path path;

    public RikkaRoundRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RikkaRoundRectView, 0, 0);
        try {
            roundRatio = typedArray.getFloat(R.styleable.RikkaRoundRectView_roundRatio, 16f);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), roundRatio * getWidth(), roundRatio * getHeight(), Path.Direction.CW);
        }
        canvas.save();
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.restore();
    }
}
