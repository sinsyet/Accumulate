package com.example.sample.async.initer;


import com.example.appbase.async.AsyncEventQueue;

import java.util.List;

public class AbsInitQueue extends AsyncEventQueue<AbsIniter> {

    public AbsInitQueue(AbsIniter first, AbsIniter... initers) {
        super(first,initers);
    }

    public AbsInitQueue(List<AbsIniter> initers) {
        super(initers);
    }
}
