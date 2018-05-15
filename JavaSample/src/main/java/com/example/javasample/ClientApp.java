package com.example.javasample;

import com.example.javasample.utils.AppHelper;
import com.example.javasample.utils.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Locale;

public class ClientApp {
    private static final String TAG = "ClientApp";
    private static boolean recvFlag;
    public static void main(String args[]) throws IOException {
        final DatagramSocket udp = new DatagramSocket();
        AppHelper.runOnPool(new Runnable() {
            @Override
            public void run() {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
                recvFlag = true;
                while (recvFlag) {
                    try {
                        udp.receive(packet);
                        byte[] data = packet.getData();
                        int len = packet.getLength();
                        int offset = packet.getOffset();
                        Log.e(TAG,"recv client: "+new String(data,offset,len));
                    } catch (IOException e) {

                    }
                }
            }
        });

        String req = "{\"t\":1,\"mid\":%d}";
        String reqMsg = String.format(Locale.CHINA, req, System.currentTimeMillis());
        Log.e(TAG, "main: send: "+reqMsg);
        byte[] reqMsgBytes = reqMsg.getBytes("UTF-8");
        DatagramPacket packet = new DatagramPacket(
                reqMsgBytes, 0, reqMsgBytes.length, new InetSocketAddress("127.0.0.1", 12200));
        udp.send(packet);
        recvFlag =false;
    }
}
