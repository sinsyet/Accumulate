package com.example.sample.async.initer;

import android.os.SystemClock;


/**
 * 获取设备信息
 */
public class GetDeviceInfoIniter extends AbsIniter {

    @Override
    protected void onHandleEvent() {

        SystemClock.sleep(5 * 1000);

        onNext();

    }
}
