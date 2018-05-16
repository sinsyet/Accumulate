package com.example.javasample.channel;

import com.example.javasample.consumer.packet.AbsConsumer;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface IChannelHandler<P1,P2> {

    void onRead(SelectionKey key) throws IOException;

    void onAccept(SelectionKey key) throws IOException;

    void onWrite(SelectionKey key) throws IOException;

    void onConnect(SelectionKey key) throws IOException;

    void onSelect(SelectionKey key) throws IOException;

    void appendConsumer(AbsConsumer<P1,P2> consumer);
}
