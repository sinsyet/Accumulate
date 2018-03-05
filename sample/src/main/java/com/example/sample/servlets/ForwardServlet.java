package com.example.sample.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public class ForwardServlet extends AndroidHttpServlet {
    private static final String TAG = "ForwardServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        Map<String, String> paramterMap = req.getParamterMap();
        for (Map.Entry<String, String> entry : paramterMap.entrySet()) {
            Log.e(TAG, "doRequest: "+entry.getKey()+" -- "+entry.getValue());
        }

        String method = req.getMethod();
        Log.e(TAG, "doRequest: method: "+method);

        OutputStream os = resp.getOutputStream();
        try {
            os.write("this is servlet forward".getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("./main").forward(req, resp);
    }
}
