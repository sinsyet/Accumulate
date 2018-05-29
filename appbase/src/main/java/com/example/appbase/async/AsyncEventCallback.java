package com.example.appbase.async;

import android.support.annotation.MainThread;


@MainThread
public interface AsyncEventCallback<EventQueue extends AsyncEventQueue<AsyncHandler>,
        AsyncHandler extends AbsAsyncEvent> {

    /**
     * 初始化会话开始
     * @param session 会话对象, 用于获取状态
     */
    void onStartSession(AsyncEventSession<EventQueue, AsyncHandler> session);

    /**
     * 初始化会话结束
     * @param session 会话对象
     * @param success 会话是否成功
     */
    void onEndSession(AsyncEventSession<EventQueue, AsyncHandler> session, boolean success);

    /**
     * 某个初始化者开始初始化时
     * @param cur 即将进行初始化的初始化者
     */
    void onEventStart(AsyncEventSession<EventQueue, AsyncHandler> session, AsyncHandler cur);

    /**
     * 某个初始化者结束初始化时
     * @param cur 结束初始化的初始化者;
     */
    void onEventEnd(AsyncEventSession<EventQueue, AsyncHandler> session, AsyncHandler cur);


    /**
     * 初始化失败
     * @param cur 初始化失败的初始化者
     * @param extra 数据
     * @return true表示忽略这个错误, 继续执行初始化; false表示结束初始化
     */
    boolean onEventFail(AsyncEventSession<EventQueue, AsyncHandler> session, AsyncHandler cur, Object extra);

    /**
     * 初始化异常
     * @param cur 初始化异常的初始化者
     * @param t 异常对象
     * @return true表示忽略这个异常, 继续执行初始化; false表示结束初始化
     */
    boolean onEventException(AsyncEventSession<EventQueue, AsyncHandler> session, AsyncHandler cur, Throwable t);

    /**
     * 事件队列执行前, 用于临时给下一个任务队列追加AsyncEvent
     *
     * @param session 绘画对象
     * @param queue 队列
     */
    void onEventQueueStart(AsyncEventSession<EventQueue, AsyncHandler> session, EventQueue queue);

    /**
     * 事件队列执行完毕后, 用于在临时需要追加请求队列
     *
     * @param session 会话对象;
     * @param queue 刚执行结束的队列
     */
    void onEventQueueEnd(AsyncEventSession<EventQueue, AsyncHandler> session, EventQueue queue);
}
