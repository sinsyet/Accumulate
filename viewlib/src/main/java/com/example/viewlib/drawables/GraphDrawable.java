package com.example.viewlib.drawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;


import com.example.viewlib.domains.Pengaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * 图形: 五角星, 正五边形;
 *
 *
 * @author YGX
 */
public class GraphDrawable extends Drawable {
    public static final int COUNT = 50;
    private final Random mRandom;
    private Context ctx;
    private List<Pengaton> mPengatons = new ArrayList<>();

    private Paint mPaint;

    private static final int CX     = 300;
    private static final int CY     = 300;
    private static final int RADIUS = 100;

    private int mScreenWidth;
    private int mScreenHeight;

    public GraphDrawable(Context ctx) {
        this.ctx = ctx;
        initScreenSize();
        mRandom = new Random();
        for (int i = 0; i < COUNT; i++){
            mPengatons.add(new Pengaton(mRandom.nextInt(mScreenWidth) + 10,mRandom.nextInt(mScreenHeight), mRandom.nextInt(100) + 10));
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    private void initScreenSize(){
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        for (int i = 0; i < COUNT; i++) {
            mPaint.setColor(Color.argb(mRandom.nextInt(150)+100,mRandom.nextInt(255),mRandom.nextInt(255),mRandom.nextInt(255)));
            canvas.drawPath(mPengatons.get(i).mPath,mPaint);
            /*mPaint.setColor(Color.GREEN);
            canvas.drawCircle(mPengatons.get(i).mCenter.x,mPengatons.get(i).mCenter.y,10,mPaint);*/
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


}
