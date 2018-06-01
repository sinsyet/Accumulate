package com.example.samplemodule;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samplemodule.adapters.AtysAdapter;
import com.example.samplemodule.atys.LoginActivity;
import com.example.samplemodule.atys.Main2Activity;
import com.example.samplemodule.atys.Main3Activity;
import com.example.samplemodule.atys.Main4Activity;
import com.example.samplemodule.atys.MainActivity;
import com.example.samplemodule.atys.MaterialViewPagerActivity;
import com.example.samplemodule.atys.SettingsActivity;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity {

    private RecyclerView mRcyv;
    private ArrayList<Class<? extends Activity>> mAtys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViews();
    }

    void findViews() {
        mRcyv = findViewById(R.id.sample_rcyv_aty);
        mAtys = new ArrayList<>();
        mAtys.add(SettingsActivity.class);
        mAtys.add(MainActivity.class);
        mAtys.add(Main2Activity.class);
        mAtys.add(Main3Activity.class);
        mAtys.add(Main4Activity.class);
        mAtys.add(LoginActivity.class);
        mAtys.add(MaterialViewPagerActivity.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRcyv.setLayoutManager(linearLayoutManager);
        mRcyv.setAdapter(new AtysAdapter(mAtys));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
