package com.example.sample.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.apphelper.AppHelper;
import com.example.sample.tasks.ScanSDTask;
import com.example.sample.values.Constants;

public class ScanService extends Service {
    public ScanService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
