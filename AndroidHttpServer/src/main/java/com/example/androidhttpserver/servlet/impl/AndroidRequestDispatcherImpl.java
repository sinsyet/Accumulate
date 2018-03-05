package com.example.androidhttpserver.servlet.impl;

import com.example.androidhttpserver.servlet.IRequestDispatcher;
import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;


public class AndroidRequestDispatcherImpl implements IRequestDispatcher {

    private final String targetUrl;
    private AndroidHttpServer androidHttpServer;

    AndroidRequestDispatcherImpl(String targetUrl){
        this.targetUrl = targetUrl;
    }

    void injectServer(AndroidHttpServer server){
        androidHttpServer = server;
    }


    @Override
    public void forward(IAndroidServletRequest req, IAndroidServletResponse resp) {
        if(!(resp instanceof AndroidServletResponseImpl)){
            throw new IllegalStateException("resp must be instance of AndroidServletResponseImpl");
        }

        AndroidServletResponseImpl androidServletResponse = (AndroidServletResponseImpl) resp;
        // 清空写出的数据
        androidServletResponse.clearOutputStream();
        androidHttpServer.onDispatch(targetUrl,req,resp);
    }

    @Override
    public void include(IAndroidServletRequest req, IAndroidServletResponse resp) {
        androidHttpServer.onDispatch(targetUrl,req,resp);
    }
}
