package com.example.facedemo.msc;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class FaceDrawerWrapper extends View implements IFaceDrawer {

    /**
     * 专门用于计算的handler
     */
    private Handler mCalcHandler;

    public FaceDrawerWrapper(Context context) {
        super(context);
    }

    public FaceDrawerWrapper(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FaceDrawerWrapper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FaceDrawerWrapper(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onUpdateFace(String desc) {

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        initCalcHanlder();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        releaseCalcHandler();
    }

    final void initCalcHanlder(){
        HandlerThread calcer = new HandlerThread("calcer");
        calcer.start();
        Looper looper = calcer.getLooper();
        mCalcHandler = new Handler(looper);
    }

    final void releaseCalcHandler(){
        if (mCalcHandler == null) {
            return;
        }

        mCalcHandler.removeCallbacksAndMessages(null);
        mCalcHandler.getLooper().quit();
        mCalcHandler = null;
    }
}
