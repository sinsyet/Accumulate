package com.example.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.apphelper.AppHelper;
import com.example.apphelper.ToastUtil;
import com.example.dblib.DBManager;
import com.example.dblib.bean.FilePath;
import com.example.dblib.greendao.DaoSession;
import com.example.dblib.greendao.FilePathDao;
import com.example.sample.R;
import com.example.sample.tasks.ScanSDTask;
import com.example.sample.values.Constants;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBActivity extends AppCompatActivity implements View.OnClickListener {

    private ScanSDTask mTask;

    private List<FilePath> mFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        findView();

        mTask = new ScanSDTask(getApplicationContext(), Constants.DB_NAME);
    }

    private void findView(){
        findViewById(R.id.db_btn_scan).setOnClickListener(this);
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

    private void loadFiles(){
        DaoSession daoSession = DBManager.getDaoSession(getApplicationContext(), Constants.DB_NAME);
        Query<FilePath> query = daoSession.getFilePathDao()
                .queryBuilder()
                .orderDesc(FilePathDao.Properties.IsDir)
                .orderDesc(FilePathDao.Properties.Name).build();
        List<FilePath> list = query.list();
        mFiles.addAll(list);
    }
}
