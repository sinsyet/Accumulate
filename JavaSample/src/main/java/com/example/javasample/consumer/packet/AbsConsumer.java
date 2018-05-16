package com.example.javasample.consumer.packet;

public abstract class AbsConsumer<P1,P2> {

    private AbsConsumer<P1,P2> mNext;

    public AbsConsumer(){}

    public void append(AbsConsumer<P1,P2> next){
        if(mNext != null){
            mNext.append(next);
        }else{
            mNext = next;
        }
    }

    public void postConsume(P1 param,P2...params){
        boolean b = handleConsume(param, params);
        if(!b && mNext != null){
            mNext.postConsume(param, params);
        }
    }

    protected abstract boolean handleConsume(P1 param, P2...params);
}
