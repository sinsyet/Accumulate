package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.sample.R;
import com.example.sample.tasks.ScanSDTask;
import com.example.sample.values.Constants;

public class DBActivity extends AppCompatActivity implements View.OnClickListener {

    private ScanSDTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        findView();

        mTask = new ScanSDTask(getApplicationContext(), Constants.DB_NAME);
    }

    private void findView(){
        findViewById(R.id.db_btn_scan).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.db_btn_scan:
                if(mTask.isRunning())
                {
                    ToastUtil.show(getApplicationContext(),R.string.op_often);
                    return;
                }
                AppHelper.run(mTask);
                break;
        }
    }
}
