package com.example.sample.async.initer;

import android.os.SystemClock;


/**
 * 获取楼栋列表
 */
public class GetBuildListIniter extends AbsIniter {
    @Override
    protected void onHandleEvent() {
        SystemClock.sleep(5 * 1000);
        onNext();
    }

}
