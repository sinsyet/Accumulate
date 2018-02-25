package com.example.androidhttpserver.webinfo;


import android.text.TextUtils;

import com.example.androidhttpserver.servlet.base.IAndroidServlet;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

public class WebMapping {

    private String url_pattern;
    private Class<? extends AndroidHttpServlet> servletClass;
    private String htmlPath;

    public WebMapping(String url_pattern, Class<? extends AndroidHttpServlet> servletClass){
        if(TextUtils.isEmpty(url_pattern) || servletClass == null)
            throw new IllegalArgumentException("args can't be null");

        if(!url_pattern.startsWith("/"))
            url_pattern = "/" + url_pattern;

        this.url_pattern = url_pattern;
        this.servletClass = servletClass;
    }

    public WebMapping(String url_pattern,String htmlPath){
        if(TextUtils.isEmpty(url_pattern) || TextUtils.isEmpty(htmlPath))
            throw new IllegalArgumentException("args can't be null");

        if(!url_pattern.startsWith("/"))
            url_pattern = "/" + url_pattern;

        this.url_pattern = url_pattern;
        this.htmlPath = htmlPath;
    }

    public Class<? extends AndroidHttpServlet> getServletClass() {
        return servletClass;
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public String getUrl_pattern() {
        return url_pattern;
    }
}
