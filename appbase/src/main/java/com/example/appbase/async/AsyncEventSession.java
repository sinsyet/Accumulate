package com.example.appbase.async;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncEventSession<EventQueue extends AsyncEventQueue<AsyncHandler>, AsyncHandler extends AbsAsyncEvent>
        implements IEventSession<EventQueue, AsyncEventCallback<EventQueue,AsyncHandler>> {
    private static final String TAG = "AsyncEventSession";
    private ExecutorService mEnginePool;

    private SparseArray<EventQueue> mQueues;
    private AsyncEventCallback<EventQueue,AsyncHandler> mCallback;

    private volatile int mQueueIndex;

    private EventQueue mFirstQueue;

    private Handler mHandler;

    interface Status {

        int INVALID = -1;
        int RUNNING = 0;
        int SUCCESS = 1;
        int FAIL = 2;
        int INTERRUPT = 3;

    }

    private int mStatus = Status.INVALID;

    public AsyncEventSession() {
        this(new Handler(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper()),
                Executors.newCachedThreadPool());
    }

    public AsyncEventSession(Handler handler) {
        this(handler, Executors.newCachedThreadPool());
    }

    public AsyncEventSession(Handler handler, ExecutorService pool) {
        mHandler = handler;
        mEnginePool = pool;
        mQueueIndex = 0;
    }

    @Override
    public void append(EventQueue asynInitQueue) {
        if (mStatus != Status.INVALID && mStatus != Status.RUNNING) {
            // throw new IllegalStateException("会话已执行完毕, 执行完毕后再添加初始化队列是一个无效的操作");
            throw new IllegalStateException("The session has been executed, " +
                    "append an initialization queue after execution is invalid operation");
        }

        if (asynInitQueue == null) {
            throw new NullPointerException("queue can not be null");
        }

        if (asynInitQueue.size() <= 0) {
            throw new IllegalStateException(" queue should not be empty");
        }

       /* SparseArray<EventQueue> queues = getQueues();
        queues.put(queues.size(),asynInitQueue);*/
       if(mFirstQueue == null) {
           mFirstQueue = asynInitQueue;
       }else{
           mFirstQueue.appendNextQueue(asynInitQueue);
       }

    }


    @Override
    public synchronized void startSession(AsyncEventCallback<EventQueue,AsyncHandler> asynInitCallback) {
        if (mStatus != Status.INVALID) {
            throw new IllegalStateException("The session has started. Please do not start it repeatedly");
        }

        /*if (getQueues().size() <= 0) {
            throw new IllegalStateException("There is no executable initer");
        }*/
        if(mFirstQueue == null){
            throw new IllegalStateException("There is no executable initer");
        }

        if (asynInitCallback == null) {
            throw new NullPointerException("Session callback should not be null");
        }


        mCallback = asynInitCallback;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mStatus = Status.RUNNING;
                mCallback.onStartSession(AsyncEventSession.this);
                // EventQueue queue = getQueues().get(mQueueIndex);
                EventQueue queue = mFirstQueue;
                startAsyncInitQueue(queue);
            }
        });

    }

    public void insertEventQueue(EventQueue queue){
        if (queue == null) {
            return;
        }
        if(mCurQueue != null){
            @SuppressWarnings("unchecked")
            EventQueue srcNextQueue = (EventQueue) mCurQueue.nextQueue();
            mCurQueue.setNextQueue(queue);
            queue.setNextQueue(srcNextQueue);
        }
    }

    public void add2CurEventQueue(AsyncHandler handler){
        if (handler == null) {
            return;
        }
        if (mCurQueue != null) {
            mCurQueue.add(handler);
        }
    }

    @Override
    public void interruptSession() {
        mStatus = Status.INTERRUPT;
        mRunningFlag = false;
    }

    private EventQueue mCurQueue;
    private void startAsyncInitQueue(EventQueue queue) {
        mRunningFlag = true;
        mRunningIniter.clear();
        mCurQueue =  queue;
        mCallback.onEventQueueStart(this, mCurQueue);
        while (queue.find()) {

            final AsyncHandler next = queue.next();

            next.registerSessionCallback(mCb, this);
            mRunningIniter.add(next);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onEventStart(AsyncEventSession.this, next);
                    mEnginePool.execute(new Runnable() {
                        @Override
                        public void run() {
                            next.onHandleEvent();
                        }
                    });
                }
            });
        }
    }

    private List<AbsAsyncEvent> mRunningIniter = new ArrayList<>();

    private SparseArray<EventQueue> getQueues() {
        return mQueues == null ? mQueues = new SparseArray<>() : mQueues;
    }

    private void release() {
        mEnginePool.shutdown();
    }

    public int getStatus() {

        return mStatus;
    }

    boolean isConcurrentRunning() {
        return mRunningFlag;
    }

    private boolean mRunningFlag;
    private Callback<AsyncHandler> mCb = new Callback<AsyncHandler>() {

        @Override
        public void onEnd(final AsyncHandler cur) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onEventEnd(AsyncEventSession.this, cur);
                    mRunningIniter.remove(cur);

                    if (mRunningIniter.size() == 0) {
                        onNextIniterQueue();
                    }
                }
            });
        }

        @Override
        public void onFail(final AsyncHandler cur, final Object extra) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (getStatus() != Status.INTERRUPT) {
                        mRunningFlag = mCallback.onEventFail(AsyncEventSession.this, cur, extra);
                    }

                    mRunningIniter.remove(cur);
                    if (mRunningIniter.size() == 0) {
                        onNextIniterQueue();
                    }
                }
            });
        }

        @Override
        public void onException(final AsyncHandler cur, final Throwable t) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (getStatus() != Status.INTERRUPT) {
                        mRunningFlag = mCallback.onEventException(AsyncEventSession.this, cur, t);
                    }
                    mRunningIniter.remove(cur);
                    if (mRunningIniter.size() == 0) {
                        onNextIniterQueue();
                    }
                }
            });
        }
    };

    private void onNextIniterQueue() {

        mCallback.onEventQueueEnd(this,mCurQueue);

        if (getStatus() == Status.INTERRUPT) {
            mCallback.onEndSession(this, false);
            release();
            return;
        }

        // mQueueIndex++;
        // if (mQueueIndex >= mQueues.size()) {
        EventQueue q = null;
        if ((q = (EventQueue) mCurQueue.nextQueue()) == null) {
            if (mRunningFlag) {
                mStatus = Status.SUCCESS;
                mCallback.onEndSession(this, true);
            } else {
                mStatus = Status.FAIL;
                mCallback.onEndSession(this, false);
            }
            release();
        } else {
            if (mRunningFlag) {
                // EventQueue queue = getQueues().get(mQueueIndex);
                startAsyncInitQueue(q);
            } else {
                mStatus = Status.FAIL;
                mCallback.onEndSession(this, false);
                release();
            }
        }
    }

    interface Callback<AsyncHandler extends AbsAsyncEvent> {

        void onEnd(AsyncHandler cur);

        void onFail(AsyncHandler cur, Object extra);

        void onException(AsyncHandler cur, Throwable t);
    }
}
