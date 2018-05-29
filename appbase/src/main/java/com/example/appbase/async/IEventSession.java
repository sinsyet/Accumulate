package com.example.appbase.async;


public interface IEventSession<Next, Callback> {

    void append(Next next);

    void startSession(Callback callback);

    void interruptSession();

}
