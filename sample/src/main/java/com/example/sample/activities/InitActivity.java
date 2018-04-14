package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.appbase.initer.AbsIniter;
import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.sample.R;
import com.example.sample.initers.GetCardsIniter;
import com.example.sample.initers.GetFaceIniter;
import com.example.sample.initers.GetPropertyIniter;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        findViews();
    }

    private void findViews(){
        findViewById(R.id.init_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init_btn:
                startInit();
                break;
        }
    }

    private boolean isIniting;

    private void startInit(){
        if(isIniting) return;

        GetPropertyIniter initer = new GetPropertyIniter();
        initer.appendIniter(new GetCardsIniter())
                .appendIniter(new GetFaceIniter());

        initer.startInit(new AbsIniter.InitObserver() {
            @Override
            public void onStartInit() {
                Log.e(TAG, "onStartInit: ");
                isIniting = true;
                ToastUtil.show(getApplicationContext(),"开始初始化, 请稍后...");
            }

            @Override
            public void onInitProgress(AbsIniter curIniter, AbsIniter nextIniter) {
                ToastUtil.show(getApplicationContext(),
                        String.valueOf(nextIniter.onStartHandleInit()));
                Log.e(TAG, "onInitProgress: " +
                        "curIniter: "+curIniter.getClass().getSimpleName() +
                        "nextIniter: "+nextIniter.getClass().getSimpleName()
                );
            }

            @Override
            public void onInitException(AbsIniter curIniter, Throwable t) {
                ToastUtil.show(getApplicationContext(),"初始化异常");
                Log.e(TAG, "onInitException: "+curIniter.getClass().getSimpleName());
                t.printStackTrace();
            }

            @Override
            public void onInitFail(AbsIniter curIniter, int errorCode, Object extra) {
                ToastUtil.show(getApplicationContext(),"初始化失败");
                Log.e(TAG, "onInitFail: "+curIniter.getClass().getSimpleName()
                        + " // errorCode: "+errorCode + " // extra: "+extra);
            }

            @Override
            public void onEndInit(boolean available) {
                isIniting = false;
                ToastUtil.show(getApplicationContext(),"初始化结束: "+(available ? "成功":"失败"));
                Log.e(TAG, "onEndInit: "+available);
            }
        });
    }
}
