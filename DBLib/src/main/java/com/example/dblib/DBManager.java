package com.example.dblib;


import android.content.Context;
import android.text.TextUtils;

import com.example.dblib.greendao.DaoMaster;
import com.example.dblib.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.HashMap;

public class DBManager {

    private static HashMap<String,DaoSession> sMap = new HashMap<>();

    public static DaoSession getDaoSession(Context ctx, String name){

        if(ctx == null || TextUtils.isEmpty(name)) throw new IllegalArgumentException();

        if(sMap.containsKey(name)) return sMap.get(name);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, name);
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

        if(daoSession != null){
            sMap.put(name,daoSession);
        }

        return daoSession;
    }
}
