package org.sample.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class TextView1 extends android.support.v7.widget.AppCompatTextView {
    public TextView1(Context context) {
        super(context);
    }

    public TextView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: "+event.getAction());
        return super.onTouchEvent(event);
//        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: "+ev.getAction());
        return super.dispatchTouchEvent(ev);
//        return true;
    }

    private static final String TAG = "TextView1";
}
