package com.example.sample.async.initer;

import android.os.SystemClock;

import com.example.apphelper.AppHelper;


/**
 * 获取单元列表
 */
public class GetCellListIniter extends AbsIniter {
    @Override
    public Object getHintExtra() {
        return "获取小区住户单元列表...";
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
