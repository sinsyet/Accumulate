package com.example.viewlib.drawables;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;

import com.example.viewlib.utils.AppHelper;


public class CallingDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener,Animatable{

    private Context ctx;
    // 线条数量
    private RectF[] mRectLines = new RectF[11];

    /**
     * 线条默认的最大高度
     */
    private int mMaxHeight;

    /**
     * 线条默认的最小高度
     */
    private int mMinHeight;

    /**
     * 线条的默认宽度
     */
    private int mDefaultWidth;

    /**
     * 线条之间的间隙宽度
     */
    private int mDefaultGap;

    /**
     * 显示模式
     *
     * @see #CONNECTED
     * @see #CONNECTING
     */
    private int mMode = CONNECTING;

    /**
     * 居中对齐模式
     */
    private int mGravity;

    /**
     * 呼叫中
     */
    public static final int CONNECTING = 1;

    /**
     * 呼通了
     */
    public static final int CONNECTED = 2;

    /**
     * 图层的总高
     */
    private int mHeight;

    /**
     * 图层的总宽
     */
    private int mWidth;


    /**
     * 图形的中心点X坐标
     */
    private int mCenterX;
    /**
     * 图形的中心点Y坐标
     */
    private int mCenterY;
    private Paint mPaint;
    private ValueAnimator animator;
    private float fraction;

    public CallingDrawable(Context ctx){
        this.ctx = ctx;

        mMaxHeight = AppHelper.dp2px(ctx,60);
        mMinHeight = AppHelper.dp2px(ctx,20);
        mDefaultWidth = AppHelper.dp2px(ctx,6);
        mDefaultGap =  AppHelper.dp2px(ctx,20);

        init();
    }

    private void init() {



        for (int i = 0; i < mRectLines.length; i++) {
            mRectLines[i] = new RectF();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);

        animator = new ValueAnimator();
        animator.setFloatValues(0,1);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.addUpdateListener(this);
    }

    /**
     * 线条的颜色
     */
    private int mColor = Color.RED;

    public void onViewSizeEffect(int width, int height){
        this.mWidth = width;
        this.mHeight = height;


        mCenterX = width / 2;
        mCenterY = height / 2;

        switch (mMode){
            case CONNECTING:
                calcConnectingLine();
                break;
            case CONNECTED:
                calcConnectedLine();
                break;
        }

        invalidateSelf();
    }

    float[][] vvs2 = {
            {0,1,2,3,2,1,0},
            {1,0,1,2,3,2,1},
            {2,1,0,1,2,3,2},
            {3,2,1,0,1,2,3},
            {2,3,2,1,0,1,2},
            {1,2,3,2,1,0,1}
    };
    int values[] = {2,1,0,1,2,3};
    private void calcConnectingLine(){

        int growHeight = (int) ((mMaxHeight / 2 - mMinHeight / 2)*1.0f / 3);
        for (int i = 0; i < mRectLines.length; i++) {
            int index = i % values.length;


            mRectLines[i].set(
                    mCenterX + (i - 5) * mDefaultGap - mDefaultWidth / 2,
                    mCenterY - mMinHeight/2 - AppHelper.getValue(fraction,vvs2[index]) * growHeight,
                    mCenterX + (i - 5) * mDefaultGap + mDefaultWidth / 2,
                    mCenterY + mMinHeight/2 + AppHelper.getValue(fraction,vvs2[index]) * growHeight);
        }
    }

    private void calcConnectedLine(){
        int activeHeight = mMaxHeight - mMinHeight;
        int growHeight = (int) (activeHeight * 1.0f / 3);

        for (int i = 0; i < mRectLines.length; i++) {
            int index = i % values.length;


            mRectLines[i].set(
                    mCenterX + (i - 5) * mDefaultGap - mDefaultWidth / 2,
                    mCenterY - AppHelper.getValue(fraction,vvs2[index]) * growHeight,
                    mCenterX + (i - 5) * mDefaultGap + mDefaultWidth / 2,
                    mCenterY + mMinHeight);
        }
    }

    private static final String TAG = "CallingDrawable";

    @Override
    public void draw(@NonNull Canvas canvas) {

        for (int i = 0; i < mRectLines.length; i++) {
            canvas.drawRect(mRectLines[i],mPaint);
        }

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        fraction = animation.getAnimatedFraction();
        switch (mMode){
            case CONNECTING:
                calcConnectingLine();
                break;
            case CONNECTED:
                calcConnectedLine();
                break;
        }
        invalidateSelf();
    }

    @Override
    public void start() {
        animator.start();
    }

    @Override
    public void stop() {
        animator.start();
    }

    @Override
    public boolean isRunning() {
        return animator.isRunning();
    }
}
