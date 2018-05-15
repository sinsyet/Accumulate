package com.example.javasample.consumer.packet;

import java.util.regex.Pattern;

public abstract class PacketConsumerWarpper extends AbsConsumer<String, Object> {
    private String[] mRegexs = {
            "\"t\":[\\d]{1,}",
            "\"mid\":[\\d]{1,}"
    };
    Pattern p[];

    public PacketConsumerWarpper() {
        p = new Pattern[mRegexs.length];
        for (int i = 0; i < p.length; i++) {
            p[i] = Pattern.compile(mRegexs[i]);
        }
    }

    @Override
    protected boolean handleConsume(String param, Object... params) {

        return false;
    }
}
