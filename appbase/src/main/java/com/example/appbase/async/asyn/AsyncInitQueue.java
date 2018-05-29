package com.example.appbase.async.asyn;


import com.example.appbase.async.AbsIniter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AsyncInitQueue {

    private LinkedList<AbsIniter> mQueue;
    public AsyncInitQueue(AbsIniter ... initers){
        if(mQueue == null){
            mQueue = new LinkedList<>();
        }

        mQueue.addAll(Arrays.asList(initers));
    }

    public AsyncInitQueue(List<AbsIniter> initers){
        if(initers == null) return;


        if(mQueue == null){
            mQueue = new LinkedList<>();
        }

        mQueue.addAll(initers);
    }

    public AsyncInitQueue add(AbsIniter initer){

        mQueue.addFirst(initer);

        return this;
    }

    int size(){
        return mQueue.size();
    }

    boolean find(){
        return true;
    }

    AbsIniter next(){
        return mQueue.removeLast();
    }
}
