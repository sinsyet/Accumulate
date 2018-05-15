package com.example.javasample.channel;

import com.example.javasample.consumer.packet.AbsConsumer;
import com.example.javasample.utils.AppHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class UdpChannelHandlerWrapper extends AbsUdpChannelHandler<String,Object> {
    private ByteBuffer buf = ByteBuffer.allocate(1024);
    private AbsConsumer<String, Object> mConsumer;
    @Override
    public void onRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        InetSocketAddress remote = (InetSocketAddress) channel.receive(buf);
        String msg = AppHelper.getMsgByByteBuffer(buf);
        if(mConsumer != null){
            mConsumer.postConsume(msg,remote,channel);
        }
    }

    @Override
    public void appendConsumer(AbsConsumer<String, Object> consumer) {
        if (mConsumer != null) {
            mConsumer.append(consumer);
        }else{
            mConsumer = consumer;
        }
    }
}
