package com.example.appbase.async.asyn;

import android.os.Handler;
import android.os.Looper;


import com.example.appbase.async.IEventSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncInitSession implements IEventSession<AsyncInitQueue, AsyncInitCallback> {

    private ExecutorService mEnginePool;

    private List<AsyncInitQueue> mQueues;
    private AsyncInitCallback mCallback;

    private volatile int mQueueIndex;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    interface Status {

        int INVALID = -1;
        int RUNNING = 0;
        int SUCCESS = 1;
        int FAIL = 2;
        int INTERRUPT = 3;

    }

    private int mStatus = Status.INVALID;

    public AsyncInitSession() {
        mEnginePool = Executors.newCachedThreadPool();
        mQueueIndex = 0;
    }

    @Override
    public void append(AsyncInitQueue asynInitQueue) {
        if (mStatus != Status.INVALID && mStatus != Status.RUNNING) {
            // throw new IllegalStateException("会话已执行完毕, 执行完毕后再添加初始化队列是一个无效的操作");
            throw new IllegalStateException("The session has been executed, " +
                    "append an initialization queue after execution is invalid operation");
        }

        if(asynInitQueue == null){
            throw new NullPointerException("queue can not be null");
        }

        if(asynInitQueue.size() <= 0){
            throw new IllegalStateException(" queue should not be empty");
        }

        getQueues().add(asynInitQueue);
    }

    @Override
    public synchronized void startSession(AsyncInitCallback asynInitCallback) {
        if(mStatus != Status.INVALID){
            throw new IllegalStateException("The session has started. Please do not start it repeatedly");
        }

        if(getQueues().size() <= 0){
            throw new IllegalStateException("There is no executable initer");
        }

        if(asynInitCallback == null){
            throw new NullPointerException("Session callback should not be null");
        }


        mCallback = asynInitCallback;

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onStartSession(AsyncInitSession.this);
            }
        });

        AsyncInitQueue queue = getQueues().get(mQueueIndex);

        startAsyncInitQueue(queue,mQueueIndex);
    }

    @Override
    public void interruptSession() {

    }

    private void startAsyncInitQueue(AsyncInitQueue queue, int index)
    {
        mRunningIniter.clear();
        while (queue.find()){

            AbsAsyncIniter next = queue.next();
            // next.registerSessionId();
        }
    }

    private List<AbsAsyncIniter> mRunningIniter = new ArrayList<>();

    private List<AsyncInitQueue> getQueues() {
        return mQueues == null ? mQueues = new ArrayList<>() : mQueues;
    }

    private void release() {
        mEnginePool.shutdown();
    }

    public int getStatus() {

        return mStatus;
    }
}
