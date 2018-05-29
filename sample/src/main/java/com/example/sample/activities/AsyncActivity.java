package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.appbase.async.AsyncEventSession;
import com.example.sample.R;

public class AsyncActivity extends AppCompatActivity implements View.OnClickListener {

    private AsyncEventSession initSession;
    private View mBtnInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);

        findView();
    }

    void findView() {
        mBtnInit = findViewById(R.id.async_btn_init);
        mBtnInit.setOnClickListener(this);
        findViewById(R.id.async_btn_interrupt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.async_btn_init:
                // test_init();
                break;
            case R.id.async_btn_interrupt:
                if (initSession != null) {
                    initSession.interruptSession();
                }
                break;
        }
    }


    private static final String TAG = "AsyncActivity";
}
