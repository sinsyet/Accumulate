package com.example.sample;

import android.app.Application;
import android.content.Intent;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(getApplicationContext(),ShellService.class));
    }
}
