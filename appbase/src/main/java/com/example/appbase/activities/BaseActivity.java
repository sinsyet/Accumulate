package com.example.appbase.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.demo.receivers.NetStatusBroadcast;


public class BaseActivity extends AppCompatActivity implements NetStatusBroadcast.Callback {

    private static final String TAG = "BaseActivity";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        NetStatusBroadcast.registerNoRepeat();
        NetStatusBroadcast.subscribe(this);
    }

    @Override
    public void onNetChanged(boolean connect) {
        // empty, should implememts by child instanc
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStatusBroadcast.unSubscribe(this);
    }
}
