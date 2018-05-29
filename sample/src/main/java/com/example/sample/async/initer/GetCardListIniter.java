package com.example.sample.async.initer;

import android.os.SystemClock;


/**
 * 获取卡列表
 */
public class GetCardListIniter extends AbsIniter {
    @Override
    protected void onHandleEvent() {
        SystemClock.sleep(5 * 1000);
        onNext();
    }
}
