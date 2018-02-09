package com.example.sample.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.sample.LoadActivity;
import com.example.sample.R;
import com.example.sample.adapters.SampleAtyAdapter;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView mGv;

    private List<Class<? extends Activity>> mAtys = new ArrayList<>();
    private SampleAtyAdapter adapter;

    {
        mAtys.add(LoadActivity.class);
        mAtys.add(GraphActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mGv = (GridView) findViewById(R.id.sample_gv);

        adapter = new SampleAtyAdapter(mAtys);
        mGv.setNumColumns(3);
        mGv.setAdapter(adapter);
        mGv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this,mAtys.get(position)));
    }
}
