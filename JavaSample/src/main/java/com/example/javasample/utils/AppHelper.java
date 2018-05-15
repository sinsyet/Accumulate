package com.example.javasample.utils;

import java.nio.ByteBuffer;

public class AppHelper {
    public static String getMsgByByteBuffer(ByteBuffer buf) {
        byte[] array = buf.array();
        int offset = buf.arrayOffset();
        int length = buf.position();
        buf.clear();
        return new String(array, offset, length);
    }
}
