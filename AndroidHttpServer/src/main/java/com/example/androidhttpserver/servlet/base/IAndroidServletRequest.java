package com.example.androidhttpserver.servlet.base;


import com.example.androidhttpserver.servlet.http.Cookie;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public interface IAndroidServletRequest {

    /**
     * 获取请求参数
     * @param name 请求参数的key
     * @return 请求参数的value
     */
    String getParamter(String name);

    /**
     * 获取请求参数的集合
     * @return 请求参数的双列集合
     */
    Map<String,String> getParamterMap();

    /**
     * 获取请求类型, get/post/....
     * @return 小写的请求类型;
     */
    String getMethod();

    /**
     * 获取指定请求头的值
     * @param name key
     * @return 请求头的value; 没有的时候, 返回null
     */
    String getHeader(String name);

    /**
     * 获取请求头集合
     * @return 请求头的双列集合
     */
    Map<String,String> getHeaderMap();

    /**
     * 获取请求的输入流
     * @return 输入流
     */
    InputStream getInputStream();


    /**
     * 获取cookies
     * @return cookies
     */
    Collection<Cookie> getCookies();

    /**
     * 获取请求连接
     */
    String getReqUri();

    Cookie getCookie(String key);
}
