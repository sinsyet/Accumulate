package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sample.R;

/**
 * 模仿抖音视频播放页面
 *
 * 要点:
 *  1. 状态栏透明, 标题栏去掉
 *  2. 视频可切换;
 */
public class DouYinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_yin);
    }
}
