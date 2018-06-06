package com.example.sample.async.initer;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.appbase.async.AbsAsyncEvent;

public abstract class AbsIniter extends AbsAsyncEvent {
    private static final String TAG = "AbsIniter";
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    /**
     * 无progress
     */
    public static final int TYPE_BASE = 1;
    public static final int TYPE_PROGRESS = 2;

    public static final int STATE_NONE = 0;
    public static final int STATE_INIT = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_FAIL = 3;
    public static final int STATE_EXCEPTION = 4;

    private int mInitState = STATE_NONE;
    private int mInitType = TYPE_BASE;


    private OnInitProgressListener mListener;
    private long mStartMillis;
    private long mEndMillis = -1;

    public Object getHintExtra() {
        return this.getClass().getSimpleName();
    }

    private Object mFailExtra;
    public Object getFailExtra(){
        return mInitState == STATE_FAIL ? mFailExtra : null;
    }

    private Throwable mThrowable;
    public Throwable getThrowableExtra(){
        return mInitState == STATE_EXCEPTION ? mThrowable : null;
    }

    @Override
    protected final void onHandleEvent() {
        mStartMillis = System.currentTimeMillis();

        mInitState = STATE_INIT;

        onInit();
    }

    protected abstract void onInit();

    protected void setInitType(int type){
        mInitType = type;
    }

    public int getInitType(){
        return mInitType;
    }

    @Override
    protected final void onEventException(Throwable t) {
        mInitState = STATE_EXCEPTION;
        mThrowable = t;
        super.onEventException(t);
    }

    @Override
    protected final void onEventFail(int code) {
        mFailExtra = code;
        mInitState = STATE_FAIL;
        super.onEventFail(code);
    }

    @Override
    protected void onNext() {
        super.onNext();
        mInitState = STATE_SUCCESS;
    }

    public long getInitElapsedTime() {
        if (mEndMillis == -1)
            return System.currentTimeMillis() - mStartMillis;
        else
            return mEndMillis - mStartMillis;
    }

    public int getInitState(){
        return mInitState;
    }

    public void setOnInitProgressListener(OnInitProgressListener listener) {
        mListener = listener;
    }

    protected void onProgress(float total, float progress) {

        if(mInitType == TYPE_BASE) return;

        if (total <= 0) {
            Log.d(TAG, "onProgress" + getClass().getSimpleName() + " report error progress value: "
                    + " total : " + total + ", progress: " + progress);
            return;
        }

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onProgress(total,progress, curFrequency = ((progress > total ? total : progress) * 100f / total));
                }
            }
        });

    }

    private float curFrequency = 0.0f;
    public float getCurFrequency() {
        return curFrequency;
    }

    public interface OnInitProgressListener {

        void onProgress(float total, float progress, float frequency);
    }
}
