package com.example.sample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appbase.activities.BaseLoadActivity;

import java.util.zip.Deflater;

public class LoadActivity extends BaseLoadActivity {
    final private Handler mHandler = new Handler();
    private int type;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            switch (type) {
                case 0:
                    onLoadEmpty();
                    break;
                case 1:
                    onLoadException();
                    break;
                case 2:
                    onLoadFail();
                    break;
                case 3:
                    onLoadSuccess();
                    break;
                default:
                    onLoadException();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        handleIntent();
    }

    private void handleIntent() {
        final Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        type = intent.getIntExtra("type", 0);

        mHandler.postDelayed(r, 3 * 1000);
    }

    @Override
    public void onReLoadClick(int reloadType) {
        onLoading();
        type++;
        mHandler.postDelayed(r, 3 * 1000);
    }

    @Override
    public void onNetChanged(boolean connect) {
        Toast.makeText(this, connect ? "网络已连接" : "网络已断开", Toast.LENGTH_LONG).show();
    }
}
