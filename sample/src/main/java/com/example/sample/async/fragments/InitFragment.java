package com.example.sample.async.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appbase.async.AsyncEventCallback;
import com.example.appbase.async.AsyncEventSession;
import com.example.apphelper.ToastUtil;
import com.example.sample.R;
import com.example.sample.async.initer.AbsInitQueue;
import com.example.sample.async.initer.AbsIniter;
import com.example.sample.async.initer.GetArcFaceInfoIniter;
import com.example.sample.async.initer.GetBuildListIniter;
import com.example.sample.async.initer.GetDoorCardListIniter;
import com.example.sample.async.initer.GetDeviceInfoIniter;
import com.example.sample.async.initer.GetGZCardIniter;
import com.example.sample.async.initer.GetGZDeviceInfoIniter;
import com.example.sample.async.initer.GetHouseListIniter;
import com.example.sample.async.initer.GetUserIdentityIniter;
import com.example.sample.async.initer.GetUserinfoIniter;
import com.example.sample.dialogs.InitDialog;


import java.util.ArrayList;
import java.util.List;


public class InitFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "InitFragment";
    private InitDialog initDialog;
    private AsyncEventCallback<AbsInitQueue, AbsIniter> asynInitCallback = new AsyncEventCallback<AbsInitQueue, AbsIniter>() {
        @Override
        public void onStartSession(AsyncEventSession<AbsInitQueue, AbsIniter> session) {
            items.clear();
            initDialog = new InitDialog(getActivity(), items);
            initDialog.setCallback(new InitDialog.Callback() {
                @Override
                public void onAction(int op) {
                    switch (op) {
                        case OP_INTERRUPT:
                            session.interruptSession();
                            initDialog.onInterrupt();
                            break;
                    }
                }
            });
            initDialog.setCancelable(false);
            initDialog.show();
        }

        @Override
        public void onEndSession(AsyncEventSession<AbsInitQueue, AbsIniter> session, boolean success) {
            if (success) {
                initDialog.dismiss();
                initDialog = null;
                ToastUtil.show(getContext(), R.string.init_success);
                items.clear();
            } else {
                int status = session.getStatus();
                if (status == AsyncEventSession.Status.INTERRUPT) {
                    initDialog.dismiss();
                    initDialog = null;
                    items.clear();
                    ToastUtil.show(getContext(), R.string.init_interrupt);
                } else {
                    initDialog.onInitFail();
                }
            }
            Log.e(TAG, "onEndSession: "+success);
        }

        @Override
        public void onEventStart(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur) {
            items.add(cur);
            initDialog.notifyDataSetChange();
            int handledCount = session.getHandledCount();
            int handledFailCount = session.getHandledFailCount();
            int handledExceptionCount = session.getHandledExceptionCount();
            initDialog.updateProgress(handledCount + handledFailCount + handledExceptionCount,
                    session.getAsyncEventCount());
        }

        @Override
        public void onEventEnd(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur) {
            initDialog.notifyDataSetChange();
        }

        @Override
        public boolean onEventFail(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur, Object extra) {
            return false;
        }

        @Override
        public boolean onEventException(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur, Throwable t) {
            return false;
        }

        @Override
        public void onEventQueueStart(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsInitQueue queue) {

        }

        @Override
        public void onEventQueueEnd(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsInitQueue queue) {
            initDialog.notifyDataSetChange();
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_init, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    void findView(View v) {
        v.findViewById(R.id.init_btn_main).setOnClickListener(this);
        v.findViewById(R.id.init_btn_main_ajb).setOnClickListener(this);
        v.findViewById(R.id.init_btn_xj_zr).setOnClickListener(this);
        v.findViewById(R.id.init_btn_xj_gz).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init_btn_main:
                init_main();
                break;
            case R.id.init_btn_main_ajb:
                init_main_ajb();
                break;
            case R.id.init_btn_xj_zr:
                init_xj_zr();
                break;
            case R.id.init_btn_xj_gz:
                init_xj_gz();
                break;
        }
    }

    private List<AbsIniter> items = new ArrayList<>();

    private void init_xj_gz() {
        initFromGZServer(0);
    }

    private void init_xj_zr() {
        initFromZZWServer(1);
    }

    private void init_main_ajb() {
        initFromZZWServer(1);
    }

    private void init_main() {
        initFromZZWServer(1);
    }

    /**
     *
     * @param type 类型; 0, 主线机型-门口机; 1, 主线机型-围墙机;
     */
    private void initFromZZWServer(int type){
        // 创建一个异步事件会话; 泛型AbsInitQueue是初始化者池类型, 泛型AbsIniter初始化者
        AsyncEventSession<AbsInitQueue, AbsIniter> session = new AsyncEventSession<>();

        // 往异步事件会话中追加一个初始化者池;
            // 它包裹了一个初始化任务: 初始化设备信息
        session.append(new AbsInitQueue(new GetDeviceInfoIniter()));

        // 再追加一个初始化任务池; 初始化房间列表
        session.append(new AbsInitQueue(new GetBuildListIniter()));

        // 再追加一个初始化任务池, 里面包含了
        session.append(new AbsInitQueue(
                new GetDoorCardListIniter(),    // 获取门卡列表
                new GetHouseListIniter(),       // 获取房间列表
                new GetArcFaceInfoIniter(),     // 获取虹软离线人脸数据列表
                new GetDoorCardListIniter(),    // 获取
                new GetUserinfoIniter(),        // 获取住户信息列表 (AJB机型才需要获取, 建议在Initer对象
                                                // 里自己做判断是否需要更新, 不需要更新则调用onNext结束更新即可)
                new GetUserIdentityIniter()     // 更新住户身份信息,HR机型需要更新, 同上, 内部自己做判断是否需要更新
                ));

        session.startSession(asynInitCallback);
    }

    /**
     *
     * @param type 类型; 0, 主线机型-门口机; 1, 主线机型-围墙机;
     */
    private void initFromGZServer(int type){
        // 创建一个异步事件会话; 泛型AbsInitQueue是初始化者池类型, 泛型AbsIniter初始化者
        AsyncEventSession<AbsInitQueue, AbsIniter> session = new AsyncEventSession<>();

        // 往异步事件会话中追加一个初始化者池;
        // 它包裹了一个初始化任务: 初始化设备信息
        session.append(new AbsInitQueue(new GetGZDeviceInfoIniter()));

        // 再追加一个初始化任务池; 初始化房间列表
        session.append(new AbsInitQueue(new GetGZCardIniter()));

        session.startSession(asynInitCallback);
    }
}
