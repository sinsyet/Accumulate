package com.example.facedemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.apphelper.AppHelper;
import com.example.facedemo.msc.FaceR;

import java.util.List;

public class FaceDrawerView extends View {

    private FaceR faceR;
    private int mW;
    private int mH;
    private Paint mDrawCirclePaint;

    public FaceDrawerView(Context context) {
        super(context);
        init();
    }

    public FaceDrawerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FaceDrawerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FaceDrawerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){

        initPaint();
    }

    private Paint mPaint;

    private void initPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(AppHelper.dp2px(getContext(), 2));
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(5));
        mPaint = paint;

        mDrawCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawCirclePaint.setColor(Color.GREEN);
    }

    private boolean hasFace;
    public void postRefreshFace(FaceR r){
        if (r == null || r.getCount() < 1) {
            if(hasFace){
                hasFace = false;
                postInvalidate();
            }
            return;
        }
        hasFace = true;
        faceR = r;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(hasFace){
            drawFace(canvas);
        }else{
            drawNoFace(canvas);
        }


    }

    void drawNoFace(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    void drawFace(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        List<FaceR.Face> faces = faceR.getFaces();
        int size = faces.size();
        Paint paint = mPaint;
        FaceR.Face face = null;
        Path position = null;
        for (int i = 0; i < size; i++) {
            face = faces.get(i);
            position = face.getPosition();
            canvas.drawPath(position, paint);
           /* List<Path> landmarks = face.getLandmarks();
            for (Path path : landmarks) {
                canvas.drawPath(path, paint);
            }*/
            List<PointF> points = face.getPoints();
            for (PointF f : points) {
                canvas.drawCircle(f.x,f.y,3,mDrawCirclePaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
    }
}
