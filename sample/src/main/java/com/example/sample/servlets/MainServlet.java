package com.example.sample.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import java.io.IOException;
import java.io.OutputStream;


public class MainServlet extends AndroidHttpServlet {
    private static final String TAG = "MainServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        Log.e(TAG, "doRequest: ");
        OutputStream os = resp.getOutputStream();
        try {
            os.write("this is page main".getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {

        }
    }
}
