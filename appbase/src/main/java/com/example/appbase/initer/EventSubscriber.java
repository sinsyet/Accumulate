package com.example.appbase.initer;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public abstract class EventSubscriber<Event extends AbsNodeEvent> {

    private Event root;
    private List<Event> curNodes = new ArrayList<>();

    private Handler mHandler;

    public EventSubscriber(Handler handler){
        mHandler = handler == null ? new Handler(Looper.getMainLooper()) : handler;
    }
    void injectEvent(Event event){
        root = event;

        onStartSession(this);
    }

    protected abstract void onStartSession(EventSubscriber<Event> subscriber);
    protected abstract void onEndSession(EventSubscriber<Event> subscriber);
}
