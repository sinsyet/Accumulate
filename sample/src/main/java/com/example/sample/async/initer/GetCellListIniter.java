package com.example.sample.async.initer;

import android.os.SystemClock;


/**
 * 获取单元列表
 */
public class GetCellListIniter extends AbsIniter {
    @Override
    protected void onHandleEvent() {
        SystemClock.sleep(7 * 1000);

        onNext();
    }
}
