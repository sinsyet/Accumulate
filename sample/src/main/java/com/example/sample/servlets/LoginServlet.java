package com.example.sample.servlets;

import android.util.Log;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.http.Cookie;
import com.example.androidhttpserver.servlet.http.HttpStatus;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class LoginServlet extends AndroidHttpServlet {
    private static final String TAG = "LoginServlet";
    @Override
    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
        Map<String, String> paramters = req.getParamterMap();

        Log.e(TAG, "doRequest: "+req.getMethod());
        for (Map.Entry<String, String> entry : paramters.entrySet()) {
            Log.e(TAG, "doRequest: key: "+entry.getKey()+" -- value: "+entry.getValue());
        }

        resp.setMimeType("text/json");
        OutputStream os = resp.getOutputStream();
        JSONObject jsonObject = new JSONObject();
        resp.setStatus(HttpStatus.OK);
        Cookie lastTime = new Cookie("lastTime", System.currentTimeMillis() + "");
        resp.addCookie(lastTime);
        resp.addCookie(new Cookie("testKey","testValue"));
        try {
            jsonObject.put("status",200);
            jsonObject.put("msg","login success");
        } catch (JSONException ignored) {

        }
        try {
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
        } catch (IOException e) {

        }
    }
}
