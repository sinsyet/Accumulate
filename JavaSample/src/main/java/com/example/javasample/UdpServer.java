package com.example.javasample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class UdpServer {

    private DatagramChannel mChannel;
    private Selector mSelector;

    private UdpServer(int port) throws IOException {
        DatagramChannel channel = null;
        try {
            channel = initChannel(port);

            Selector selector = Selector.open();

            channel.register(selector, SelectionKey.OP_READ);
            mChannel = channel;
            mSelector = selector;
        } catch (IOException e) {
            if(channel != null){
                try {
                    channel.close();
                } catch (IOException e1) {
                }
            }
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

    }

    private boolean loopFlag = false;
    private Runnable mServerLoop = new Runnable() {
        @Override
        public void run() {
            loopFlag = true;
            while (loopFlag){
                try {
                    int select = mSelector.select(1000);
                    if (select <= 0) {
                        continue;
                    }

                    Set<SelectionKey> keys = mSelector.selectedKeys();
                    for (SelectionKey key : keys) {

                    }
                } catch (IOException e) {

                }
            }
        }
    };

}
