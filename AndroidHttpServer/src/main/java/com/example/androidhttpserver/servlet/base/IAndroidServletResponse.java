package com.example.androidhttpserver.servlet.base;


import com.example.androidhttpserver.servlet.http.Cookie;
import com.example.androidhttpserver.servlet.http.HttpStatus;

import java.io.OutputStream;

public interface IAndroidServletResponse {

    void setStatus(HttpStatus status);

    /**
     * 文本信息; content Type: text/html;charset=utf-8  , 有点类似
     * @param contentType
     */
    void setContentType(String contentType);

    /**
     * cookie其实是key为Set-Cookie: cookieKey=cookieValue 的head信息;
     *
     * 如果写了多个cookie, 则有多个
     * Set-Cookie: cookieKeyA=cookieValueA
     * Set-Cookie: cookieKeyB=cookieValueB
     * ....
     * @param cookie cookie对象
     */
    void addCookie(Cookie cookie);

    void setHeader(String name,String value);

    OutputStream getOutputStream();

    void sendRedirect(String path);
}
