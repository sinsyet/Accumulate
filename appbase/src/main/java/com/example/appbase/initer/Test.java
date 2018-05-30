package com.example.appbase.initer;

public class Test {
    public static void test(){
        AbsNodeEvent event = new AbsNodeEvent() {
            @Override
            protected void onHandleEvent() {

            }
        };
        event.startEvent(new EventSubscriber(null) {
            @Override
            protected void onStartSession(EventSubscriber subscriber) {

            }

            @Override
            protected void onEndSession(EventSubscriber subscriber) {

            }
        });
    }
}
