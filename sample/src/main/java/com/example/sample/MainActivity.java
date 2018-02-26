package com.example.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidhttpserver.ServerService;
import com.example.androidhttpserver.WebMappingSet;
import com.example.androidhttpserver.webinfo.WebMapping;
import com.example.sample.activities.SampleActivity;
import com.example.sample.activities.SampleFragmentActivity;
import com.example.sample.servlets.IndexServlet;
import com.example.sample.servlets.LoginServlet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_btn_aty).setOnClickListener(this);
        findViewById(R.id.main_btn_fragment).setOnClickListener(this);

        initServlet();
        startService(new Intent(this, ServerService.class));
    }

    private void initServlet(){
        /*WebMappingSet.put(WebMappingSet.INDEX,new WebMapping("/", IndexServlet.class));
        WebMappingSet.put("/login",new WebMapping("/login", LoginServlet.class));*/

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
