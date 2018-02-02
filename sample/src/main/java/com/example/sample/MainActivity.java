package com.example.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sample.activities.SampleActivity;
import com.example.sample.activities.SampleFragmentActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_btn_aty).setOnClickListener(this);
        findViewById(R.id.main_btn_fragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_aty:
                startActivity(new Intent(this,SampleActivity.class));
                break;
            case R.id.main_btn_fragment:
                startActivity(new Intent(this, SampleFragmentActivity.class));
                break;
        }
    }
}
