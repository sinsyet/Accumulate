package com.example.appbase.initer;


import android.os.Handler;
import android.os.Looper;

public abstract class AbsIniter {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private AbsIniter mNext;
    protected InitObserver mObserver;

    public AbsIniter(){

    }

    public Object onStartHandleInit(){
        return new Object();
    }


    public AbsIniter appendIniter(AbsIniter initer) {
        if (mNext == null) {
            mNext = initer;
        }else{
            mNext.appendIniter(initer);
        }
        return this;
    }


    public void startInit(InitObserver observer) {
        mObserver = observer;

        Runnable mStartRunnable = new Runnable() {
            @Override
            public void run() {
                if (mObserver != null) {
                    mObserver.onStartInit();
                }

                onHandleInit();
            }
        };

        mHandler.post(mStartRunnable);
    }

    protected abstract void onHandleInit();

    protected void onInitException(Throwable t){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mObserver != null) {
                    mObserver.onInitException(AbsIniter.this,t);
                    mObserver.onEndInit(false);
                }
            }
        });
    }

    protected void onInitFail(int code, Object extra){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mObserver != null) {
                    mObserver.onInitFail(AbsIniter.this, code, extra);
                    mObserver.onEndInit(false);
                }
            }
        });
    }

    protected void onNext() {
        mHandler.post(() -> {
            if (mNext != null) {
                if (mObserver != null) {
                    mObserver.onInitProgress(AbsIniter.this,mNext);
                }
                mNext.mObserver = mObserver;

                mNext.onHandleInit();
            } else {
                if (mObserver != null) {
                    mObserver.onEndInit(true);
                }
            }
        });
    }


    public interface InitObserver {

        int COMMON_ERROR = -1;

        void onStartInit();

        void onInitProgress(AbsIniter curIniter, AbsIniter nextIniter);

        void onInitException(AbsIniter curIniter, Throwable t);

        void onInitFail(AbsIniter curIniter, int errorCode, Object extra);

        void onEndInit(boolean available);
    }
}
