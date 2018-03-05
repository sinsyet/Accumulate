package com.example.androidhttpserver.servlet.impl;

import com.example.androidhttpserver.servlet.IRequestDispatcher;
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
    private AndroidHttpServer server;

    void injectReqMethod(String method){
        this.mRequestMethod = method;
    }

    void injectParamter(String key,String value){
        mParamters.put(key, value);
    }

    void injectHeader(String name,String value){
        mHeaders.put(name, value);
    }

    void injectCookie(Cookie cookie){
        mCookies.put(cookie.getName(),cookie);
    }

    void injectInputStream(InputStream is){
        this.mRequestInputStream = is;
    }

    void injectReqUri(String uri){
        this.mReqUri = uri;
    }

    void injectServer(AndroidHttpServer server){
        this.server = server;
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

    @Override
    public IRequestDispatcher getRequestDispatcher(String targetUrl) {
        AndroidRequestDispatcherImpl dispatcher = new AndroidRequestDispatcherImpl(targetUrl);
        dispatcher.injectServer(server);
        return dispatcher;
    }
}
