package com.example.sample.async.initer;

import android.os.SystemClock;

import com.example.apphelper.AppHelper;


/**
 * 获取设备信息
 */
public class GetDeviceInfoIniter extends AbsIniter {

    @Override
    protected void onInit() {

        long value = (AppHelper.randomInt(5) + 5) * 1000;
        long start = System.currentTimeMillis();
        long cur = 0;
        while (start + value > (cur = System.currentTimeMillis())) {
            SystemClock.sleep(AppHelper.randomInt(400) + 100);
            onProgress(value,cur-start);
        }
        onProgress(value,cur-start);
        onNext();

    }
}
