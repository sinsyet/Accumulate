package com.example.javasample.channel;

import com.example.javasample.utils.Log;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public abstract class AbsUdpChannelHandler<P1,P2> implements IChannelHandler<P1,P2> {
    private static final String TAG = "AbsUdpChannelHandler";
    @Override
    public void onAccept(SelectionKey key) {

    }

    @Override
    public void onWrite(SelectionKey key) {

    }

    @Override
    public void onConnect(SelectionKey key) {

    }

    @Override
    public void onSelect(SelectionKey key) throws IOException {
        int ops = key.interestOps();
        switch (ops) {
            case SelectionKey.OP_READ:
                onRead(key);
                break;
            default:
                Log.e(TAG, "onSelect: invalid ops: "+ops);
                break;
        }
    }
}
