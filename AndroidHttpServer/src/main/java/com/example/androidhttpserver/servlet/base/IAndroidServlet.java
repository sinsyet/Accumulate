package com.example.androidhttpserver.servlet.base;


import android.content.Context;

public interface IAndroidServlet {

    /**
     *  获取上下文
     */
    Context getContext();

    void doRequest(IAndroidServletRequest req,IAndroidServletResponse resp);

}
