package com.example.androidhttpserver;


import android.content.Context;
import android.content.res.AssetManager;

import com.example.androidhttpserver.servlet.base.IAndroidServlet;

public abstract class AndroidHttpServlet implements IAndroidServlet{

    private Context ctx;
    private AssetManager assetManager;

    void injectContext(Context ctx){
        this.ctx = ctx;
        assetManager = ctx.getAssets();
    }

    @Override
    public Context getContext() {
        return ctx;
    }

    @Override
    public void init() {

    }

    @Override
    public void destory() {

    }

    protected AssetManager getAssetManager(){
        return assetManager;
    }
}
