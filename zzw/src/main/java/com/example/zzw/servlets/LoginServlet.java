package com.example.zzw.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.http.HttpStatus;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;



public class LoginServlet extends AndroidHttpServlet {

    private static final String TAG = "LoginServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        String usn = req.getParamter("usn");
        String psw = req.getParamter("psw");
        Log.e(TAG, "doRequest: "+usn+" // "+psw);
        if("admin".equals(usn) && "pass".equals(psw)){
            resp.sendRedirect("./main");
            resp.setStatus(HttpStatus.REDIRECT);
        }else{
            resp.sendRedirect("./web/html/login_fail.html");
        }
    }
}
