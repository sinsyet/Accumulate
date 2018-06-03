package org.sample.demo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class FrameLayout1 extends FrameLayout {
    public FrameLayout1(@NonNull Context context) {
        super(context);
    }

    public FrameLayout1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayout1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FrameLayout1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        Object tag = getTag();
        Log.e(TAG+tag, "dispatchHoverEvent: "+event.getAction());
        return super.dispatchHoverEvent(event);
    }

    private static final String TAG = "FrameLayout1: ";

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Object tag = getTag();
        Log.e(TAG+ tag, "onInterceptTouchEvent: "+ev.getAction());
        /*if ("layout_1".equals(tag)) {
            return true;
        }*/
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        Log.e(TAG+getTag(), "onInterceptHoverEvent: "+event.getAction());
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Object tag = getTag();
        Log.e(TAG+ tag, "onTouchEvent: "+event.getAction());
        if ("layout_2".equals(tag)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Object tag = getTag();
        Log.e(TAG+ tag, "dispatchTouchEvent: "+ev.getAction());

        return super.dispatchTouchEvent(ev);
    }
}
