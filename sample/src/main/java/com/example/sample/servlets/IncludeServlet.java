package com.example.sample.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import java.io.IOException;
import java.io.OutputStream;


public class IncludeServlet extends AndroidHttpServlet {
    private static final String TAG = "IncludeServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {

        String method = req.getMethod();
        Log.e(TAG, "doRequest: method: "+method);

        OutputStream os = resp.getOutputStream();
        try {
            os.write("this is servlet include".getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("./main").include(req, resp);
    }
}
