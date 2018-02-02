package com.example.viewlib.layout;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.example.demo.R;
import com.example.demo.views.drawable.Circle;


public class SimpleLoadLayout extends LoadLayout {

    public interface SimpleMode{
        int SERVER_ERROR = 4;
    }

    private OnLoadActionListener l;

    private OnClickListener mLayoutClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (l == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.loadempty_ll:
                    l.onReLoadClick(OnLoadActionListener.RELOAD_4_EMPTY);
                    break;
                case R.id.servererror_btn_reload:
                    l.onReLoadClick(OnLoadActionListener.RELOAD_4_SERVERERROR);
                    break;
                case R.id.netdisable_btn_checknet:
                    l.onCheckNet();
                    break;
                case R.id.netdisable_btn_refresh:
                    l.onReLoadClick(OnLoadActionListener.RELOAD_4_NETDISABLE);
                    break;
            }
        }
    };
    public SimpleLoadLayout(@NonNull Context context) {
        super(context);
        initStateView();
    }

    public SimpleLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initStateView();
    }

    public SimpleLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStateView();
    }

    private void initStateView(){
        View loadingView = createLoadingView();
        setLoadingView(loadingView);

        View loadEmptyView = createLoadEmptyView();
        setLoadEmptyView(loadEmptyView);

        View loadFailView = createNetDisableView();
        setLoadFailView(loadFailView);

        View serverError = createServerErrorView();
        addStateView2LoadLayout(SimpleMode.SERVER_ERROR,serverError);
    }

    private View createServerErrorView() {
        View serverErrorView = View.inflate(getContext(), R.layout.layout_servererror, null);
        serverErrorView.findViewById(R.id.servererror_btn_reload).setOnClickListener(mLayoutClickListener);
        return serverErrorView;
    }

    private View createLoadingView(){
        View view = View.inflate(getContext(), R.layout.layout_loading, null);
        ProgressBar mPb = (ProgressBar) view.findViewById(R.id.loading_pb);
        mPb.setIndeterminateDrawable(new Circle());
        return view;
    }

    private View createLoadEmptyView(){
        View loadempty = View.inflate(getContext(), R.layout.layout_loadempty, null);
        loadempty.findViewById(R.id.loadempty_ll).setOnClickListener(mLayoutClickListener);
        return loadempty;
    }

    private View createNetDisableView(){
        View netDisableView = View.inflate(getContext(), R.layout.layout_netdisable, null);
        netDisableView.findViewById(R.id.netdisable_btn_checknet).setOnClickListener(mLayoutClickListener);
        netDisableView.findViewById(R.id.netdisable_btn_refresh).setOnClickListener(mLayoutClickListener);
        return netDisableView;
    }

    public void setOnLoadActionListener(OnLoadActionListener l){
        this.l = l;
    }

    public void onServerError(){
        if ("main".equals(Thread.currentThread().getName())) {
            refreshLayout(SimpleMode.SERVER_ERROR);
        } else {
            postRefreshLayout(SimpleMode.SERVER_ERROR);
        }
    }

    public interface OnLoadActionListener {
        int RELOAD_4_NETDISABLE = 1;
        int RELOAD_4_EMPTY = 2;
        int RELOAD_4_SERVERERROR = 3;

        void onReLoadClick(int reloadType);

        void onCheckNet();
    }

}
