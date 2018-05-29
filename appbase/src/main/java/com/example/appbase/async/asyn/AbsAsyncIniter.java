package com.example.appbase.async.asyn;

import android.support.annotation.WorkerThread;

public abstract class AbsAsyncIniter{

    private int id;
    private AsyncInitSession.Callback cb;

    void registerSessionCallback(AsyncInitSession.Callback cb, int id){
        this.cb = cb;
        this.id = id;
    }

    int getRegisterId(){
        return id;
    }

    @WorkerThread
    protected abstract void onHandleInit();

    protected void onNext(){
        cb.onEnd(this);
    }

    protected void onInitFail(int code){
        cb.onFail(this,code);
    }

    protected void onInitException(Throwable t){

        cb.onException(this, t);
    }
}
