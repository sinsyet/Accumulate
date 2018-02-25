package com.example.androidhttpserver.servlet.impl;


import android.content.Context;
import android.content.res.AssetManager;

import com.example.androidhttpserver.servlet.base.IAndroidServlet;

public abstract class AndroidHttpServlet implements IAndroidServlet{

    private Context ctx;
    private AssetManager assetManager;

    public void injectContext(Context ctx){
        this.ctx = ctx;
        assetManager = ctx.getAssets();
    }

    @Override
    public Context getContext() {
        return ctx;
    }

    protected AssetManager getAssetManager(){
        return assetManager;
    }
}
