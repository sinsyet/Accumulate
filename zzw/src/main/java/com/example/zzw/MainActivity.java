package com.example.zzw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidhttpserver.HttpServer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpServer.init(getApplicationContext(),9999,"web/webinfo.xml");
    }
}
