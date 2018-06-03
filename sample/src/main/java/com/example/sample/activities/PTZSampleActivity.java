package com.example.sample.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.ecloud.pulltozoomview.sample.PullToZoomListActivity;
import com.example.sample.R;

public class PTZSampleActivity extends AppCompatActivity {
    private PullToZoomListViewEx listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptzsample);
        ActionBar bar = getSupportActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (bar != null) {
            bar.hide();
        }
        listView = (PullToZoomListViewEx) findViewById(R.id.sample_lv);

        String[] adapterData = new String[]{"Activity", "Service", "Content Provider", "Intent", "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient",
                "DDMS", "Android Studio", "Fragment", "Loader", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient"};

        listView.setAdapter(new ArrayAdapter<String>(PTZSampleActivity.this, android.R.layout.simple_list_item_1, adapterData));
        listView.getPullRootView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("zhuwenwu", "position = " + position);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("zhuwenwu", "position = " + position);
            }
        });

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (13.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.ecloud.pulltozoomview.R.menu.list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == com.ecloud.pulltozoomview.R.id.action_normal) {
            listView.setParallax(false);
            return true;
        } else if (id == com.ecloud.pulltozoomview.R.id.action_parallax) {
            listView.setParallax(true);
            return true;
        } else if (id == com.ecloud.pulltozoomview.R.id.action_show_head) {
            listView.setHideHeader(false);
            return true;
        } else if (id == com.ecloud.pulltozoomview.R.id.action_hide_head) {
            listView.setHideHeader(true);
            return true;
        } else if (id == com.ecloud.pulltozoomview.R.id.action_disable_zoom) {
            listView.setZoomEnabled(false);
            return true;
        } else if (id == com.ecloud.pulltozoomview.R.id.action_enable_zoom) {
            listView.setZoomEnabled(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
