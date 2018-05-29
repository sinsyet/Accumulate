package com.example.appbase.async;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AsyncEventQueue<AsyncHandler extends AbsAsyncEvent> {

    private LinkedList<AsyncHandler> mQueue;
    public AsyncEventQueue(AsyncHandler... initers){
        if(mQueue == null){
            mQueue = new LinkedList<>();
        }

        mQueue.addAll(Arrays.asList(initers));
    }

    public AsyncEventQueue(List<AsyncHandler> initers){
        if(initers == null) return;


        if(mQueue == null){
            mQueue = new LinkedList<>();
        }

        mQueue.addAll(initers);
    }

    public AsyncEventQueue add(AsyncHandler initer){

        mQueue.addFirst(initer);

        return this;
    }

    int size(){
        return mQueue.size();
    }

    boolean find(){
        return mQueue.size() > 0;
    }

    AsyncHandler next(){
        return mQueue.removeLast();
    }

    private AsyncEventQueue<AsyncHandler> mNext;
    AsyncEventQueue<AsyncHandler> appendNextQueue(AsyncEventQueue<AsyncHandler> next){
        if(mNext == null)
            mNext = next;
        else
            mNext.appendNextQueue(next);
        return this;
    }

    void setNextQueue(AsyncEventQueue<AsyncHandler> next){
        mNext = next;
    }

    AsyncEventQueue<AsyncHandler> nextQueue(){

        return mNext;
    }
}
