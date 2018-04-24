package com.example.sample.tasks;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.dblib.DBManager;
import com.example.dblib.bean.FilePath;
import com.example.dblib.greendao.DaoSession;
import com.example.dblib.greendao.FilePathDao;

import java.io.File;

public class ScanSDTask implements Runnable {
    private static final String TAG = "ScanSDTask";
    private DaoSession daoSession;
    private FilePathDao filePathDao;

    public ScanSDTask(Context ctx, String name) {
        daoSession = DBManager.getDaoSession(ctx,name);
    }

    @Override
    public void run() {
        File file = Environment.getExternalStorageDirectory();
        filePathDao = daoSession.getFilePathDao();
        long start  = System.currentTimeMillis();
        isRunning = true;
        daoSession.clear();
        filePathDao.deleteAll();
        Log.e(TAG, "run: "+file.getAbsolutePath()+" // start");
        traverse(file);

        isRunning = false;
        long end  = System.currentTimeMillis();
        Log.e(TAG, "run: "+(end-start));
    }

    private void traverse(File file) {
        String parentPath = file.getAbsolutePath();
        File[] files = file.listFiles();


        for (File f : files) {
            boolean directory = f.isDirectory();
            long l_m_d = 0;

            String name = f.getName();
            String path = f.getAbsolutePath();
            FilePath entity = new FilePath(name, path, parentPath, directory ? FilePath.TYPE_DIR : FilePath.TYPE_FILE, l_m_d);
            filePathDao.insert(entity);
            Log.e(TAG, "traverse: "+entity.toString());
            if (f.isDirectory()) {
                traverse(f);
            }
        }
    }

    private boolean isRunning;
    public boolean isRunning(){
        return isRunning;
    }

}
