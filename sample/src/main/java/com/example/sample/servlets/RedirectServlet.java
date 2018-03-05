package com.example.sample.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.http.HttpStatus;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;


public class RedirectServlet extends AndroidHttpServlet {

    private static final String TAG = "RedirectServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        Log.e(TAG, "doRequest: ");
        resp.setStatus(HttpStatus.REDIRECT302);
        resp.sendRedirect("./main");

    }
}
