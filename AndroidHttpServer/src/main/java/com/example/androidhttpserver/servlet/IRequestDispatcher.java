package com.example.androidhttpserver.servlet;


import com.example.androidhttpserver.servlet.base.IAndroidServletRequest;
import com.example.androidhttpserver.servlet.base.IAndroidServletResponse;

/**
 * 转发器
 *
 * @author YGX
 */
public interface IRequestDispatcher {

    void forward(IAndroidServletRequest req, IAndroidServletResponse resp);

    void include(IAndroidServletRequest req,IAndroidServletResponse resp);
}
