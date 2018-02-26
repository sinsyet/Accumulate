package com.example.androidhttpserver.servlet.impl;

import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.http.Cookie;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class AndroidServletRequestImpl implements IAndroidServletRequest {
    private Map<String,String> mParamters = new HashMap<>();
    private String mRequestMethod;

    private Map<String,String> mHeaders = new HashMap<>();

    private InputStream mRequestInputStream;

    private Map<String,Cookie> mCookies = new HashMap<>();
    private String mReqUri;

    public void injectReqMethod(String method){
        this.mRequestMethod = method;
    }

    public void injectParamter(String key,String value){
        mParamters.put(key, value);
    }

    public void injectHeader(String name,String value){
        mHeaders.put(name, value);
    }

    public void injectCookie(Cookie cookie){
        mCookies.put(cookie.getName(),cookie);
    }

    public void injectInputStream(InputStream is){
        this.mRequestInputStream = is;
    }

    public void injectReqUri(String uri){
        this.mReqUri = uri;
    }

    @Override
    public String getParamter(String name) {
        return mParamters.get(name);
    }

    @Override
    public Map<String, String> getParamterMap() {
        return mParamters;
    }

    @Override
    public String getMethod() {
        return mRequestMethod;
    }

    @Override
    public String getHeader(String name) {
        return mHeaders.get(name);
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return mHeaders;
    }

    @Override
    public InputStream getInputStream() {
        return this.mRequestInputStream;
    }

    @Override
    public Collection<Cookie> getCookies() {
        return mCookies.values();
    }

    @Override
    public String getReqUri() {
        return this.mReqUri;
    }

    @Override
    public Cookie getCookie(String key) {
        return mCookies.get(key);
    }
}
