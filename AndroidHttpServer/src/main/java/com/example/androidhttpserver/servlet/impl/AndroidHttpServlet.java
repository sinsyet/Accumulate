package com.example.androidhttpserver.servlet.impl;


import android.content.Context;
import android.content.res.AssetManager;

import com.example.androidhttpserver.servlet.base.IAndroidServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    protected void writeFile2OutputStream(String assetsPath, OutputStream os)
        throws IOException
    {
        InputStream is = null;
        try {
            is = getAssetManager().open(assetsPath);
            byte buf[] = new byte[1024];
            int len = 0;
            while ((len = is.read(buf))!=-1){
                os.write(buf,0,len);
                os.flush();
            }
        }catch (Exception e){
            throw e;
        }finally {
            if (is != null) {
                try{
                    is.close();
                }catch (Exception ignored){
                }
            }
        }
    }
}
