package com.example.appbase.async;

import android.support.annotation.WorkerThread;

public abstract class AbsIniter {

    @WorkerThread
    protected abstract void onHandleInit();

    protected void onNext(){}

    protected void onInitFail(){}

    protected void onInitException(){}
}
