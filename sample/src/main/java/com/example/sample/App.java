package com.example.sample;

import android.app.Application;
import android.content.Intent;

import com.example.facedemo.Msc;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(getApplicationContext(),ShellService.class));
        Msc.init(this);
    }
}
