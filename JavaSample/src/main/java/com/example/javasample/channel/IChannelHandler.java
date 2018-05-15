package com.example.javasample.channel;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface IChannelHandler {

    void onRead(SelectionKey key) throws IOException;

    void onAccept(SelectionKey key) throws IOException;

    void onWrite(SelectionKey key) throws IOException;

    void onConnect(SelectionKey key) throws IOException;

    void onSelect(SelectionKey key) throws IOException;
}
