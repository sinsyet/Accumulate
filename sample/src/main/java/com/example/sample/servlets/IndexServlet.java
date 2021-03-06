package com.example.sample.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.http.Cookie;
import com.example.androidhttpserver.servlet.http.HttpStatus;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class IndexServlet extends AndroidHttpServlet {
    private static final String TAG = "IndexServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        Log.e(TAG, "doRequest: method: "+req.getMethod());
        Cookie lasttime = req.getCookie("lastTime");
        Log.e(TAG, "doRequest: cookie: "+((lasttime==null)?"cookie is null":(lasttime.getName()+"="+lasttime.getValue())));
        OutputStream os = resp.getOutputStream();
        resp.setStatus(HttpStatus.OK);
        if(lasttime != null) {
            String lasttimeValue = lasttime.getValue();
            try{
                long l = Long.parseLong(lasttimeValue);
                long now = System.currentTimeMillis();
                Log.e(TAG, "doRequest: "+l+" -- "+now);
                if((now - l) < 2 * 1000 * 60){
                    // 2分钟;
                    resp.setContentType("text/html");
                    os.write("already login".getBytes("UTF-8"));
                    os.flush();
                }else{
                    // 2分钟;
                    resp.setContentType("text/html");
                    os.write("session timeout".getBytes("UTF-8"));
                    os.flush();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            resp.setContentType("text/html");
            InputStream is = null;
            try {
                is = getAssetManager().open("html/index.html");
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf))!=-1){
                    os.write(buf,0,len);
                    os.flush();
                }
                is.close();
            } catch (IOException ignored) {

            }finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {

                    }
                    is = null;
                }
            }
        }
    }
}
