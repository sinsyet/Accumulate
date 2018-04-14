package com.example.sample.initers;

import android.os.SystemClock;

import com.example.appbase.initer.AbsIniter;
import com.example.apphelper.AppHelper;


public class GetCardsIniter extends AbsIniter {

    @Override
    public Object onStartHandleInit() {
        return "开始获取卡片数据";
    }

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
}
