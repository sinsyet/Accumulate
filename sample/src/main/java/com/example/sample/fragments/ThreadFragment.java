package com.example.sample.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sample.R;

public class ThreadFragment extends Fragment implements View.OnClickListener {

    private Thread thread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thread,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    void findView(View v){
        v.findViewById(R.id.thread_btn_interrupt).setOnClickListener(this);
        v.findViewById(R.id.thread_btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thread_btn_interrupt:
                if (thread != null) {
                    // 调用interrupt无法中断
                    // thread.interrupt();
                    // 调用stop, android不支持;
                    thread.stop();
                }
                break;
            case R.id.thread_btn_start:
                thread = new Thread(r);
                thread.start();
                break;
        }
    }

    private static final String TAG = "ThreadFragment";
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: start "+Thread.currentThread().getName());
            while (true){
                SystemClock.sleep(1000);
                Log.e(TAG, "run: wake");
            }
        }
    };
}
