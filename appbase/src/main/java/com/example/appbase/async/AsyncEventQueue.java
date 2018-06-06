package com.example.appbase.async;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AsyncEventQueue<AsyncHandler extends AbsAsyncEvent> {

    private List<AsyncHandler> mQueue = new ArrayList<>();
    private int index;
    public AsyncEventQueue(AsyncHandler handler, AsyncHandler... initers){

        mQueue.add(handler);
        mQueue.addAll(Arrays.asList(initers));
    }

    public AsyncEventQueue(List<AsyncHandler> initers){
        if(initers == null) return;



        mQueue.addAll(initers);
    }

    public AsyncEventQueue add(AsyncHandler initer){

        mQueue.add(initer);

        return this;
    }

    int size(){
        return mQueue.size();
    }

    boolean find(){
        int size = mQueue.size();
        return size > 0 && index < size;
    }

    AsyncHandler next(){
        return mQueue.get(index ++);
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

    public List<AsyncHandler> getAsyncHandler(){
        return mQueue;
    }
}
