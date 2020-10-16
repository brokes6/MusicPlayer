package com.example.musicplayerdome.rewrite;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.example.musicplayerdome.MyApplication;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.util.DensityUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

public class SingerAppBarBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "SingerAppBarBehavior";

    //最大放大倍数是0.5
    private static float MAX_SCALE = 0.5f;
    //放大最大高度
    private static final float MAX_ZOOM_HEIGHT = DensityUtil.dp2px(MyApplication.getContext(), 50);

    private ImageView mImageView, mImageViewCover;
    private TextView tvName;
    private RoundButton buttonPersonal;
    //记录AppbarLayout原始高度
    private int mAppbarHeight;
    //记录ImageView原始高度
    private int mImageViewHeight;
    private RelativeLayout rlTop;

    //Appbar的变化高度
    private int mCurrentBottom;
    //图片缩放比例
    private float mScaleValue;
    //手指在Y轴滑动的总距离
    private float mTotalDy;
    //是否做动画标志
    private boolean isAnimate;

    public SingerAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        init(abl);
        return handled;
    }

    /**
     * 进行初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
     *
     * @param abl
     */
    private void init(AppBarLayout abl) {
        abl.setClipChildren(false);
        mAppbarHeight = abl.getHeight();
        mImageView = abl.findViewById(R.id.iv_singer);
        mImageViewCover = abl.findViewById(R.id.iv_singer_cover);
        buttonPersonal = abl.findViewById(R.id.button_personal);
        rlTop = abl.findViewById(R.id.rl_top);
        tvName = abl.findViewById(R.id.tv_name);
        if (mImageView != null) {
            mImageViewHeight = mImageView.getHeight();
        }
    }

    /**
     * 是否处理嵌套滑动
     *
     * @param parent
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        isAnimate = true;
        return true;
    }

    /**
     * 在这里做具体的滑动处理
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (mImageView != null && child.getBottom() >= mAppbarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {
            zoomHeaderImageView(child, dy);
        } else {
            if (mImageView != null && child.getBottom() > mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
                consumed[1] = dy;
                zoomHeaderImageView(child, dy);
            } else {
                if (valueAnimator == null || !valueAnimator.isRunning()) {
                    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
                }

            }
        }

    }


    /**
     * 对ImageView进行缩放处理，对AppbarLayout进行高度的设置
     *
     * @param abl
     * @param dy
     */
    private void zoomHeaderImageView(AppBarLayout abl, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, MAX_ZOOM_HEIGHT);
        mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT * MAX_SCALE);
        mImageView.setScaleX(mScaleValue);
        mImageView.setScaleY(mScaleValue);
        mImageViewCover.setScaleX(mScaleValue);
        mImageViewCover.setScaleY(mScaleValue);
        mCurrentBottom = mAppbarHeight + (int) (mImageViewHeight / 2 * (mScaleValue - 1));
        rlTop.setTranslationY((int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        tvName.setTranslationY((int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        buttonPersonal.setTranslationY((int) (mImageViewHeight / 2 * (mScaleValue - 1)));
        abl.setBottom(mCurrentBottom);
    }


    /**
     * 处理惯性滑动的情况
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY > 100) {
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    /**
     * 滑动停止的时候，恢复AppbarLayout、ImageView的原始状态
     *
     * @param coordinatorLayout
     * @param abl
     * @param target
     * @param type
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
    }

    ValueAnimator valueAnimator;

    /**
     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
     *
     * @param abl
     */
    private void recovery(final AppBarLayout abl) {
        if (mTotalDy > 0) {
            mTotalDy = 0;
            if (isAnimate) {
                valueAnimator = ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(220);
                valueAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    mImageView.setScaleX(value);
                    mImageView.setScaleY(value);
                    mImageViewCover.setScaleX(value);
                    mImageViewCover.setScaleY(value);
                    int bottom = (int) (mCurrentBottom - (mCurrentBottom - mAppbarHeight) * animation.getAnimatedFraction());
                    rlTop.setTranslationY(bottom - mAppbarHeight);
                    tvName.setTranslationY(bottom - mAppbarHeight);
                    buttonPersonal.setTranslationY(bottom - mAppbarHeight);
                    abl.setBottom(bottom);
                });
                valueAnimator.start();
            } else {
                mImageView.setScaleX(1f);
                mImageView.setScaleY(1f);
                mImageViewCover.setScaleX(1f);
                mImageViewCover.setScaleY(1f);
                rlTop.setTranslationY(0);
                tvName.setTranslationY(0);
                buttonPersonal.setTranslationY(0);
                abl.setBottom(mAppbarHeight);
            }
        }
    }
}
