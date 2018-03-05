package com.example.androidhttpserver.webinfo;


import android.text.TextUtils;

import com.example.androidhttpserver.servlet.exception.NoServletException;
import com.example.androidhttpserver.servlet.impl.AndroidHttpServlet;

public class WebMapping {

    private String url_pattern;
    private Class<? extends AndroidHttpServlet> servletClass;
    private String htmlPath;

    private int mFlag;
    private final static int URL_PATTERN = 1;
    private final static int SERVLET_CLASS = 1 << 1;
    private final static int HTML_PATH = 1 << 2;

    public WebMapping(){}

    public WebMapping(String url_pattern, Class<? extends AndroidHttpServlet> servletClass){
        if(TextUtils.isEmpty(url_pattern) || servletClass == null)
            throw new IllegalArgumentException("args can't be null");

        if(!url_pattern.startsWith("/"))
            url_pattern = "/" + url_pattern;

        this.url_pattern = url_pattern;
        this.servletClass = servletClass;

        mFlag |= URL_PATTERN;
        mFlag |= SERVLET_CLASS;
    }

    public WebMapping(String url_pattern,String htmlPath){
        if(TextUtils.isEmpty(url_pattern) || TextUtils.isEmpty(htmlPath))
            throw new IllegalArgumentException("args can't be null");

        if(!url_pattern.startsWith("/"))
            url_pattern = "/" + url_pattern;

        this.url_pattern = url_pattern;
        this.htmlPath = htmlPath;
        mFlag |= URL_PATTERN;
        mFlag |= HTML_PATH;
    }

    public void setUrl_pattern(String url){
        this.url_pattern = url;
        mFlag |= URL_PATTERN;
    }

    public void setServletClass(Class<? extends AndroidHttpServlet>clazz){
        this.servletClass = clazz;
        mFlag |= SERVLET_CLASS;
    }

    public void loadServletClazz(String clazzName){
        try {
            Class<?> clazz = Class.forName(clazzName);
            this.servletClass = (Class<? extends AndroidHttpServlet>) clazz;
            mFlag |= SERVLET_CLASS;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoServletException(clazzName);
        }
    }

    public void setHtmlPath(String path){
        mFlag |= HTML_PATH;
        this.htmlPath = path;
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

    @Override
    public String toString() {
        return "WebMapping{" +
                "url_pattern='" + url_pattern + '\'' +
                ", servletClass=" + servletClass +
                ", htmlPath='" + htmlPath + '\'' +
                '}';
    }

    public boolean isValid(){
        return (mFlag == (URL_PATTERN | SERVLET_CLASS)) || (mFlag == (URL_PATTERN | HTML_PATH));
    }
}
