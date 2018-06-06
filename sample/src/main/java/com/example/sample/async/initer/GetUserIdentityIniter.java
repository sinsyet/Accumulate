package com.example.sample.async.initer;


import android.os.SystemClock;

import com.example.apphelper.AppHelper;

/**
 * 获取住户身份
 */
public class GetUserIdentityIniter extends AbsIniter {
    @Override
    public Object getHintExtra() {
        return "获取住户身份信息列表...";
    }

    @Override
    protected void onInit() {
        setInitType(TYPE_PROGRESS);
        long value = (AppHelper.randomInt(5) + 5) * 1000;
        long start = System.currentTimeMillis();
        long cur = 0;
        while (start + value > (cur = System.currentTimeMillis())) {
            SystemClock.sleep(AppHelper.randomInt(400) + 100);
            onProgress(value,cur-start);
            if(!isCanContinute()){
                onNext();
                return;
            }
        }
        onProgress(value,cur-start);
        onNext();
    }
}
