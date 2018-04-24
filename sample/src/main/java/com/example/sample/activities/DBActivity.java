package com.example.sample.activities;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.dblib.DBManager;
import com.example.dblib.bean.FilePath;
import com.example.dblib.greendao.DaoSession;
import com.example.dblib.greendao.FilePathDao;
import com.example.sample.R;
import com.example.sample.adapters.FilePathAdapter;
import com.example.sample.tasks.ScanSDTask;
import com.example.sample.values.Constants;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBActivity extends AppCompatActivity implements View.OnClickListener, ScanSDTask.Observer, AdapterView.OnItemClickListener {

    private ScanSDTask mTask;

    private List<FilePath> mFiles = new ArrayList<>();
    private FilePathAdapter filePathAdapter;
    private ListView mLv;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        findView();
        initAdapter();
        mTask = new ScanSDTask(getApplicationContext(), Constants.DB_NAME);

        mTask.setObserver(this);
    }

    private void initAdapter(){
        filePathAdapter = new FilePathAdapter(mFiles);
        mLv.setAdapter(filePathAdapter);
        mLv.setOnItemClickListener(this);

        AppHelper.run(new Runnable() {
            @Override
            public void run() {
                loadFiles(Environment.getExternalStorageDirectory().getAbsolutePath());
            }
        });
    }

    private void findView(){
        findViewById(R.id.db_btn_scan).setOnClickListener(this);
        mLv = findViewById(R.id.db_lv_files);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.db_btn_scan:
                if(mTask.isRunning())
                {
                    ToastUtil.show(getApplicationContext(),R.string.op_often);
                    return;
                }
                AppHelper.run(mTask);
                break;
        }
    }

    private static final String TAG = "DBActivity";
    private void loadFiles(String parentPath){
        DaoSession daoSession = DBManager.getDaoSession(getApplicationContext(), Constants.DB_NAME);
        Query<FilePath> query = daoSession.getFilePathDao()
                .queryBuilder()
                .where(FilePathDao.Properties.ParentalPath.eq(parentPath))
                .orderDesc(FilePathDao.Properties.IsDir,FilePathDao.Properties.Name)
                /*.orderDesc(FilePathDao.Properties.Name)*/.build();
        List<FilePath> list = query.list();
        mFiles.clear();
        mFiles.addAll(list);
        Log.e(TAG, "loadFiles: "+mFiles.size());
        mHandler.post(()->{
           filePathAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onLoadFinish() {
        AppHelper.run(new Runnable() {
            @Override
            public void run() {
                loadFiles(Environment.getExternalStorageDirectory().getAbsolutePath());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final FilePath filePath = mFiles.get(position);
        if(filePath.getIsDir() == FilePath.TYPE_DIR){
            AppHelper.run(new Runnable() {
                @Override
                public void run() {
                    loadFiles(filePath.getPath());
                }
            });
        }else{
            if(filePath.getName().endsWith(".png") || filePath.getName().endsWith(".jpg")){
                Intent intent = new Intent(this,PreviewActivity.class);
                intent.putExtra("type",PreviewActivity.PHOTO);
                intent.putExtra("path",filePath.getPath());
                startActivity(intent);
            }
        }
    }
}
