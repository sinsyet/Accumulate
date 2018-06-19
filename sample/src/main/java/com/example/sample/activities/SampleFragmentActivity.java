package com.example.sample.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sample.R;
import com.example.sample.adapters.FragmentRecyAdapter;
import com.example.sample.fragments.SoundFragment;
import com.example.sample.fragments.ThreadFragment;

import java.util.ArrayList;
import java.util.List;

public class SampleFragmentActivity extends AppCompatActivity {

    private RecyclerView mRecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_fragment);

        mRecy = findViewById(R.id.sample_fragment_rcyv);

        initRecyVAdapter();
    }

    private void initRecyVAdapter(){
        List<Class<? extends Fragment>> fragmentList = new ArrayList<>();
        fragmentList.add(SoundFragment.class);
        fragmentList.add(ThreadFragment.class);
        mRecy.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        mRecy.setAdapter(new FragmentRecyAdapter(fragmentList));
    }

}
