package com.example.sample.initers;

import android.os.SystemClock;

import com.example.appbase.initer.AbsIniter;
import com.example.apphelper.AppHelper;


public class GetFaceIniter extends AbsIniter {
    @Override
    protected void onHandleInit() {
        AppHelper.run(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5 * 1000);
                onNext();
            }
        });
    }

    @Override
    public Object onStartHandleInit() {
        return "开始获取人脸数据";
    }
}
