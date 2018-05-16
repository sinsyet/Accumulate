package com.example.javasample;

import com.example.javasample.channel.IChannelHandler;
import com.example.javasample.channel.UdpChannelHandlerWrapper;
import com.example.javasample.consumer.packet.Packet_T1_Consumer;
import com.example.javasample.utils.AppHelper;
import com.example.javasample.utils.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class UdpServer {
    private static final String TAG = "UdpServer";
    private DatagramChannel mChannel;
    private Selector mSelector;
    private IChannelHandler<String, Object> udpHandler;

    public UdpServer(int port) throws IOException {
        DatagramChannel channel = null;
        try {
            channel = initChannel(port);

            Selector selector = Selector.open();

            channel.register(selector, SelectionKey.OP_READ);
            mChannel = channel;
            mSelector = selector;
            Log.e(TAG, "UdpServer: create success");
        } catch (IOException e) {
            if(channel != null){
                try {
                    channel.close();
                } catch (IOException e1) {
                }
            }
            Log.e(TAG, "UdpServer: create fail");
            throw e;
        }
    }

    private DatagramChannel initChannel(int port) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        return channel;
    }

    public void start(){
        udpHandler = new UdpChannelHandlerWrapper();
        udpHandler.appendConsumer(new Packet_T1_Consumer());
        AppHelper.runOnPool(mServerLoop);
    }
    public void stop(){
        loopFlag = false;
        if(mChannel != null){
            try {
                mChannel.close();
            } catch (IOException e) {

            }
        }

        if(mSelector != null){
            try {
                mSelector.close();
            } catch (IOException e) {
            }
        }
    }

    private boolean loopFlag = false;
    private Runnable mServerLoop = new Runnable() {
        @Override
        public void run() {
            loopFlag = true;
            Log.e(TAG, "run: server loop start ...");
            while (loopFlag){
                try {
                    int select = mSelector.select(1000);
                    if (select <= 0) {
                        continue;
                    }

                    Set<SelectionKey> keys = mSelector.selectedKeys();
                    for (SelectionKey key : keys) {
                        if (udpHandler != null) {
                            udpHandler.onSelect(key);
                        }
                    }
                    keys.clear();
                } catch (IOException e) {

                }
            }
        }
    };

}
