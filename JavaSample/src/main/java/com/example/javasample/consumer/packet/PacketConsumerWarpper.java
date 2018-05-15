package com.example.javasample.consumer.packet;

import com.example.javasample.utils.Log;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PacketConsumerWarpper extends AbsConsumer<String, Object> {
    private static final String TAG = "PacketConsumerWarpper";
    private String[] mRegexs = {
            "\"t\":[\\d]{1,}",
            "\"mid\":[\\d]{1,}",
    };
    Pattern p[];

    private static final int INVALID = 0;

    public PacketConsumerWarpper() {
        p = new Pattern[mRegexs.length];
        for (int i = 0; i < p.length; i++) {
            p[i] = Pattern.compile(mRegexs[i]);
        }
    }

    private static final String _DEFAULT_RESP = "{\"t\":0,\"msg\":\"%s\"}";
    private PacketMsg mPacket;

    @Override
    protected boolean handleConsume(String param, Object... params) {
        Log.e(TAG, "handleConsume: "+param);
        int t = getT(param);
        InetSocketAddress remote = null;
        DatagramChannel channel = null;
        try{
            remote = (InetSocketAddress) params[0];
        }catch (Exception e){
            Log.e(TAG,"params[0] should be instance of remote InetSocketAddress");
            return false;
        }

        try{
            channel = (DatagramChannel) params[1];
        }catch (Exception e){
            Log.e(TAG,"params[1] should be instance of DatagrameChannel");
            return false;
        }

        if(t == 0){
            try {
                channel.send(
                        ByteBuffer.wrap(
                                String.format(
                                        Locale.CHINA,_DEFAULT_RESP,
                                        "value 't' is invalid")
                                        .getBytes("UTF-8")),
                        remote);
            } catch (Exception ignored) {
                Log.e(TAG,"send resp fail: "+ignored.getMessage());
            }
            return true;
        }

        long mid = getMid(param);
        if(mid == -1){
            try {
                channel.send(
                        ByteBuffer.wrap(
                                String.format(
                                        Locale.CHINA,_DEFAULT_RESP,
                                        "value 'mid' is invalid")
                                        .getBytes("UTF-8")),
                        remote);
            } catch (Exception ignored) {
                Log.e(TAG,"send resp fail: "+ignored.getMessage());
            }
            return true;
        }
        if(mPacket == null){
            mPacket = new PacketMsg(t,mid,param,remote,channel);
        }else{
            mPacket.setT(t);
            mPacket.setMid(mid);
            mPacket.setMsg(param);
            mPacket.setRemote(remote);
            mPacket.setChannel(channel);
        }
        return onHandlePacket(mPacket);
    }

    protected abstract boolean onHandlePacket(PacketMsg msg);

    private int getT(String s){
        Matcher matcher = p[0].matcher(s);
        if (!matcher.find()) {
            return 0;
        }
        String group = matcher.group();
        String t_str = group.replace("\"t\":", "");
        int t = 0;
        try{
            t = Integer.parseInt(t_str.trim());
        }catch (Exception e){
        }
        return t;
    }

    private long getMid(String s){
        Matcher matcher = p[1].matcher(s);
        if(!matcher.find()) return -1;
        String group = matcher.group();
        String mid_str = group.replace("\"mid\":", "");
        long mid = -1;
        try{
            mid = Long.parseLong(mid_str.trim());
        }catch (Exception e){
        }
        return mid;
    }

    protected static class PacketMsg{
        private int t;
        private long mid;
        private String msg;
        private InetSocketAddress remote;
        private DatagramChannel channel;

        public PacketMsg(){}

        public PacketMsg(int t, long mid, String msg, InetSocketAddress remote, DatagramChannel channel) {
            this.t = t;
            this.mid = mid;
            this.msg = msg;
            this.remote = remote;
            this.channel = channel;
        }

        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public long getMid() {
            return mid;
        }

        public void setMid(long mid) {
            this.mid = mid;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public InetSocketAddress getRemote() {
            return remote;
        }

        public void setRemote(InetSocketAddress remote) {
            this.remote = remote;
        }

        public DatagramChannel getChannel() {
            return channel;
        }

        public void setChannel(DatagramChannel channel) {
            this.channel = channel;
        }
    }
}
