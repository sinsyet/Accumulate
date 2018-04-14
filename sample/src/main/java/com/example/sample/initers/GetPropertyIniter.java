package com.example.sample.initers;

import android.os.SystemClock;

import com.example.appbase.initer.AbsIniter;
import com.example.apphelper.AppHelper;


public class GetPropertyIniter extends AbsIniter {

    @Override
    public Object onStartHandleInit() {
        return "开始获取属性";
    }

    @Override
    protected void onHandleInit() {

        AppHelper.run(() -> {
            SystemClock.sleep(1000 * 3);
            onNext();
        });
    }
}
