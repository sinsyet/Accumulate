package com.example.sample.servlets;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.http.HttpStatus;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginServlet extends AndroidHttpServlet {
    private static final String TAG = "LoginServlet";
    private static final String USN = "admin";
    private static final String PSW = "pass";
    private static final String LOGIN_FAIL = "html/login_fail.html";
    private static final String LOGIN_SUCCESS = "html/frameset/main.html";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        String usn = req.getParamter("usn");
        String psw = req.getParamter("psw");

        resp.setStatus(HttpStatus.OK);
        resp.setContentType("text/html");
        if(USN.equals(usn) && PSW.equals(psw))
        {
            write(LOGIN_SUCCESS,resp.getOutputStream());
        }else
            write(LOGIN_FAIL,resp.getOutputStream());

    }

    private void write(String filePath,OutputStream os){
        try {
            InputStream is = getAssetManager().open(filePath);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf))!= - 1){
                os.write(buf,0,len);
                os.flush();
            }
            is.close();
        } catch (IOException e) {

        }
    }
}
