package com.example.zzw.servlets;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import java.io.IOException;


public class MainServlet extends AndroidHttpServlet {
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        try {
            writeFile2OutputStream("web/html/frameset/main.html",resp.getOutputStream());
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
}
