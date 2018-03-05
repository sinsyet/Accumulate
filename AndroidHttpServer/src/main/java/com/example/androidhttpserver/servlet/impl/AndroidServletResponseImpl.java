package com.example.androidhttpserver.servlet.impl;

import android.text.TextUtils;
import android.util.Log;

import com.example.androidhttpserver.NanoHTTPD;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;
import com.example.androidhttpserver.servlet.http.Cookie;
import com.example.androidhttpserver.servlet.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AndroidServletResponseImpl implements IAndroidServletResponse {
    private static final String TAG = "AndroidServletResponseI";
    private List<Cookie> mCookies = new ArrayList<>();

    private Map<String,String> mHeaders = new HashMap<>();

    private ByteArrayOutputStream mBaos = new ByteArrayOutputStream();

    /**
     * HTTP status code after processing, e.g. "200 OK", HTTP_OK
     */
    private HttpStatus status;
    /**
     * MIME type of content, e.g. "text/html"
     */
    private String contentType;
    /**
     * Data of the response, may be null.
     */
    private InputStream data;
    /**
     * Headers for the HTTP response. Use addHeader() to add lines.
     */
    private Map<String, String> header = new HashMap<String, String>();
    /**
     * The request method that spawned this response.
     */
    private NanoHTTPD.Method requestMethod;
    /**
     * Use chunkedTransfer
     */
    private boolean chunkedTransfer;

    private String host = "";

    @Override
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void addCookie(Cookie cookie) {
        mCookies.add(cookie);
    }

    @Override
    public void setHeader(String name, String value) {
        mHeaders.put(name, value);
    }

    @Override
    public OutputStream getOutputStream() {
        return mBaos;
    }

    @Override
    public void sendRedirect(String path) {
        if(TextUtils.isEmpty(path)) return;

        if(path.startsWith("./")) {
            String value = "http://" + host + path.substring(1);
            Log.e(TAG, "sendRedirect: ");
            mHeaders.put("Location", value);
            return;
        }


        if(path.length() >= 8){
            String schema = path.substring(0, 8);
            if(schema.toLowerCase().startsWith("https://")){
                mHeaders.put("Location",path);
                return;
            }
        }

        if(path.length() >= 7){
            String schema = path.substring(0, 7);
            if(schema.toLowerCase().startsWith("http://")){
                mHeaders.put("Location",path);
                return;
            }
        }

        mHeaders.put("Location","http://"+path);
    }

    public String toResponseString(){
        return mBaos.toString();
    }

    public HttpStatus getStatus(){
        return status;
    }

    public String getContentType(){
        return contentType;
    }

    public List<Cookie> getCookies(){
        return mCookies;
    }

    public Map<String,String> getHeaders(){
        return mHeaders;
    }

    void setHost(String host){
        this.host = host;
    }

    void clearOutputStream(){
        mBaos.reset();
    }
}
