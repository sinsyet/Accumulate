package com.example.sample.async.initer;


import com.example.appbase.async.AbsAsyncEvent;

public abstract class AbsIniter extends AbsAsyncEvent {

    public Object getHintExtra(){
        return this.getClass().getSimpleName();
    }
}
