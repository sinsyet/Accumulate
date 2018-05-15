package com.example.javasample.channel;

import com.example.javasample.utils.AppHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class UdpChannelHandlerWrapper extends AbsUdpChannelHandler {
    private ByteBuffer buf = ByteBuffer.allocate(1024);

    @Override
    public void onRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        InetSocketAddress remote = (InetSocketAddress) channel.receive(buf);
        String fromHost = remote.getAddress().getHostAddress();
        int fromPort = remote.getPort();
        String msg = AppHelper.getMsgByByteBuffer(buf);

    }
}
