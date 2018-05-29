package com.example.appbase.async.asyn;

import android.support.annotation.MainThread;

import com.example.appbase.async.AbsIniter;


@MainThread
public interface AsyncInitCallback {

    /**
     * 初始化会话开始
     * @param session 会话对象, 用于获取状态
     */
    void onStartSession(AsyncInitSession session);

    /**
     * 初始化会话结束
     * @param session 会话对象
     * @param success 会话是否成功
     */
    void onEndSession(AsyncInitSession session, boolean success);

    /**
     * 某个初始化者开始初始化时
     * @param absIniter 即将进行初始化的初始化者
     */
    void onIniterStart(AbsIniter absIniter);

    /**
     * 某个初始化者结束初始化时
     * @param absIniter 结束初始化的初始化者;
     */
    void onIniterEnd(AbsIniter absIniter);


    /**
     * 初始化失败
     * @param absIniter 初始化失败的初始化者
     * @param extra 数据
     * @return true表示忽略这个错误, 继续执行初始化; false表示结束初始化
     */
    boolean onIniterFail(AbsIniter absIniter, Object extra);

    /**
     * 初始化异常
     * @param absIniter 初始化异常的初始化者
     * @param t 异常对象
     * @return true表示忽略这个异常, 继续执行初始化; false表示结束初始化
     */
    boolean onIniterException(AbsIniter absIniter, Throwable t);
}
