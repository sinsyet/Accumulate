package com.example.appbase.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.apphelper.NetStatus;


public class BaseActivity extends AppCompatActivity implements NetStatus.Callback{

    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetStatus.attachContext(getApplicationContext());
        NetStatus.subscribe(this);
    }

    @Override
    public void onNetChanged(boolean connect) {
        // empty, should implememts by child instanc
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetStatus.unSubscribe(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
