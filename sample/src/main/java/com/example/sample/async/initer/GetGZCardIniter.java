package com.example.sample.async.initer;


import android.os.SystemClock;
import android.util.Log;

import com.example.apphelper.AppHelper;

public class GetGZCardIniter extends AbsIniter {
    private static final String TAG = "GetGZCardIniter";
    @Override
    protected void onInit() {
        setInitType(TYPE_PROGRESS);
        long value = (AppHelper.randomInt(5) + 5) * 1000;
        long start = System.currentTimeMillis();
        long cur = 0;
        Log.e(TAG, "onInit: "+value);
        while (start + value > (cur = System.currentTimeMillis())) {
            SystemClock.sleep(AppHelper.randomInt(400) + 100);
            onProgress(value,cur-start);
            if(!isCanContinute()){
                onNext();
                return;
            }
        }
        Log.e(TAG, "onInit: "+(cur-start));
        onProgress(value,cur-start);
        onNext();
    }
}
