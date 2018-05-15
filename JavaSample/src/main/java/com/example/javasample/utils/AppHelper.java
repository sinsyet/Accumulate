package com.example.javasample.utils;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppHelper {
    public static String getMsgByByteBuffer(ByteBuffer buf) {
        byte[] array = buf.array();
        int offset = buf.arrayOffset();
        int length = buf.position();
        buf.clear();
        return new String(array, offset, length);
    }

    static ExecutorService pool = Executors.newCachedThreadPool();
    public static void runOnPool(Runnable r){
        if(r == null) return;
        pool.execute(r);
    }
}
