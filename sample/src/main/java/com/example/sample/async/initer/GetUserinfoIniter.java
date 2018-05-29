package com.example.sample.async.initer;

import android.os.SystemClock;


/**
 * 获取住户数据
 */
public class GetUserinfoIniter extends AbsIniter {
    @Override
    protected void onHandleEvent() {

        SystemClock.sleep(5 * 1000);

        onNext();
    }
}
