package com.example.javasample.consumer.packet;

import com.example.javasample.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Locale;

public class Packet_T1_Consumer extends PacketConsumerWarpper {
    private String mResp = "{\"t\":%d,\"mid\":%d,\"msg\":{" +
            "\"host\":\"%s\",\"port\":%d"
            + "}}";
    private static final String TAG = "Packet_T1_Consumer";
    @Override
    protected boolean onHandlePacket(PacketMsg msg) {
        if (msg.getT() != 1) {
            return false;
        }

        InetSocketAddress remote = msg.getRemote();
        String hostAddress = remote.getAddress().getHostAddress();
        int port = remote.getPort();
        String resp = String.format(Locale.CHINA, mResp, -msg.getT(), msg.getMid(), hostAddress, port);
        try {
            msg.getChannel().send(ByteBuffer.wrap(resp.getBytes("UTF-8")),remote);
        } catch (IOException e) {
            Log.e(TAG,"onHandlePacket: "+e.getMessage());
        }
        return true;
    }
}
