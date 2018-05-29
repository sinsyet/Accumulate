package com.example.appbase.async;

import android.support.annotation.WorkerThread;

public abstract class AbsAsyncEvent {

    private AsyncEventSession session;
    private AsyncEventSession.Callback cb;

    void registerSessionCallback(AsyncEventSession.Callback cb, AsyncEventSession session){
        this.cb = cb;
        this.session = session;
    }

    @WorkerThread
    protected abstract void onHandleEvent();

    protected void onNext(){
        cb.onEnd(this);
    }

    protected void onEventFail(int code){
        cb.onFail(this,code);
    }

    protected void onEventException(Throwable t){

        cb.onException(this, t);
    }

    protected boolean isCanContinute(){
        return session.isConcurrentRunning();
    }
}
