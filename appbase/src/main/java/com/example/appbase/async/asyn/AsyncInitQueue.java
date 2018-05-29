package com.example.appbase.async.asyn;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AsyncInitQueue {

    private LinkedList<AbsAsyncIniter> mQueue;
    public AsyncInitQueue(AbsAsyncIniter... initers){
        if(mQueue == null){
            mQueue = new LinkedList<>();
        }

        mQueue.addAll(Arrays.asList(initers));
    }

    public AsyncInitQueue(List<AbsAsyncIniter> initers){
        if(initers == null) return;


        if(mQueue == null){
            mQueue = new LinkedList<>();
        }

        mQueue.addAll(initers);
    }

    public AsyncInitQueue add(AbsAsyncIniter initer){

        mQueue.addFirst(initer);

        return this;
    }

    int size(){
        return mQueue.size();
    }

    boolean find(){
        return true;
    }

    AbsAsyncIniter next(){
        return mQueue.getLast();
    }
}
