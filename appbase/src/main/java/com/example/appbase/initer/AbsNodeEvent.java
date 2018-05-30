package com.example.appbase.initer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsNodeEvent<Event extends AbsNodeEvent> {

    private Event pre;
    private List<Event> nexts = new ArrayList<>();

    public void setPre(Event pre){
        this.pre = pre;
    }

    public Event getPre() {
        return pre;
    }

    public void add2Next(Event ...events){
        for (Event event : events) {
            event.setPre(this);
            nexts.add(event);
        }
    }

    public void startEvent(EventSubscriber<Event> subscriber){
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber can't be null");
        }

        subscriber.injectEvent((Event) this);
    }

    protected abstract void onHandleEvent();

    protected void onNext(){}

    protected void onFail(Object extra){}

    protected void onException(Throwable t){

    }
}
