package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.sample.R;

public class ZZWInitActivity extends AppCompatActivity {

    private RecyclerView mRecyv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zzwinit);

        findViews();
    }

    void findViews(){
        mRecyv = findViewById(R.id.zzwinit_recyv);
    }
}
