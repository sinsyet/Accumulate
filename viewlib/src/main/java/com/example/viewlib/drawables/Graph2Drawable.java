package com.example.viewlib.drawables;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.viewlib.utils.AppHelper;

import java.util.Random;


public class Graph2Drawable extends Drawable implements ValueAnimator.AnimatorUpdateListener, Animatable {

    private Context ctx;
    private Paint mPaint;
    private Path mPath;


    public Graph2Drawable(Context ctx) {
        this.ctx = ctx;

        init();
    }
    
    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPath = new Path();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            if(i == 0)
            mPath.moveTo(i * AppHelper.dp2px(ctx,20),AppHelper.dp2px(ctx,50+ random.nextInt(40)));
            else
            mPath.lineTo(i * AppHelper.dp2px(ctx,20),AppHelper.dp2px(ctx,50+ random.nextInt(40)));
        }
    }


    /**
     * 线条的颜色
     */
    private int mColor = Color.RED;



    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(mPath,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
