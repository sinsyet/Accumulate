package com.example.appbase.async.asyn;

import android.support.annotation.WorkerThread;

public abstract class AbsAsyncIniter implements Runnable{

    private int id;

    void registerSessionId(int id){
        this.id = id;
    }

    @Override
    public void run() {
        onHandleInit();
    }

    @WorkerThread
    protected abstract void onHandleInit();

    protected void onNext(){

    }

    protected void onInitFail(){}

    protected void onInitException(){}
}
