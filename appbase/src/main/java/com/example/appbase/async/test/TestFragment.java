package com.example.appbase.async.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.appbase.async.asyn.AbsAsyncIniter;
import com.example.appbase.async.asyn.AsyncInitCallback;
import com.example.appbase.async.asyn.AsyncInitQueue;
import com.example.appbase.async.asyn.AsyncInitSession;
import com.example.appbase.async.sample.GetCardIniter;
import com.example.appbase.async.sample.GetDeviceInfoIniter;
import com.example.appbase.async.sample.GetFaceIniter;
import com.example.appbase.async.sample.GetHouseListIniter;
import com.example.appbase.async.sample.GetUserInfoIniter;



public class TestFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AsyncInitSession initSession = new AsyncInitSession();
        // 初始化分为三个步骤, 第一是获取设备信息
        initSession.append(new AsyncInitQueue(new GetDeviceInfoIniter()));

        // 初始化分为三个步骤, 第二是获取房间列表
        initSession.append(new AsyncInitQueue(new GetHouseListIniter()));

        // 初始化分为三个步骤, 第三是获取人脸数据, 卡片数据, 住户数据
        initSession.append(new AsyncInitQueue(new GetFaceIniter(), new GetCardIniter(), new GetUserInfoIniter()));

        initSession.startSession(new AsyncInitCallback() {


            @Override
            public void onStartSession(AsyncInitSession session) {

            }

            @Override
            public void onEndSession(AsyncInitSession session, boolean success) {

            }

            @Override
            public void onIniterStart(AbsAsyncIniter absIniter) {

            }

            @Override
            public void onIniterEnd(AbsAsyncIniter absIniter) {

            }

            @Override
            public boolean onIniterFail(AbsAsyncIniter absIniter, Object extra) {
                return false;
            }

            @Override
            public boolean onIniterException(AbsAsyncIniter absIniter, Throwable t) {
                return false;
            }
        });
    }
}
