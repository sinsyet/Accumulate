package com.example.sample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.viewlib.domains.Pengaton;

/**
 * Created by YGX on 2018/3/25.
 */

public class PentacleView extends View {

    private Paint mPaint;
    private int mW;
    private int mH;
    private Pengaton mPen;
    private Path mPath;

    public PentacleView(Context context) {
        super(context);
        init();
    }

    public PentacleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PentacleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PentacleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 必须写满argb, 否则默认a是00
        mPaint.setColor(0xFFFF0000);
        mPaint.setShadowLayer(100,0,0,0xff00ff00);
        Log.e(TAG, "init: ");
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w == 0 || h == 0) return;

        mW = w;
        mH = h;

        mPen = new Pengaton(mW / 2, mH / 2, Math.min(mW / 2, mH / 2));
        PointF mCenter = mPen.mCenter;
        mPath = mPen.mPath;
        invalidate();
        Log.e(TAG, "onSizeChanged: "+mH+" // "+mW+" // "+mCenter.toString());
    }

    private static final String TAG = "PentacleView";
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPath != null) {
            mPaint.setShadowLayer(Math.min(mW/2,mH/2) * 1.2f,0,0,0xffff00ff);
            canvas.drawPath(mPath,mPaint);
        }

        Log.e(TAG, "onDraw: ");
    }
}
