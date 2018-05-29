package com.example.sample.async.initer;


import com.example.appbase.async.AsyncEventQueue;

import java.util.List;

public class AbsInitQueue extends AsyncEventQueue<AbsIniter> {

    public AbsInitQueue(AbsIniter... initers) {
        super(initers);
    }

    public AbsInitQueue(List<AbsIniter> initers) {
        super(initers);
    }
}
