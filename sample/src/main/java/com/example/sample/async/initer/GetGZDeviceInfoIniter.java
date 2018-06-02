package com.example.sample.async.initer;


import android.os.SystemClock;

/**
 * 获取感知设备信息
 */
public class GetGZDeviceInfoIniter extends AbsIniter {
    @Override
    protected void onInit() {

        SystemClock.sleep(5 * 1000);

        onNext();
    }
}
