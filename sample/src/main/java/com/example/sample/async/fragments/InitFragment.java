package com.example.sample.async.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appbase.async.AbsAsyncEvent;
import com.example.appbase.async.AsyncEventCallback;
import com.example.appbase.async.AsyncEventQueue;
import com.example.appbase.async.AsyncEventSession;
import com.example.sample.R;
import com.example.sample.async.dialogs.SimpleLoadingDialog;
import com.example.sample.async.initer.AbsInitQueue;
import com.example.sample.async.initer.AbsIniter;
import com.example.sample.async.initer.GetBuildListIniter;
import com.example.sample.async.initer.GetCardListIniter;
import com.example.sample.async.initer.GetCellListIniter;
import com.example.sample.async.initer.GetDeviceInfoIniter;
import com.example.sample.async.initer.GetGZCardIniter;
import com.example.sample.async.initer.GetGZDeviceInfoIniter;
import com.example.sample.async.initer.GetUserIdentityIniter;
import com.example.sample.async.initer.GetUserinfoIniter;
import com.example.sample.dialogs.InitDialog;
import com.example.sample.initers.GetFaceIniter;


import java.util.ArrayList;
import java.util.List;


public class InitFragment extends Fragment implements View.OnClickListener, SimpleLoadingDialog.Callback {
    private static final String TAG = "InitFragment";
    private SimpleLoadingDialog dialog;
    private AsyncEventSession<AbsInitQueue, AbsIniter> initSession;
    private InitDialog initDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_init,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    void findView(View v){
        v.findViewById(R.id.init_btn_main).setOnClickListener(this);
        v.findViewById(R.id.init_btn_main_ajb).setOnClickListener(this);
        v.findViewById(R.id.init_btn_xj_zr).setOnClickListener(this);
        v.findViewById(R.id.init_btn_xj_gz).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init_btn_main:     init_main();       break;
            case R.id.init_btn_main_ajb: init_main_ajb();   break;
            case R.id.init_btn_xj_zr:    init_xj_zr();      break;
            case R.id.init_btn_xj_gz:    init_xj_gz();      break;
        }
    }

    private List<AbsIniter> items = new ArrayList<>();
    private void init_xj_gz() {

        AsyncEventSession<AbsInitQueue, AbsIniter> session = new AsyncEventSession<>();

        session.append(new AbsInitQueue(new GetGZDeviceInfoIniter()));

        session.append(new AbsInitQueue(new GetGZCardIniter()));

        session.startSession(new AsyncEventCallback<AbsInitQueue, AbsIniter>() {
            @Override
            public void onStartSession(AsyncEventSession<AbsInitQueue, AbsIniter> session) {
                Log.e(TAG, "onStartSession: ");
                items.clear();
                initDialog = new InitDialog(getActivity(), items);
                initDialog.show();
            }

            @Override
            public void onEndSession(AsyncEventSession<AbsInitQueue, AbsIniter> session, boolean success) {
                Log.e(TAG, "onEndSession: ");
            }

            @Override
            public void onEventStart(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur) {
                items.add(cur);
                initDialog.notifyDataSetChange();
                Log.e(TAG, "onEventStart: ");
            }

            @Override
            public void onEventEnd(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur) {
                Log.e(TAG, "onEventEnd: ");
            }

            @Override
            public boolean onEventFail(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur, Object extra) {
                Log.e(TAG, "onEventFail: ");
                return false;
            }

            @Override
            public boolean onEventException(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur, Throwable t) {
                Log.e(TAG, "onEventException: ");
                return false;
            }

            @Override
            public void onEventQueueStart(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsInitQueue queue) {
                Log.e(TAG, "onEventQueueStart: ");
            }

            @Override
            public void onEventQueueEnd(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsInitQueue queue) {
                Log.e(TAG, "onEventQueueEnd: ");
            }
        });
    }

    private void init_xj_zr() {
        AsyncEventSession<AbsInitQueue, AbsIniter> session = new AsyncEventSession<>();

        session.append(new AbsInitQueue(new GetDeviceInfoIniter()));

        session.append(new AbsInitQueue(new GetUserIdentityIniter()));

        session.append(new AbsInitQueue(new GetBuildListIniter()));

        session.append(new AbsInitQueue(new GetCellListIniter(), new GetCardListIniter()));

        session.startSession(new AsyncEventCallback<AbsInitQueue, AbsIniter>() {
            @Override
            public void onStartSession(AsyncEventSession<AbsInitQueue, AbsIniter> session) {
                Log.e(TAG, "onStartSession: ");
            }

            @Override
            public void onEndSession(AsyncEventSession<AbsInitQueue, AbsIniter> session, boolean success) {
                Log.e(TAG, "onEndSession: ");
            }

            @Override
            public void onEventStart(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur) {
                Log.e(TAG, "onEventStart: ");
            }

            @Override
            public void onEventEnd(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur) {
                Log.e(TAG, "onEventEnd: ");
            }

            @Override
            public boolean onEventFail(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur, Object extra) {
                Log.e(TAG, "onEventFail: ");
                return false;
            }

            @Override
            public boolean onEventException(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsIniter cur, Throwable t) {
                Log.e(TAG, "onEventException: ");
                return false;
            }

            @Override
            public void onEventQueueStart(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsInitQueue queue) {
                Log.e(TAG, "onEventQueueStart: ");
            }

            @Override
            public void onEventQueueEnd(AsyncEventSession<AbsInitQueue, AbsIniter> session, AbsInitQueue queue) {
                Log.e(TAG, "onEventQueueEnd: ");
            }
        });
    }

    private void init_main_ajb() {
        AsyncEventSession<AbsInitQueue, AbsIniter> initSession = new AsyncEventSession<>();

        // 初始化第一步: 获取设备初始化信息
        final AbsInitQueue first = new AbsInitQueue(new GetDeviceInfoIniter());
        initSession.append(first);

        // 初始化第二步: 获取楼栋列表
        AbsInitQueue second = new AbsInitQueue(new GetBuildListIniter());
        initSession.append(second);

        // 初始化第三步: 获取单元信息, 门卡列表
        final AbsInitQueue thrid = new AbsInitQueue(new GetCellListIniter(), new GetCardListIniter());
        initSession.append(thrid);

        initSession.startSession(new AsyncEventCallback<AbsInitQueue,AbsIniter>() {
            @Override
            public void onStartSession( AsyncEventSession<AbsInitQueue,AbsIniter> session) {
                showLoadingDialog();
                Log.e(TAG, "onStartSession: ");
            }

            @Override
            public void onEndSession( AsyncEventSession<AbsInitQueue,AbsIniter> session, boolean success) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                Log.e(TAG, "onEndSession: "+success+" // "+session.getStatus());
            }

            @Override
            public void onEventStart( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur) {
                initer.add(cur);
                dialog.notifyDataSetChanged();
                Log.e(TAG, "onEventStart: "+cur.getHintExtra());
            }

            @Override
            public void onEventEnd( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur) {
                Log.e(TAG, "onEventEnd: "+cur.getHintExtra()+"\n"

                        + session.getHandledCount() + " // "+session.getHandledFailCount()+" // "
                        +session.getHandledExceptionCount() + " // "+session.getAsyncEventCount());

                initer.remove(cur);
                // dialog.notifyDataSetChanged();
            }

            @Override
            public boolean onEventFail( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur, Object extra) {
                return false;
            }

            @Override
            public boolean onEventException( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur, Throwable t) {
                return false;
            }

            @Override
            public void onEventQueueStart( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsInitQueue queue) {
                /*
                例如:
                可以判断queue是不是
                 */
                if(queue == thrid){
                    queue.add(new GetUserinfoIniter());
                }
            }

            @Override
            public void onEventQueueEnd( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsInitQueue queue) {
                if(queue == first){
                    session.insertEventQueue(new AbsInitQueue(new GetBuildListIniter()));
                }
                int asyncEventCount = session.getAsyncEventCount();
                int handledCount = session.getHandledCount();
                Log.e(TAG, "onEventQueueEnd: "+asyncEventCount+" // "+handledCount);
            }
        });
    }

    private void init_main() {
        initSession = new AsyncEventSession<>();

        // 初始化第一步: 获取设备初始化信息
        final AbsInitQueue first = new AbsInitQueue(new GetDeviceInfoIniter());
        initSession.append(first);

        // 初始化第二步: 获取楼栋列表
       /* AbsInitQueue second = new AbsInitQueue(new GetBuildListIniter());
        initSession.append(second);*/

        // 初始化第三步: 获取单元信息, 门卡列表
        final AbsInitQueue thrid = new AbsInitQueue(new GetCellListIniter(), new GetCardListIniter());
        initSession.append(thrid);

        initSession.startSession(new AsyncEventCallback<AbsInitQueue,AbsIniter>() {
            @Override
            public void onStartSession( AsyncEventSession<AbsInitQueue,AbsIniter> session) {
                showLoadingDialog();
                Log.e(TAG, "onStartSession: ");
            }

            @Override
            public void onEndSession( AsyncEventSession<AbsInitQueue,AbsIniter> session, boolean success) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                Log.e(TAG, "onEndSession: "+success+" // "+session.getStatus());
                initSession = null;
            }

            @Override
            public void onEventStart( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur) {
                initer.add(cur);
                dialog.notifyDataSetChanged();
                Log.e(TAG, "onEventStart: "+cur.getHintExtra());
            }

            @Override
            public void onEventEnd( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur) {
                Log.e(TAG, "onEventEnd: "+cur.getHintExtra()+"\n"

                        + session.getHandledCount() + " // "+session.getHandledFailCount()+" // "
                +session.getHandledExceptionCount() + " // "+session.getAsyncEventCount());

                initer.remove(cur);
                // dialog.notifyDataSetChanged();
            }

            @Override
            public boolean onEventFail( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur, Object extra) {
                return false;
            }

            @Override
            public boolean onEventException( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsIniter cur, Throwable t) {
                return false;
            }

            @Override
            public void onEventQueueStart( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsInitQueue queue) {
                /*
                例如:
                可以判断queue是不是
                 */
                if(queue == thrid){
                    queue.add(new GetUserinfoIniter());
                }
            }

            @Override
            public void onEventQueueEnd( AsyncEventSession<AbsInitQueue,AbsIniter> session, AbsInitQueue queue) {
                if(queue == first){
                    session.insertEventQueue(new AbsInitQueue(new GetBuildListIniter()));
                }
                int asyncEventCount = session.getAsyncEventCount();
                int handledCount = session.getHandledCount();
                Log.e(TAG, "onEventQueueEnd: "+asyncEventCount+" // "+handledCount);
            }
        });
    }
    List<AbsIniter> initer = new ArrayList<AbsIniter>();
    private void showLoadingDialog(){
        dialog = new SimpleLoadingDialog(getActivity(), initer, this);
        dialog.show();
    }

    @Override
    public void onCancle() {
        if (initSession != null) {
            initSession.interruptSession();
        }
    }
}
