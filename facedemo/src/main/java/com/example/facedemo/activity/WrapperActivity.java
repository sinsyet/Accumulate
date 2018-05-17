package com.example.facedemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.facedemo.R;

public class WrapperActivity extends AppCompatActivity {
    private static final String TAG = "WrapperActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);
        Log.e(TAG, "onCreate: ");
    }
}
