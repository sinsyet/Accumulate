package com.example.viewlib.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 *     @author sin
 *     @date 2017-06-03
 *     @desc 加载布局
 * </pre>
 */

public class LoadLayout extends FrameLayout {

    private static final String TAG = "LoadLayout";

    /**
     * 加载中显示的布局
     */
    private View mLoadingView;
    /**
     * 加载失败显示的布局
     */
    private View mLoadFailView;
    /**
     * 加载成功显示的布局
     */
    private View mLoadSuccessView;
    /**
     * 加载数据为空显示的布局
     */
    private View mLoadEmptyView;

    /**
     * 状态和对应的布局的映射集合:
     * 采用这样的集合来维护映射关系是为了方便后续 隐藏或显示指定状态的布局时的操作
     */
    private SparseArray<View> mStateViewMap;

    /**
     * 加载的状态
     */
    private int mLoadState = -1;
    /**
     * 上一个加载状态
     */
    private int mLastLoadState;

    private int mDefaultState = 0;

    private boolean displayDefaultState = false;

    private Map<String,Integer> mTagStateMaps = new HashMap<>();

    public interface State {
        /**
         * 加载中
         */
        int LOADING = 0;

        /**
         * 加载失败
         */
        int NET_DISABLE = 1;

        /**
         * 加载成功
         */
        int SUCCESS = 2;

        /**
         * 加载的数据为空
         */
        int EMPTY = 3;
    }

    public LoadLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // initAttr(context,attrs);
        init();
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                      @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // initAttr(context,attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                      @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // initAttr(context,attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        // 1. 初始化Map
        mStateViewMap = new SparseArray<>();

        mTagStateMaps.put("loading", State.LOADING);
        mTagStateMaps.put("loadEmpty", State.EMPTY);
        mTagStateMaps.put("loadFail", State.NET_DISABLE);
        mTagStateMaps.put("loaded", State.SUCCESS);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                injectStateViewMap();
                // 2. 注入状态和布局对应的映射
            }
        });

    }

    /**
     * 注入状态和对应布局的映射
     */
    private void injectStateViewMap() {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            View view = getChildAt(i);
            try{
                String tag = (String) view.getTag();
                if (mTagStateMaps.containsKey(tag)) {
                    Integer state = mTagStateMaps.get(tag);
                    mStateViewMap.put(state, view);
                    if(state == mDefaultState){
                        view.setVisibility(VISIBLE);
                        displayDefaultState = true;
                    }else {
                        view.setVisibility(GONE);
                    }
                }else {
                }
            }catch (Exception e){
            }
        }

        if(!displayDefaultState){
            refreshLayout(State.LOADING);
        }
    }

    /**
     * 刷新布局
     */
    private void refreshLayout() {
        View view = mStateViewMap.get(mLoadState);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }

        View lastLoadView = mStateViewMap.get(mLastLoadState);
        if (lastLoadView != null) {
            lastLoadView.setVisibility(View.GONE);
        }
    }


    protected void addStateView2LoadLayout(int state, View newStateView) {
        View view = mStateViewMap.get(state);
        if (view != null) {
            removeView(view);
        }
        newStateView.setVisibility(View.GONE);
        mStateViewMap.put(state, newStateView);
        addView(newStateView);
    }

    @UiThread
    public void setLoadingView(View loadingView) {
        if (loadingView == null) {
            Log.d(TAG, "setLoadingView: loadingView is null");
            return;
        }
        this.mLoadingView = loadingView;
        addStateView2LoadLayout(State.LOADING, loadingView);
        refreshLayout();
    }

    @UiThread
    public void setLoadFailView(View loadFailView) {
        if (loadFailView == null) {
            Log.d(TAG, "setLoadFailView: loadFailView is null");
            return;
        }
        this.mLoadFailView = loadFailView;
        addStateView2LoadLayout(State.NET_DISABLE, loadFailView);
        refreshLayout();
    }

    @UiThread
    public void setLoadSuccessView(View loadSuccessView) {
        if (loadSuccessView == null) {
            Log.d(TAG, "setLoadSuccessView: loadSuccessView is null");
            return;
        }
        this.mLoadSuccessView = loadSuccessView;
        addStateView2LoadLayout(State.SUCCESS, loadSuccessView);
        refreshLayout();
    }

    @UiThread
    public void setLoadEmptyView(View loadEmptyView) {
        if (loadEmptyView == null) {
            Log.d(TAG, "setLoadEmptyView: loadEmptyView is null");
            return;
        }
        this.mLoadEmptyView = loadEmptyView;
        addStateView2LoadLayout(State.EMPTY, loadEmptyView);
        refreshLayout();
    }

    /**
     * 更新布局
     */
    public void refreshLayout(int loadState) {
        if (mStateViewMap.indexOfKey(loadState) < 0) {
            Log.d(TAG, loadState + " is unlawful state code");
            mDefaultState = loadState;
            return;
        }

        if(loadState == mLoadState){
            Log.d(TAG,"same state, do not need refreshLayout");
            return;
        }

        mLastLoadState = mLoadState;
        mLoadState = loadState;
        refreshLayout();
    }

    /**
     * 更新布局, 这个方法可以在子线程中调用
     */
    public void postRefreshLayout(final int loadState) {
        post(new Runnable() {
            @Override
            public void run() {
                refreshLayout(loadState);
            }
        });
    }

    /**
     * 将状态修改为正在加载
     */
    public void onLoading() {
        if ("main".equals(Thread.currentThread().getName())) {
            refreshLayout(State.LOADING);
        } else {
            postRefreshLayout(State.LOADING);
        }
    }

    /**
     * 将布局修改为显示加载失败
     */
    public void onLoadFail() {
        if ("main".equals(Thread.currentThread().getName())) {
            refreshLayout(State.NET_DISABLE);
        } else {
            postRefreshLayout(State.NET_DISABLE);
        }
    }

    /**
     * 将布局修改为显示加载成功
     */
    public void onLoadSuccess() {
        if ("main".equals(Thread.currentThread().getName())) {
            refreshLayout(State.SUCCESS);
        } else {
            postRefreshLayout(State.SUCCESS);
        }
    }

    /**
     * 将布局修改为显示加载数据为空
     */
    public void onLoadEmpty() {
        if ("main".equals(Thread.currentThread().getName())) {
            refreshLayout(State.EMPTY);
        } else {
            postRefreshLayout(State.EMPTY);
        }
    }
}
